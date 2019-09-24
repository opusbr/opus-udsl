package br.com.opussoftware.tools.udsl.generator.k8s

import br.com.opussoftware.tools.udsl.generator.Generator
import br.com.opussoftware.tools.udsl.generator.ResourceLoader
import br.com.opussoftware.tools.udsl.generator.util.FileTreeBuilder
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

			def ftb = new FileTreeBuilder(outputDir)
			
			ftb.dir(env.name) { 
				
				// entry point do m�dulo
				file("main.tf",true, K8SGenerator.processTemplate(loader, "main.tf.tpl",[
								env: env,
								config: config,
								k8s: k8sHelper,
								tf: tfHelper
								]))
							generatedFiles++
				
				// Gera um m�dulo para cada ingress pendurado no environment
				// Estes endpoint s�o os de entradas
				dir("ingress") {
					env.endpoints.each { ep ->
						dir(ep.name) {
							file("main.tf",true, K8SGenerator.processTemplate(loader,"ingress.tf.tpl",[
								env: env,
								endpoint: ep,
								config: config,
								k8s: k8sHelper
								]))
							generatedFiles++
						}
					}
				}
				
				// Gera um m�dulo para cada enpoint de sa�da
				dir("external") {
					env.externalEndpoints.each { ep ->
						if ( ep.routes.size() == 0 ) {
							dir( ep.name ) {
								file("main.tf",true,K8SGenerator.processTemplate(loader,"external-endpoint.tf.tpl",[
								env: env,
								endpoint: ep,
								config: config,
								k8s: k8sHelper
								]))
							generatedFiles++
							}
						}						
					}
				}

				// Para cada deployment crio um deployment em si e um
				// servi�o
				env.deployments.each { deployment ->
					dir(deployment.name) {
												
						file("deployment.tf",true, K8SGenerator.processTemplate(loader,"deployment.tf.tpl",[							
							env: env,
							config: config,
							k8s: k8sHelper,
							deployment:deployment]))
						
						file("service.tf",true, K8SGenerator.processTemplate(loader,"service.tf.tpl",[
							env: envSpec,
							config: config,
							k8s: k8sHelper,
							deployment:deployment]))
					}
				}
			}
			
		}
		
		return generatedFiles
	}
	
	
	protected static byte[] processTemplate(ResourceLoader loader, String template, Map bindings) {
		
		log.info("Processando template: ${template}")
		
		Reader r= locateTemplate(loader, template);
		try {
			def tpl = new StreamingTemplateEngine().createTemplate(r);
			
			StringWriter sw = new StringWriter()
			tpl.make(bindings).writeTo(sw)
			return sw.toString().getBytes("UTF-8");
		}
		finally {
			r.close();
		}
		
	}
	
	protected static Reader locateTemplate(ResourceLoader loader, String tpl) {
				
		InputStream is = loader.getResourceAsStream(TEMPLATE_PREFIX + tpl)
		if ( is == null ) {
			throw new IllegalArgumentException("Template n�o encontrado: ${tpl}")
		}
		return is.newReader()
		
	}
}
