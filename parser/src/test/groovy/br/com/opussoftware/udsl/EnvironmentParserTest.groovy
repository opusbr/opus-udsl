package br.com.opussoftware.udsl

import static org.junit.Assert.*

import org.junit.Test

import groovy.json.JsonOutput
import groovy.util.logging.Slf4j

@Slf4j
class EnvironmentParserTest {

	@Test
	public void testParse() {
		
		def reader = getClass().getResourceAsStream("/sample1.udsl").newReader()
		def parser = new EnvironmentParser()
		def env = parser.parse(reader)	
		
		def json = JsonOutput.toJson(env)
		println json
	}

}
