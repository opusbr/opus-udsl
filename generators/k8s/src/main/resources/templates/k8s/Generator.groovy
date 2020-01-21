import br.com.opussoftware.tools.udsl.generator.k8s.K8SHelper
import br.com.opussoftware.tools.udsl.generator.k8s.TFHelper

/*
 * K8S Generator definitions
 */

// Global Bindings
globalBindings(
	k8s: K8SHelper,
	tf: TFHelper,
	config: args.config	)

args.envSpec.each { env ->

	dir(env.name) {

		// entry point do módulo
		file(name: "main.tf",template: "main.tf.tpl", overwrite:true, bindings:[env:env]);

		// Gera um módulo para cada ingress pendurado no environment
		// Estes endpoint são os de entradas
		dir("ingress") {
			env.endpoints.each { ep ->
				dir(ep.name) {
					file(name:"main.tf",template:"ingress.tf.tpl",overwrite:true,
					bindings:[
						env:env,
						endpoint:ep])
				}
			}
		}

		// Gera um módulo para cada enpoint de saída
		dir("external") {
			env.externalEndpoints.each { ep ->
				if ( ep.routes.size() == 0 ) {
					dir( ep.name ) {
						file(name:"main.tf",overwrite:true,
						template: "external-endpoint.tf.tpl",
						bindings: [
							env:env,
							endpoint:ep])
					}
				}
			}
		}

		// Módulo para configuração do provedor de mensageria.
		if ( !env.messageChannels.empty ) {
			dir("messaging") {

				// Determina o provedor de mensageria. Por default será o RabbitMQ
				def messagingProvider = config?.messaging?.provider?:"rabbitmq"

				dir(messagingProvider) {

					// Arquivo principal
					file(name:"main.tf",overwrite:true,template:"messaging/${messagingProvider}/main.tf.tpl",bindings:[])

					// Variáveis para customização
					file(name:"variables.tf",overwrite:true,template:"messaging/${messagingProvider}/variables.tf.tpl",bindings:[])

					// Variáveis de saída
					file(name:"output.tf",overwrite:true,template:"messaging/${messagingProvider}/outputs.tf.tpl",bindings:[])

					// Canais
					file(name:"channels.tf",overwrite:true,template:"messaging/${messagingProvider}/channels.tf.tpl",bindings:[env:env])

					// Customizações locais
					file(name:"custom.tf",overwrite:false,template:"custom.tf.tpl",bindings:[])
				}
			}
		}

		// Para cada deployment crio um deployment em si e um
		// serviço
		env.deployments.each { deployment ->
			dir(deployment.name) {
				def deploymentBindings = [
					env: env,
					deployment:deployment
				]

				file(name:"deployment.tf",overwrite:true,template:"deployment.tf.tpl",bindings:deploymentBindings)
				file(name:"service.tf",overwrite:true,template:"service.tf.tpl",bindings:deploymentBindings)
			}
		}

		// Templates para segurança. Ativados apenas quando a propriedade security.enabled == true
		if ( config?.security?.enabled ?: false ) {
			def securityProvider = config.security?.provider ?: 'keycloak'

			dir('security') {
				dir(securityProvider) {
					// Arquivo principal
					file(name:"main.tf",overwrite:true,template:"security/${securityProvider}/main.tf.tpl",bindings:[])

					// Variáveis para customização
					file(name:"variables.tf",overwrite:true,template:"security/${securityProvider}/variables.tf.tpl",bindings:[env:env])

					// Variáveis de saída
					file(name:"output.tf",overwrite:true,template:"security/${securityProvider}/outputs.tf.tpl",bindings:[])

					// Customizações locais
					file(name:"custom.tf",overwrite:false,template:"custom.tf.tpl",bindings:[])
				}
			}
		}
	}
}
