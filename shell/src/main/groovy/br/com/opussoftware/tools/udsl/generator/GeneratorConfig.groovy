package br.com.opussoftware.tools.udsl.generator

import org.springframework.stereotype.Component

/**
 * Configura��o global utilizada pelo gerador
 * @author Philippe
 *
 */
@Component
class GeneratorConfig {
	
	File customTemplatesDir;
	
	public GeneratorConfig() {}
	
}
