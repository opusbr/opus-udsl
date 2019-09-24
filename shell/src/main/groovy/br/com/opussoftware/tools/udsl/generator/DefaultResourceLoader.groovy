package br.com.opussoftware.tools.udsl.generator

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import groovy.util.logging.Slf4j

@Component
@Slf4j
class DefaultResourceLoader implements ResourceLoader {
	
	private GeneratorConfig generatorConfig
	private org.springframework.core.io.ResourceLoader springResourceLoader
	
	
	DefaultResourceLoader( org.springframework.core.io.ResourceLoader springResourceLoader, GeneratorConfig generatorConfig) {
		this.springResourceLoader = springResourceLoader	
		this.generatorConfig = generatorConfig
	}
	
	
	@Override
	public InputStream getResourceAsStream(String name) {
		
		if ( generatorConfig.customTemplatesDir != null ) {
			def f = new File(generatorConfig.customTemplatesDir,name)
			if ( f.exists()) {
				log.info "[I29] utilizando template customizado: ${f}"
				return f.newInputStream()
			}
		}
		
		return springResourceLoader.getResource(name).getInputStream()
	}
	
	
}
