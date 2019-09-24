package br.com.opussoftware.tools.udsl.generator

import br.com.opussoftware.udsl.model.EnvironmentSpec

interface Generator {
	
	// retorna nome do gerador
	String getName();
	String getDescription();
	String getVersion();
	String getAuthor();
	
	/**
	 * Gera artefatos para os ambientes indicados.
	 * @param envSpec
	 * @param generatorConfig Dados de configuração utilizados pelo gerados
	 * @param outputDir Diretório-raiz para geração dos artefatos
	 * @return Quantidade de artefatos gerados
	 */
	int generate(List<EnvironmentSpec> envSpec, ConfigObject generatorConfig, File outputDir, ResourceLoader resourceLoader  )
	
	
}
