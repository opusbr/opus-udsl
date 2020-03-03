package io.github.opusbr.tools.udsl.generator

import org.springframework.boot.Banner
import org.springframework.boot.ResourceBanner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.core.env.Environment
import org.springframework.core.env.PropertyResolver
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource

@SpringBootApplication
class OpusUdslGeneratorApplication {

	static void main(String[] args) {
		SpringApplication.run(OpusUdslGeneratorApplication,args)
	}

}
