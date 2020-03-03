package io.github.opusbr.tools.udsl.generator

import io.github.opusbr.udsl.model.EnvironmentSpec

interface Generator {
	
	// retorna nome do gerador
	String getName();
	String getDescription();
	String getVersion();
	String getAuthor();
	
	/**
	 * Gera artefatos para os ambientes indicados.
	 * @param envSpec
	 * @param generatorConfig Dados de configura��o utilizados pelo gerados
	 * @param outputDir Diret�rio-raiz para gera��o dos artefatos
	 * @return Quantidade de artefatos gerados
	 */
	int generate(List<EnvironmentSpec> envSpec, ConfigObject generatorConfig, File outputDir, ResourceLoader resourceLoader  )
	
	
}
