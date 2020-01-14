import br.com.opussoftware.tools.udsl.generator.k8s.K8SHelper
import br.com.opussoftware.tools.udsl.generator.k8s.TFHelper

/*
 * K8S Generator definitions
 */

// Global Bindings
globalBindings(
	k8s: K8SHelper,
	tf: TFHelper	
)
 
args.envSpec.each { env ->
	
	dir(env.name) {
		
		// entry point do módulo
		file(name: "main.tf",template: "main.tf.tpl", overwrite:true, bindings:[env:env]); 
		
		// Gera um módulo para cada ingress pendurado no environment
		// Estes endpoint são os de entradas
		dir("ingress") {
			env.endpoints.each { ep ->
				dir(ep.name) {
					file("main.tf",true, K8SGeneratorV1.processTemplate(loader,"ingress.tf.tpl",baseBindings + [endpoint:ep]))
					generatedFiles++
				}
			}
		}
		
		// Gera um módulo para cada enpoint de saída
		dir("external") {
			env.externalEndpoints.each { ep ->
				if ( ep.routes.size() == 0 ) {
					dir( ep.name ) {
						file("main.tf",true,
						  K8SGeneratorV1.processTemplate(
							  loader,"external-endpoint.tf.tpl",baseBindings + [endpoint:ep]))
						generatedFiles++
					}
				}
			}
		}
		
		// Módulo para configuração do provedor de mensageria.
		if ( !env.messageChannels.empty ) {
			dir("messaging") {
				
				// Determina o provedor de mensageria. Por default será o RabbitMQ
				def messagingProvider = config?.messaging?.provider?:"rabbitmq"
				log.info("[I100] messagingProvider=${messagingProvider}")
				
				dir(messagingProvider) {
					
					// Arquivo principal
					file("main.tf",true,K8SGeneratorV1.processTemplate(loader,"messaging/${messagingProvider}/main.tf.tpl",baseBindings))
					
					// Variáveis para customização
					file("variables.tf",true,K8SGeneratorV1.processTemplate(loader,"messaging/${messagingProvider}/variables.tf.tpl",baseBindings))
					
					// Variáveis de saída
					file("output.tf",true,K8SGeneratorV1.processTemplate(loader,"messaging/${messagingProvider}/outputs.tf.tpl",baseBindings))
					
					// Canais
					file("channels.tf",true,K8SGeneratorV1.processTemplate(loader,"messaging/${messagingProvider}/channels.tf.tpl",baseBindings))


					// Customizações locais
					file("custom.tf",false,K8SGeneratorV1.processTemplate(loader,"custom.tf.tpl",baseBindings))

					generatedFiles++
				}
				
				//
			}
		}

		// Para cada deployment crio um deployment em si e um
		// serviço
		env.deployments.each { deployment ->
			dir(deployment.name) {
										
				file("deployment.tf",true, K8SGeneratorV1.processTemplate(loader,"deployment.tf.tpl",
					baseBindings + [deployment: deployment]))
				
				file("service.tf",true, K8SGeneratorV1.processTemplate(loader,"service.tf.tpl",
					baseBindings + [deployment: deployment]))

			}
		}
		
		// Templates para segurança. Ativados apenas quando a propriedade security.enabled == true
		if ( config?.security?.enabled ?: false ) {
			def securityProvider = config.security?.provider ?: 'keycloak'
			
			dir('security') {
				dir(securityProvider) {
					// Arquivo principal
					file("main.tf",true,K8SGeneratorV1.processTemplate(loader,"security/${securityProvider}/main.tf.tpl",baseBindings))
					
					// Variáveis para customização
					file("variables.tf",true,K8SGeneratorV1.processTemplate(loader,"security/${securityProvider}/variables.tf.tpl",baseBindings))
					
					// Variáveis de saída
					file("output.tf",true,K8SGeneratorV1.processTemplate(loader,"security/${securityProvider}/outputs.tf.tpl",baseBindings))

					// Customizações locais
					file("custom.tf",false,K8SGeneratorV1.processTemplate(loader,"custom.tf.tpl",baseBindings))
				}
			}
		}
	}
	
}
}