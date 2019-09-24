package br.com.opussoftware.tools.udsl.generator.cmd

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption

import br.com.opussoftware.tools.udsl.generator.GeneratorConfig
import groovy.util.logging.Slf4j

@ShellComponent
@Slf4j
class CmdConfiguration {
	
	private GeneratorConfig generatorConfig
	
	CmdConfiguration(GeneratorConfig generatorConfig)  {
		this.generatorConfig = generatorConfig
	}
	
	
	@ShellMethod("Define diret�rio contendo templates customizados")
	void setCustomTemplatesDir(
		@ShellOption(
			value= ["-d","--dir"],
			help="Caminho relativo ou absoluto para o diret�rio contendo templates customizados")
		File dir ) {
		
		if ( !dir.exists() || !dir.isDirectory()) {
			println "Diret�rio inv�lido: ${dir}"
			return
		}
		
		log.info "Diret�rio customizado: ${dir}"
		generatorConfig.customTemplatesDir = dir
	}

}
