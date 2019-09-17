package br.com.opussoftware.tools.udsl.generator

import org.springframework.stereotype.Component

@Component
class GeneratorRegistry {
	
	private List<Generator> generators;
	
	public GeneratorRegistry(List<Generator> generators) {
		this.generators = generators
		
		// Adiciona geradores do classpath
		ServiceLoader.load(Generator.class).each { 
			this.generators << it
		}
		
	}
	
	
	public Generator findByName(String name) {
		
		Generator gen = generators.find { it.name == name }
		if ( gen == null ) {
			throw new IllegalArgumentException("Gerador não registrado: ${name}. Utilize o comando listGenerators para obter a lista de geradores disponíveis")
		}
		
		return gen
	}
	
	public List<Generator> getGenerators() {
		return generators;
	}
}
