package br.com.opussoftware.tools.udsl.generator.cmd

import org.apache.commons.text.WordUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption

import br.com.opussoftware.tools.udsl.generator.GeneratorRegistry
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
	
	
	@ShellMethod(value="Gera artefatos para o ambiente alvo a partir dos arquivos uDSL especificados")
	public String generate(
		@ShellOption(
			value= ["-g","--generator"],
			help="Gerador a ser utilizado. Para uma lista dos geradores disponíveis, utilize o comando listGenerators")
		String generator,
		@ShellOption(
			value= ["-s","--specFile"],
			help="Arquivo contendo parâmetros para geração do código")
		File specFile,
		@ShellOption(
			value= ["-o","--outputDir"],
			help="Diretório onde os artefatos serão criados. O diretório será criado caso não exista")
		File outputDir,
		@ShellOption(
			value= ["-i","--input"],
			help="Arquivo ou diretório de entrada. Caso seja um diretório, todos os arquivos com extensão .udsl serão processados")
		File inputFileOrDir ) {

		// Valida o gerador passado
		def gen = generatorRegistry.findByName(generator)
		log.info "Gerador selecionado: ${gen.name}, versão ${gen.version}"
		
		
		// Obtem lista de modelos
		def modelFiles = buildModelFileList(inputFileOrDir)
		if ( modelFiles.size() == 0 ) {
			throw new IllegalArgumentException("Nenhum modelo encontrado. Verifique se o caminho informado é válido e contem arquivos com a extensão .udsl")
		}
		
		// Valida a especificação
		if ( !specFile.isFile()) {
			throw new IllegalArgumentException("Arquivo de parametrização inválido: ${specFile}")
		}
		
		// Gera modelos
		def environments = []
		modelFiles.each {  
			log.info "Processando modelo: ${it.name}"
			def r = it.newReader()
			def envSpec = environmentParser.parse(r)
			environments << envSpec
		}
		
		log.info "${environments.size()} ambientes encontrados. Iniciando geração..."
		gen.generate(environments, specFile, outputDir)
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
