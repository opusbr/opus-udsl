package br.com.opussoftware.tools.udsl.generator.k8s

import br.com.opussoftware.tools.udsl.generator.Generator
import br.com.opussoftware.tools.udsl.generator.ResourceLoader
import br.com.opussoftware.tools.udsl.generator.util.FileTreeBuilder
import br.com.opussoftware.tools.udsl.generator.util.TemplateHelper
import br.com.opussoftware.udsl.model.EnvironmentSpec
import groovy.text.StreamingTemplateEngine
import groovy.util.logging.Slf4j

@Slf4j
class K8SGenerator implements Generator {
	
	static String TEMPLATE_PREFIX = "/templates/k8s/"

	@Override
	public String getName() {
		return "kubernetes"
	}

	@Override
	public String getDescription() {
		return "Gerador de c�digo Terraform para provisionamento de ambiente de microservi�os";
	}

	@Override
	public String getVersion() {
		return "1.0"; // TODO: Usar a informa��o do git-info
	}

	@Override
	public String getAuthor() {
		return "PMS";
	}

	
	@Override
	public int generate(List<EnvironmentSpec> envSpec, ConfigObject config, File outputDir, ResourceLoader loader) {
		
		def generatedFiles = 0
		
		def k8sHelper = new K8SHelper()
		def tfHelper = new TFHelper()
		
		
		envSpec.each { env ->
			
			def baseBindings = [
				env: env,
				config: config,
				k8s: k8sHelper,
				tf: tfHelper
			]


			def ftb = new FileTreeBuilder(outputDir)
			
			ftb.dir(env.name) { 
				
				// entry point do m�dulo
				file("main.tf",true, K8SGenerator.processTemplate(loader, "main.tf.tpl",baseBindings))
							generatedFiles++
				
				// Gera um m�dulo para cada ingress pendurado no environment
				// Estes endpoint s�o os de entradas
				dir("ingress") {
					env.endpoints.each { ep ->
						dir(ep.name) {
							file("main.tf",true, K8SGenerator.processTemplate(loader,"ingress.tf.tpl",baseBindings + [endpoint:ep]))
							generatedFiles++
						}
					}
				}
				
				// Gera um m�dulo para cada enpoint de sa�da
				dir("external") {
					env.externalEndpoints.each { ep ->
						if ( ep.routes.size() == 0 ) {
							dir( ep.name ) {
								file("main.tf",true,
								  K8SGenerator.processTemplate(
									  loader,"external-endpoint.tf.tpl",baseBindings + [endpoint:ep]))
								generatedFiles++
							}
						}						
					}
				}
				
				// M�dulo para configura��o do provedor de mensageria. 
				if ( !env.messageChannels.empty ) {
					dir("messaging") {
						
						// Determina o provedor de mensageria. Por default ser� o RabbitMQ
						def messagingProvider = config?.messaging?.provider?:"rabbitmq"
						log.info("[I100] messagingProvider=${messagingProvider}")
						
						dir(messagingProvider) {
							
							// Arquivo principal
							file("main.tf",true,K8SGenerator.processTemplate(loader,"messaging/${messagingProvider}/main.tf.tpl",baseBindings))
							
							// Vari�veis para customiza��o
							file("variables.tf",true,K8SGenerator.processTemplate(loader,"messaging/${messagingProvider}/variables.tf.tpl",baseBindings))
							
							// Vari�veis de sa�da
							file("output.tf",true,K8SGenerator.processTemplate(loader,"messaging/${messagingProvider}/outputs.tf.tpl",baseBindings))
							
							// Canais
							file("channels.tf",true,K8SGenerator.processTemplate(loader,"messaging/${messagingProvider}/channels.tf.tpl",baseBindings))


							// Customiza��es locais
							file("custom.tf",false,K8SGenerator.processTemplate(loader,"custom.tf.tpl",baseBindings))

							generatedFiles++
						}
						
						// 
					}
				}

				// Para cada deployment crio um deployment em si e um
				// servi�o
				env.deployments.each { deployment ->
					dir(deployment.name) {
												
						file("deployment.tf",true, K8SGenerator.processTemplate(loader,"deployment.tf.tpl",
							baseBindings + [deployment: deployment]))
						
						file("service.tf",true, K8SGenerator.processTemplate(loader,"service.tf.tpl",
							baseBindings + [deployment: deployment]))

					}
				}
				
				// Templates para seguran�a. Ativados apenas quando a propriedade security.enabled == true
				if ( config?.security?.enabled ?: false ) {
					def securityProvider = config.security?.provider ?: 'keycloak'
					
					dir('security') {
						dir(securityProvider) {
							// Arquivo principal
							file("main.tf",true,K8SGenerator.processTemplate(loader,"security/${securityProvider}/main.tf.tpl",baseBindings))
							
							// Vari�veis para customiza��o
							file("variables.tf",true,K8SGenerator.processTemplate(loader,"security/${securityProvider}/variables.tf.tpl",baseBindings))
							
							// Vari�veis de sa�da
							file("output.tf",true,K8SGenerator.processTemplate(loader,"security/${securityProvider}/outputs.tf.tpl",baseBindings))

							// Customiza��es locais
							file("custom.tf",false,K8SGenerator.processTemplate(loader,"custom.tf.tpl",baseBindings))
						}
					}
				}
			}
			
		}
		
		return generatedFiles
	}
	
	
	protected static byte[] processTemplate(ResourceLoader loader, String template, Map bindings) {
		return TemplateHelper.processTemplate(loader, TEMPLATE_PREFIX + template, bindings)				
		log.info("Processando template: ${template}")
	}
	
}
