import groovy.json.StringEscapeUtils
import br.com.opussoftware.tools.udsl.generator.ec2.TFHelper

/*
 * Exemplo de defini��o de gerador usando DSL
 * 
 */

// Global Bindings
// Objetos que ser�o injetados na execu��o de um script sem que seja necess�rio 
// declar�-los
globalBindings (
	sutil: StringEscapeUtils,
	tf: TFHelper
)

// Named Bindings. 
// Define um mapa de objetos que estar�o dispon�veis
// para uso em um template, devendo ser explicitamente indicados
// no template
bindings ("standard", [
	stringUtil: StringEscapeUtils	
])

// "args" possui os argumentos passados para o gerador:
//  envSpec: Lista de environments
//  outputDir: Diret�rio de sa�da especificado
//  config: Objeto com a parametriza��o a ser utilizada na gera��o

args.envSpec.each { env ->

	// Cria diret�rio de sa�da. Diret�rios s�o sempre criados relativos ao outputDir especificado
	dir( "${env.name}-ec2" ) {
				
		file(name:"main.tf",template:"main.tf.tpl",overwrite: true, 
			bindings: [
				env: env,
				config: config, 
				stringUtil: StringEscapeUtils])
	}
}

