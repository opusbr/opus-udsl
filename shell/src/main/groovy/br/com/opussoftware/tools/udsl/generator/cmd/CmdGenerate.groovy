package br.com.opussoftware.tools.udsl.generator.cmd

import org.apache.commons.text.WordUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption

import br.com.opussoftware.tools.udsl.generator.GeneratorRegistry
import br.com.opussoftware.tools.udsl.generator.ResourceLoader
import br.com.opussoftware.udsl.EnvironmentParser
import groovy.util.logging.Slf4j

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
	
	
	@ShellMethod(value="Gera artefatos para o ambiente alvo a partir dos arquivos uDSL especificados")
	public String generate(
		@ShellOption(
			value= ["-i","--input"],
			help="Arquivo ou diretório de entrada. Caso seja um diretório, todos os arquivos com extensão .udsl serão processados")
		File inputFileOrDir,
		@ShellOption(
			value= ["-s","--spec"],
			help="Arquivo ou diretório contendo parâmetros para geração do código")
		File specFileOrDir,
		@ShellOption(
			value= ["-o","--outputDir"],
			help="Diretório onde os artefatos serão criados. O diretório será criado caso não exista")
		File outputDir,
		
		@ShellOption(
			value = ["-e", "--env"],
			help = "Environment a ser utilizado para a parametrização. Se não fornecido, será utilizado o Environment padrão do arquivo de configuração",
			defaultValue = "" )
		String environment 		
		) {
		
		log.info "Diretório de saída informado: ${outputDir}"
		
		// Processa arquivo de configuração passado
		log.info "Processando arquivo de parametrização: ${specFileOrDir}"
		def config = createConfig(environment,specFileOrDir)
		
		
		if ( !config["generator"]) {
			throw new IllegalArgumentException("Arquivo de configuração não possui a chave 'generator'. Verifique o arquivo de configuração")
		}

		// Valida o gerador passado
		def gen = generatorRegistry.findByName(config.generator)
		log.info "Gerador selecionado: ${gen.name}, versão ${gen.version}"
		
		
		// Obtem lista de modelos
		def modelFiles = buildModelFileList(inputFileOrDir)
		if ( modelFiles.size() == 0 ) {
			throw new IllegalArgumentException("Nenhum modelo encontrado. Verifique se o caminho informado é válido e contem arquivos com a extensão .udsl")
		}
		
		
		// Gera modelos
		def environments = []
		modelFiles.each {  
			log.info "Processando modelo: ${it.name}"
			def r = it.newReader()
			def envSpec = environmentParser.parse(r)
			environments += envSpec
		}
		
		log.info "${environments.size()} ambientes encontrados. Iniciando geração..."
		gen.generate(environments, config, outputDir, resourceLoader)
		
		log.info "Geração finalizada com sucesso"
		
	}
	
	protected ConfigObject createConfig(String environment, File configFileOrDir ) {
		
		def configs = [];
		
		if ( configFileOrDir.isFile()) {
			configs << configFileOrDir;
		}
		else if (configFileOrDir.isDirectory()){
			
			configFileOrDir.eachFileMatch( ~/.*.config/) { file ->
				configs << file
			}			
		}
		
		ConfigObject mergedConfig = null; 
		
		// Garante ordem consistente de merge...
		configs = configs.sort { File a,File b -> a.name <=> b.name } 
		
		configs.each { File configFile ->
			
			log.info("[I113] Processando arquivo de configuração: ${configFile}")
			def partialConfig = new ConfigSlurper(environment).parse(configFile.toURI().toURL())
			
			if ( mergedConfig == null ) {
				mergedConfig = partialConfig;
			}
			else {
				mergedConfig.merge(partialConfig)
			}
			
		}

		return mergedConfig;	
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
