import groovy.json.StringEscapeUtils
import br.com.opussoftware.tools.udsl.generator.ec2.TFHelper

/*
 * Exemplo de definição de gerador usando DSL
 * 
 */

// Global Bindings
// Objetos que serão injetados na execução de um script sem que seja necessário 
// declará-los
globalBindings (
	sutil: StringEscapeUtils,
	tf: TFHelper
)

// Named Bindings. 
// Define um mapa de objetos que estarão disponíveis
// para uso em um template, devendo ser explicitamente indicados
// no template
bindings ("standard", [
	stringUtil: StringEscapeUtils	
])

// "args" possui os argumentos passados para o gerador:
//  envSpec: Lista de environments
//  outputDir: Diretório de saída especificado
//  config: Objeto com a parametrização a ser utilizada na geração

args.envSpec.each { env ->

	// Cria diretório de saída. Diretórios são sempre criados relativos ao outputDir especificado
	dir( "${env.name}-ec2" ) {
				
		file(name:"main.tf",template:"main.tf.tpl",overwrite: true, 
			bindings: [
				env: env,
				config: config, 
				stringUtil: StringEscapeUtils])
	}
}

