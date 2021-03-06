import io.github.opusbr.tools.udsl.generator.k8s.K8SHelper
import io.github.opusbr.tools.udsl.generator.k8s.TFHelper

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

		// entry point do m�dulo
		file(name: "main.gen.tf",template: "main.tf.tpl", overwrite:true, bindings:[env:env]);
		
		// providers
		file(name: "provider.tf",template: "provider.tf.tpl", overwrite:false, bindings:[env:env]);

		// tfvars
		file(name: "terraform.tfvars",template: "terraform.tfvars.tpl", overwrite:false, bindings:[env:env]);

		// Gera um m�dulo para cada ingress pendurado no environment
		// Estes endpoint s�o os de entradas
		dir("ingress") {
			env.endpoints.each { ep ->
				
				println "Ingress: ep=${ep?.name}"
				
				dir(ep.name) {
					file(name:"main.gen.tf",template:"ingress.tf.tpl",overwrite:true,
					bindings:[
						env:env,
						endpoint:ep])
					file(name:"custom.tf",template:"custom.tf.tpl",overwrite:false,
					bindings:[
						env:env,
						endpoint:ep])
				}
			}
		}

		// Gera um m�dulo para cada enpoint de sa�da
		dir("external") {
			env.externalEndpoints.each { ep ->
				if ( ep.routes.size() == 0 ) {
					dir( ep.name ) {
						file(name:"main.gen.tf",overwrite:true,
						template: "external-endpoint.tf.tpl",
						bindings: [
							env:env,
							endpoint:ep])
						
						file(name:"custom.tf",template:"custom.tf.tpl",overwrite:false,
							bindings:[
								env:env,
								endpoint:ep])		
					}
				}
			}
		}

		// M�dulo para configura��o do provedor de mensageria.
		if ( !env.messageChannels.empty ) {
			dir("messaging") {

				// Determina o provedor de mensageria. Por default ser� o RabbitMQ
				def messagingProvider = config?.messaging?.provider?:"rabbitmq"

				dir(messagingProvider) {

					// Arquivo principal
					file(name:"main.gen.tf",overwrite:true,template:"messaging/${messagingProvider}/main.tf.tpl",bindings:[])

					// Vari�veis para customiza��o
					file(name:"variables.gen.tf",overwrite:true,template:"messaging/${messagingProvider}/variables.tf.tpl",bindings:[])

					// Vari�veis de sa�da
					file(name:"output.gen.tf",overwrite:true,template:"messaging/${messagingProvider}/outputs.tf.tpl",bindings:[])

					// Canais
					file(name:"channels.gen.tf",overwrite:true,template:"messaging/${messagingProvider}/channels.tf.tpl",bindings:[env:env])

					// Customiza��es locais
					file(name:"custom.gen.tf",overwrite:false,template:"custom.tf.tpl",bindings:[])
				}
			}
		}

		// Para cada deployment crio um deployment em si e um
		// servi�o
		env.deployments.each { deployment ->
			dir(deployment.name) {
				def deploymentBindings = [
					env: env,
					deployment:deployment
				]

				file(name:"deployment.gen.tf",overwrite:true,template:"deployment.tf.tpl",bindings:deploymentBindings)
				file(name:"service.gen.tf",overwrite:true,template:"service.tf.tpl",bindings:deploymentBindings)
				
				file(name:"custom.tf",template:"custom.tf.tpl",overwrite:false,
					bindings:deploymentBindings)

			}
		}

		// Templates para seguran�a. Ativados apenas quando a propriedade security.enabled == true
		if ( config?.security?.enabled ?: false ) {
			def securityProvider = config.security?.provider ?: 'keycloak'

			dir('security') {
				dir(securityProvider) {
					// Arquivo principal
					file(name:"main.gen.tf",overwrite:true,template:"security/${securityProvider}/main.tf.tpl",bindings:[])

					// Vari�veis para customiza��o
					file(name:"variables.gen.tf",overwrite:true,template:"security/${securityProvider}/variables.tf.tpl",bindings:[env:env])

					// Vari�veis de sa�da
					file(name:"output.gen.tf",overwrite:true,template:"security/${securityProvider}/outputs.tf.tpl",bindings:[])

					// Customiza��es locais
					file(name:"custom.tf",overwrite:false,template:"custom.tf.tpl",bindings:[])
				}
			}
		}
	}
}
