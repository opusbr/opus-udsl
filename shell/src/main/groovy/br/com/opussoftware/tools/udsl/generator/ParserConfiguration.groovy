package br.com.opussoftware.tools.udsl.generator

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import br.com.opussoftware.udsl.EnvironmentParser

@Configuration
class ParserConfiguration {
	
	
	@Bean
	EnvironmentParser environmentParser() {
		return new EnvironmentParser()
	}
}
