package br.com.opussoftware.tools.udsl.generator

import br.com.opussoftware.udsl.model.EnvironmentSpec

interface Generator {
	
	// retorna nome do gerador
	String getName();
	String getDescription();
	String getVersion();
	String getAuthor();
	
	int generate(List<EnvironmentSpec> envSpec, File generatorSpecFile, File outputDir)
	
	
}
