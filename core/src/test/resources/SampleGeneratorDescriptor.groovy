import groovy.json.StringEscapeUtils

/*
 * Exemplo de defini��o de gerador usando DSL
 * 
 */

// Global Bindings
// Objetos que ser�o injetados na execu��o de um script sem que seja necess�rio 
// declar�-los
globalBindings [
	sutil: StringEscapeUtils	
]

// Named Bindings. 
// Define um mapa de objetos que estar�o dispon�veis
// para uso em um template, devendo ser explicitamente indicados
// no template
bindings "standard" [
	stringUtil: StringEscapeUtils	
]

// "args" possui os argumentos passados para o gerador:
//  envSpec: Lista de environments
//  outputDir: Diret�rio de sa�da especificado
//  config: Objeto com a parametriza��o a ser utilizada na gera��o
args.envSpec.each { env ->

	// Cria, caso necess�rio um diret�rio
	dir( "${args.outputDir}/${env.name}" ) {
				
		file(name:"teste.tf",template:"t1.tpl",overwrite: true, bindings: standard)
	}
}
