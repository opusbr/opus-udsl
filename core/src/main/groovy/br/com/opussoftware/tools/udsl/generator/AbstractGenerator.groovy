package br.com.opussoftware.tools.udsl.generator

import org.codehaus.groovy.control.CompilerConfiguration

import br.com.opussoftware.udsl.model.EnvironmentSpec
import br.com.opussoftware.tools.udsl.generator.util.FileTreeBuilder

abstract class AbstractGenerator implements Generator {
	
	/**
	 * @return Resource prefix to use with this generator. By convention, this prefix should take
	 *         the following form: "/templates/&lt;module name&gt;", where &lt;module name&gt; is the same value
	 *         returned by the getName() method. 
	 */
	protected String getResourcePrefix() {
		return "/templates/${this.getName()}/";
	}
	

	@Override
	public int generate(List<EnvironmentSpec> environments, ConfigObject generatorConfig, File outputDir, ResourceLoader resourceLoader) {
		
		
		// Configurações do compilador
		def binding = new Binding();
		def config = new CompilerConfiguration()
		
		// Veja a documentação da classe DelegatingScript para entender o motivo
		// da linha abaixo.
		config.scriptBaseClass = DelegatingScript.class.getName()
		
		def prefix = getResourcePrefix()
		def resourceName = prefix + "Generator.groovy"
		
		Reader reader = resourceLoader.getResourceAsStream(resourceName).newReader()
		
		// Prepara o delegate
		FileTreeBuilder ftb = new FileTreeBuilder(outputDir)
		def args = [
			envSpec: environments,
			environments: environments,
			config: generatorConfig,
			outputDir: outputDir,
			resourceLoader: resourceLoader
		]
		
		GeneratorDelegate delegate = new GeneratorDelegate(fileTreeBuilder: ftb, args: args, resourceLoader: resourceLoader, prefix: prefix)
		
		
		def shell = new GroovyShell(this.class.getClassLoader(),binding, config)
		
		def script = (DelegatingScript)shell.parse(reader, resourceName)
		script.setDelegate(delegate)
		script.run();
		
		return 0;
	}

}
