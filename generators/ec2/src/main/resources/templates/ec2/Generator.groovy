import groovy.json.StringEscapeUtils
import io.github.opusbr.tools.udsl.generator.ec2.TFHelper

/*
 * Exemplo de definição de gerador usando DSL
 * 
 */

// Helpers específicos do EC2

def DEFAULT_AMI = [
	owner : "099720109477",
	name : "ubuntu/images/hvm-ssd/*bionic*-amd64-server*"	
]

def ec2Helpers = [
	// Retorna nome da imagem AMI a ser utilizada para um deployment
	amiName: { config, name -> config.amiMap[name]?:config.defaultAmi?:DEFAULT_AMI },
	
	// Retorna lista de "target groups" associados a um deployment
	targetGroupsForDeployment : { env, deployment -> 		
		def targets = new HashSet();		
		env.endpoints.each { ep ->  			
			ep.routes.each { route ->				
				if ( route.deployment == deployment.name ) {
					def epSuffix = TFHelper.moduleName(ep.name)
					def routeName = TFHelper.moduleName(route.deployment)					
					targets.add( "tg_${routeName}")					
				}				
			}			
		}
		
		return targets
	},
	
	targetGroupsForEnvironment: { env ->
	  def targets = new HashSet()
	  env.endpoints.each { ep ->
		  ep.routes.each { route ->
		     def routeName = TFHelper.moduleName(route.deployment)
		     targets.add( "tg_${routeName}")
		  }
	  }	  
	  return targets
	},
	
	moduleName: { str -> TFHelper.moduleName(str) },
	
	awsName: { name ->
		return name.collect({
			if ( it =~ /[0-9A-Za-z]/ || it == '-') {
				return it
			}
			else {
				return '-'
			}
		}).join()

		
	}
]


// Global Bindings
// Objetos que serão injetados na execução de um script sem que seja necessário 
// declará-los
globalBindings (
	sutil: StringEscapeUtils,
	tf: TFHelper,
	config: args.config,
	ec2: ec2Helpers
)

// Named Bindings. 
// Define um mapa de objetos que estarão disponíveis
// para uso em um template, devendo ser explicitamente indicados
// no template
bindings ("standard", [
	stringUtil: StringEscapeUtils,
])


def checkMessagingProvider = { p -> 
	if ( ["rabbitmq"].contains(p)) {
		return true
	}
	else {
		throw new IllegalArgumentException("Messaging provider not supported: ${p}")
	}
}


// "args" possui os argumentos passados para o gerador:
//  envSpec: Lista de environments
//  outputDir: Diretório de saída especificado
//  config: Objeto com a parametrização a ser utilizada na geração

args.envSpec.each { env ->

	// Cria diretório de saída. Diretórios são sempre criados relativos ao outputDir especificado
	dir( "${env.name}-ec2" ) {

		file(name:"provider.tf",template:"provider.tf.tpl",overwrite: false,
			bindings: [
				env: env
			])

		file(name:"terraform.tfvars",template:"terraform.tfvars.tpl",overwrite: false,
			bindings: [
				env: env
			])

		file(name:"main.gen.tf",template:"main.tf.tpl",overwrite: true, 
			bindings: [			
				env: env 
			])
		
		// Configurações da VPC que irá abrigar a aplicação
		dir("network") {
			["main":true,"outputs":true,"variables":true,"custom":false].each { tpl,overwrite ->
				file(name: "${tpl}${overwrite?'.gen.tf':'.tf'}", template:"network/${tpl}.tf.tpl", overwrite: overwrite,bindings: [
					env: env,
					endpoints: env.endpoints
				])
	
			}

		}
		
		//  No caso do EC2 uso uma única instância para todos os endpoints
		dir("ingress") {
			
			["main":true,"outputs":true,"variables":true,"custom":false].each { tpl,overwrite ->
				file(name: "${tpl}${overwrite?'.gen.tf':'.tf'}", template:"ingress/${tpl}.tf.tpl", overwrite: overwrite,bindings: [
					env: env,
					endpoints: env.endpoints
				])
	
			}
		}
				
		
		
		// Cria um diretório para cada deployment
		env.deployments.each { deployment ->

			dir( deployment.name ) {
				
				["main":true,"outputs":true,"variables":true,"custom":false].each { tpl, overwrite ->
					
					file(name: "${tpl}${overwrite?'.gen.tf':'.tf'}", template:"deployment/${tpl}.tf.tpl", overwrite: overwrite,bindings: [
						env: env,
						deployment: deployment
					])	
				}			
			}			
		}
		
		// Gera instalação do RabbitMQ se houver ao menos um messageChannel
		if ( env.messageChannels.size() > 0 ) {
			
			dir("messaging") {
				
				checkMessagingProvider(args.config?.messaging?.provider)
				
				dir( args.config.messaging.provider ) {
					
					["main":true,"outputs":true,"variables":true,"custom":false, "channels": true].each { tpl, overwrite ->
						file(name: "${tpl}${overwrite?'.gen.tf':'.tf'}", template:"messaging/${args.config.messaging.provider}/${tpl}.tf.tpl", overwrite: overwrite,bindings: [
							env: env
						])
					}
				}				
			}			
		} 
		
		// TBD: Security
	}
}

