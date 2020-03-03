package io.github.opusbr.tools.udsl.generator

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import io.github.opusbr.udsl.EnvironmentParser

@Configuration
class ParserConfiguration {
	
	
	@Bean
	EnvironmentParser environmentParser() {
		return new EnvironmentParser()
	}
}
