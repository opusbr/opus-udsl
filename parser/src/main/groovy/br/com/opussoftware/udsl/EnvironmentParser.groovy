/**
 * 
 */
package br.com.opussoftware.udsl

import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer
import org.codehaus.groovy.syntax.ParserException
import org.codehaus.groovy.syntax.Token

import static org.codehaus.groovy.control.customizers.builder.CompilerCustomizationBuilder.withConfig
import static org.codehaus.groovy.syntax.Types.*

import br.com.opussoftware.udsl.model.EnvironmentSpec
import br.com.opussoftware.udsl.model.UDslScriptDelegate
import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j

/**
 * Processa o arquivo contendo DSL descrevendo o ambiente a ser gerado
 * e retorna o mode
 * @author Philippe
 *
 */
@Slf4j
class EnvironmentParser {

	List<EnvironmentSpec> parse(Reader reader, String filename, Map variables = null) {
		return parse(reader, null, filename,variables)
	}
	
	List<EnvironmentSpec> parse(Reader reader, EnvironmentSpec initialSpec,Map variables = null) {
		return parse(reader, initialSpec, null,variables)
	}
	
	List<EnvironmentSpec> parse(Reader reader,Map variables = null) {
		return parse(reader, null, null,variables)
	}

	
	protected List<EnvironmentSpec> parse(Reader reader, EnvironmentSpec initialSpec, String filename, Map variables) {
		
		def binding = new Binding(variables?:[:])
		def config = new CompilerConfiguration()
		config.setScriptBaseClass(DelegatingScript.class.getName())
		
		// ref: http://docs.groovy-lang.org/docs/latest/html/documentation/type-checking-extensions.html
		//config.addCompilationCustomizers(
		//	new ASTTransformationCustomizer(TypeChecked, extensions:[UDslScriptExtention.class.getName()]))
		
		withConfig(config) {
			secureAst {
				methodDefinitionAllowed = false
				closuresAllowed = true
				importsWhitelist = [] // empty whitelist means imports are disallowed
				staticImportsWhitelist = [] // same for static imports
			}
		}
		
		def shell = new GroovyShell(binding, config)
		def script = (DelegatingScript)shell.parse(reader, filename?: "Environment.udsl")
		def delegate = new UDslScriptDelegate(initialSpec)
		script.setDelegate(delegate)
		script.run()
		
		return delegate.environments
	}
	
}
