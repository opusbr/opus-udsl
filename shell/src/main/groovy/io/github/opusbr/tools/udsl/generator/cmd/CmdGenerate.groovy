package io.github.opusbr.tools.udsl.generator.cmd

import org.apache.commons.text.WordUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption

import io.github.opusbr.tools.udsl.generator.GeneratorRegistry
import io.github.opusbr.tools.udsl.generator.ResourceLoader
import io.github.opusbr.udsl.EnvironmentParser
import io.github.opusbr.udsl.model.AbstractSpec
import io.github.opusbr.udsl.model.SpecIssue
import groovy.util.logging.Slf4j

import io.github.opusbr.tools.udsl.generator.GeneratorConfig

/**
 * Entrypoint principal para geração de código a partir de um modelo de solução descrito
 * a partir de um conjunto de arquivos uDSL.
 * @author Philippe
 *
 */
@ShellComponent
@Slf4j
class CmdGenerate {
	
	@Autowired
	private GeneratorRegistry generatorRegistry;
	
	@Autowired
	private EnvironmentParser environmentParser
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	
	@Autowired
	private GeneratorConfig generatorConfig
	
	
	@ShellMethod(value="Gera artefatos para o ambiente alvo a partir dos arquivos uDSL especificados")
	public String generate(
		@ShellOption(
			value= ["-i","--input"],
			help="Arquivo ou diretório de entrada. Caso seja um diretório, todos os arquivos com extensão .udsl serão processados")
		File inputFileOrDir,
		@ShellOption(
			value= ["-s","--spec"],
			help="Arquivo ou diretório contendo parâmetros para geração do código")
		File[] specFileOrDir,
		@ShellOption(
			value= ["-o","--outputDir"],
			help="Diretório onde os artefatos serão criados. O diretório será criado caso não exista")
		File outputDir,
		
		@ShellOption(
			value = ["-e", "--env"],
			help = "Environment a ser utilizado para a parametrização. Se não fornecido, será utilizado o Environment padrão do arquivo de configuração",
			defaultValue = "" )
		String environment 	,
		@ShellOption(
			value= ["-t","--templatesDir"],
			defaultValue = "",
			help="Diretório contendo templates customizados para a geração")
		File customTemplatesDir) {
		
		
		// Obtem lista de modelos
		def modelFiles = buildModelFileList(inputFileOrDir)
		if ( modelFiles.size() == 0 ) {
			throw new IllegalArgumentException("No model found. Check if you've entered the correct path.")
		}
		
		
		// Processa modelos informados
		def environments = []
		List<SpecIssue> modelIssues = []

		modelFiles.each {
			log.info "Processing model: ${it.name}"
			def r = it.newReader()
			def envSpecs = environmentParser.parse(r)
			envSpecs.each { envSpec ->
				envSpec.traverse { AbstractSpec self ->
					modelIssues += self.issues
				}
			}
			environments += envSpecs
		}
		
		// Don't bother to move to the generation phase
		// if we don't have a valid model
		if ( modelIssues.size() > 0 ) {
			reportIssues(modelIssues)
			return
		}

		log.info "${environments.size()} environment(s) found. Processing configuration..."
		
		// Processa arquivos de configuração passados
		def configMap = createConfig(environment,specFileOrDir)
		
		
		if( customTemplatesDir.toString()  != ""  && customTemplatesDir.isDirectory()) {
			log.info "Custom templates directory: ${customTemplatesDir}"
			this.generatorConfig.customTemplatesDir = customTemplatesDir
		}
		
		log.info "Output directory: ${outputDir}"
		// Roda cada gerador em sequencia
		configMap.each { generator, config -> 
			// Valida o gerador passado
			def gen = generatorRegistry.findByName(generator)			
			log.info "Using generator: ${gen.name}, versão ${gen.version}"									
			log.info "Generating artifacts..."
			gen.generate(environments, config, outputDir, resourceLoader)
		}
		
		log.info "Project artifacts succesfully generated."
		
	}
	
	protected void reportIssues(List<SpecIssue> modelIssues) {
		
		println "================================================================="
		println " S T O P"
		println "================================================================="
		println "Unable to generate project artifacts due to the following issues:"
		modelIssues.each { issue ->
			println "${issue.level}: ${issue.spec.class.simpleName}(${issue.spec.name}) - ${issue.issue}"
		}
		
	}
	
	/**
	 * Processa lista de arquivos de configuração e/ou diretórios passados
	 * @param environment
	 * @param configFilesOrDirs
	 * @return mapa onde a chave é o nome do gerador e o valor um ConfigObject com as configurações pertinentes
	 */
	protected ConfigObject createConfig(String environment, File[] configFilesOrDirs ) {
		
		def configs = new HashMap<String,ConfigObject>()

		for( File configFileOrDir : configFilesOrDirs ) {		

			def configsToProcess = []		
			
			// Se for um arquivo, inclui na lista a ser processada
			if ( configFileOrDir.isFile()) {
				configsToProcess << configFileOrDir;
			}
			else if (configFileOrDir.isDirectory()){				
				configFileOrDir.eachFileMatch( ~/.*.config/) { file ->
					configsToProcess << file
				}			
			}
			else {
				// It's not a file, It's not a directory, it's Super File !
				// No. This is just the CLI parser doing its thing...
				continue
			}
						
			// Garante ordem consistente de merge...
			configsToProcess = configsToProcess.sort { File a,File b -> a.name <=> b.name } 
			
			ConfigObject partialConfig = null
			
			// Processa arquivo solitário ou no diretório
			configsToProcess.each { File configFile ->				
				log.info("[I113] Processing config file: '${configFile}'")
				
				if ( !configFile.isFile()) {
					throw new IllegalArgumentException("${configFile}: invalid configuration file")
				}
				
				def tmpConfig = new ConfigSlurper(environment).parse(configFile.toURI().toURL())
				if ( tmpConfig == null ) {
					throw new IllegalArgumentException("Unable to parse configuration file ${configFile}")
				}
				if ( partialConfig == null ) {
					partialConfig = tmpConfig
				}
				else {
					partialConfig.merge(tmpConfig)
				}				
			}
			
			if ( !partialConfig["generator"]) {
				if ( configFileOrDir.isFile()) {
					throw new IllegalArgumentException("Invalid configuration file ${configFileOrDir}. Required property 'generator' not found.")
				}
				else {
					throw new IllegalArgumentException("Invalid configuration directory ${configFileOrDir}. Required property 'generator' not found in any config file.")					
				}
			}
			
			// Adiciona as configurações na entrada correspondente
			// ao gerador
			def generator = partialConfig["generator"]	
			def mergedConfig = configs[generator]
			if ( mergedConfig == null ) {
				mergedConfig = partialConfig;
				configs[generator] = mergedConfig
			}
			else {
				mergedConfig.merge(partialConfig)
			}

		}

		return configs;	
	}
	
	/**
	 * retorna lista de arquivos contendo modelos a serem processados
	 * @param inputFileOrDir
	 * @return
	 */
	protected List<File> buildModelFileList(File inputFileOrDir) {
		
		if ( !inputFileOrDir.exists()) {
			throw new IllegalArgumentException("Caminho não encontrado: ${inputFileOrDir}")
		}
		
		if ( inputFileOrDir.isDirectory()) {
			def files = []
			inputFileOrDir.eachFileRecurse { 
				if ( it.isFile() && it.name.endsWith(".udsl")) {
					files << it.absoluteFile
				}
			}
			return files			
		}
		else {
			// retorna apenas as lista
			return [inputFileOrDir]
		}
		
	}
	
	@ShellMethod("Lista geradores disponíveis")
	public String listGenerators() {
		
		StringBuilder b = new StringBuilder()
		b.append("Geradores disponíveis:\n\n")
		
		generatorRegistry.generators.each {
			
			b.append("Nome: ${it.name}\n")
			 .append("Versão: ${it.version}\n")
			 .append("Descrição:\n")
			 .append(WordUtils.wrap(it.description, 64))
			 .append("\n")
			
		}
		
		return b.toString()
		
		
	} 
	
}
