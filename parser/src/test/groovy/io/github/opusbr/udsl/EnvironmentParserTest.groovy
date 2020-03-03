package io.github.opusbr.udsl

import static org.junit.Assert.*
import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.Matcher.*

import org.junit.Test

import groovy.json.JsonOutput
import groovy.util.logging.Slf4j

@Slf4j
class EnvironmentParserTest {

	@Test
	public void testParse() {
		
		def reader = getClass().getResourceAsStream("/sample1.udsl").newReader()
		def parser = new EnvironmentParser()
		def env = parser.parse(reader,"sample1.udsl", [version:"1.0"])	
		
		assertThat(env.size(), equalTo(1))
		
		def json = JsonOutput.toJson(env)
		println json
	}

	@Test
	public void testParse2() {
		
		def reader = getClass().getResourceAsStream("/sample2.udsl").newReader()
		def parser = new EnvironmentParser()
		def env = parser.parse(reader,"sample2.udsl", [version:"1.0"])

		assertThat(env.size(), equalTo(1))
		
		def count = 0
		env[0].traverse { spec ->
			count++
		}
		
		println "Elements found: ${count}"
		
		def json = JsonOutput.toJson(env)
		println json
	}

}
