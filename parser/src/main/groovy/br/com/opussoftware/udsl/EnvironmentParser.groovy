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

	EnvironmentSpec parse(Reader reader, String filename, Map variables = null) {
		return parse(reader, null, filename,variables)
	}
	
	EnvironmentSpec parse(Reader reader, EnvironmentSpec initialSpec,Map variables = null) {
		return parse(reader, initialSpec, null,variables)
	}
	
	EnvironmentSpec parse(Reader reader,Map variables = null) {
		return parse(reader, null, null,variables)
	}

	
	protected EnvironmentSpec parse(Reader reader, EnvironmentSpec initialSpec, String filename, Map variables) {
		
		def binding = new Binding(variables?:[:])
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
		def script = shell.parse(reader, filename?: "Environment.udsl")		
		def environment = initialSpec ?: new EnvironmentSpec()
		
		script.metaClass.getEnvironment = { -> environment }
		script.metaClass.methodMissing = environmentMethodMissing
		script.run()
		
		return environment
	}
	
	
	def getEnvironmentMethodMissing() {
		
		return { name, args ->

			println "processEnvironmentRoot: name=${name}"
			
			if ( name.toLowerCase() == "environment" ) {
				processEnvironmentRoot( environment, args)
			}	
			else {
				throw new ParserException("[E75] Unsupported element ': name=${name}, args=${args}", Token.NULL)
			}		
		}
	}
	
	def processEnvironmentRoot(environment, args) {
		
		
		if ( args.size() < 2 || args.size() > 3 ) {
			throw new ParserException("Syntax: Environment(name:'name', tags:['tag1','tag2'])  { specification } ", Token.NULL)			
		}
		
		def delegate = environment
		
		
		if ( args[0] instanceof Map) {
			def name = args[0].name
			if (!name) {
				throw new ParserException("[E93] Environment requires a 'name'", Token.NULL);
			}
			delegate.name = 
		}
		
		if ( !args[0] instanceof String ) {
			throw new ParserException("Environment name must be a string ", Token.NULL)			
		}

		if ( !args[1] instanceof Closure ) {
			throw new ParserException("Definição do ambiente inválida. Deve ser um bloco de código declarando seus elementos", Token.NULL)			
		}		
		
		delegate.name = args[0]
		args[1].delegate = delegate
		args[1].resolveStrategy = Closure.DELEGATE_FIRST
		
		args[1].call()
		
		return delegate
		
	}
	
	
	
}
