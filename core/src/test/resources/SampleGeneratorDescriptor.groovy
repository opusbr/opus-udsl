import groovy.json.StringEscapeUtils

/*
 * Exemplo de definição de gerador usando DSL
 * 
 */

// Global Bindings
// Objetos que serão injetados na execução de um script sem que seja necessário 
// declará-los
globalBindings [
	sutil: StringEscapeUtils	
]

// Named Bindings. 
// Define um mapa de objetos que estarão disponíveis
// para uso em um template, devendo ser explicitamente indicados
// no template
bindings "standard" [
	stringUtil: StringEscapeUtils	
]

// "args" possui os argumentos passados para o gerador:
//  envSpec: Lista de environments
//  outputDir: Diretório de saída especificado
//  config: Objeto com a parametrização a ser utilizada na geração
args.envSpec.each { env ->

	// Cria, caso necessário um diretório
	dir( "${args.outputDir}/${env.name}" ) {
				
		file(name:"teste.tf",template:"t1.tpl",overwrite: true, bindings: standard)
	}
}
