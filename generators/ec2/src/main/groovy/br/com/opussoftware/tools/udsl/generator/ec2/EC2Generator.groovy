/**
 * 
 */
package br.com.opussoftware.tools.udsl.generator.ec2

import org.codehaus.groovy.control.CompilerConfiguration

import br.com.opussoftware.tools.udsl.generator.Generator
import br.com.opussoftware.tools.udsl.generator.ResourceLoader
import br.com.opussoftware.tools.udsl.generator.util.FileTreeBuilder
import br.com.opussoftware.udsl.model.EnvironmentSpec
import groovy.util.logging.Slf4j

/**
 * @author Philippe
 *
 */
@Slf4j
class EC2Generator implements Generator{
	
	static String TEMPLATE_PREFIX = "/templates/ec2/"
	

	@Override
	public String getName() {
		return "ec2";
	}

	@Override
	public String getDescription() {
		return "Gerador de código Terraform para provisionamento de ambiente de microserviços em ambiente EC2";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "PMS";
	}

	@Override
	public int generate(List<EnvironmentSpec> envSpec, ConfigObject generatorConfig, File outputDir, ResourceLoader resourceLoader) {
		
		
		// Configurações do compilador
		def binding = new Binding();
		def config = new CompilerConfiguration()
		
		// Veja a documentação da classe DelegatingScript para entender o motivo
		// da linha abaixo.
		config.scriptBaseClass = DelegatingScript.class.getName() 
		
		
		Reader reader = resourceLoader.getResourceAsStream(TEMPLATE_PREFIX + "Generator.groovy").newReader()
		
		// Prepara o delegate
		FileTreeBuilder ftb = new FileTreeBuilder(outputDir)
		def args = [
			envSpec: envSpec,
			config: generatorConfig,
			outputDir: outputDir,
			resourceLoader: resourceLoader
		]
		GeneratorDelegate spec = new GeneratorDelegate(fileTreeBuilder: ftb, args: args, resourceLoader: resourceLoader, prefix: TEMPLATE_PREFIX)
		
		
		def shell = new GroovyShell(this.class.getClassLoader(),binding, config)
		
		def script = shell.parse(reader)
		script.setDelegate(spec)
		script.run();
		
		return 0;
	}
}
