/**
 * 
 */
package br.com.opussoftware.udsl

import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.syntax.ParserException
import org.codehaus.groovy.syntax.Token

import static org.codehaus.groovy.control.customizers.builder.CompilerCustomizationBuilder.withConfig
import static org.codehaus.groovy.syntax.Types.*

import br.com.opussoftware.udsl.model.EnvironmentSpec
import groovy.util.logging.Slf4j

/**
 * Processa o arquivo contendo DSL descrevendo o ambiente a ser gerado
 * e retorna o mode
 * @author Philippe
 *
 */
@Slf4j
class EnvironmentParser {
	
	EnvironmentSpec parse(Reader reader, EnvironmentSpec initialSpec = null) {
		
		def binding = new Binding();
		def config = new CompilerConfiguration()
		
		withConfig(config) {
			secureAst {
				methodDefinitionAllowed = false
				closuresAllowed = true
				importsWhitelist = [] // empty whitelist means imports are disallowed
				staticImportsWhitelist = [] // same for static imports
			}
		}
		
		def shell = new GroovyShell(binding, config)		
		def script = shell.parse(reader)		
		def environment = initialSpec ?: new EnvironmentSpec()
		
		script.metaClass.getEnvironment = { -> environment }
		script.metaClass.methodMissing = environmentMethodMissing		
		script.run()
		
		return environment
	}
	
	
	def getEnvironmentMethodMissing() {
		
		return { name, args ->
			
			if ( name.toLowerCase() == "environment" ) {
				processEnvironmentRoot( environment, args)
			}	
			else {
				throw new ParserException("Elemento raiz deve ser um 'Environment': name=${name}, args.size=${args}", Token.NULL)
			}		
		}
	}
	
	def processEnvironmentRoot(environment, args) {
		
		if ( args.size() != 2 ) {
			throw new ParserException("sintaxe: Environment 'nome' { definição } ", Token.NULL)			
		}
		
		if ( !args[0] instanceof String ) {
			throw new ParserException("Nome do ambiente deve ser um string ", Token.NULL)			
		}

		if ( !args[1] instanceof Closure ) {
			throw new ParserException("Definição do ambiente inválida. Deve ser um bloco de código declarando seus elementos", Token.NULL)			
		}		
		
		def delegate = environment
		delegate.name = args[0]
		args[1].delegate = delegate
		args[1].resolveStrategy = Closure.DELEGATE_FIRST
		
		args[1].call()
		
		return delegate
		
	}
	
	
	
}
