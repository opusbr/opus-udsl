package br.com.opussoftware.tools.udsl.generator

import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.shell.Shell
import org.springframework.shell.jline.InteractiveShellApplicationRunner
import org.springframework.test.context.junit4.SpringRunner

import groovy.util.logging.Slf4j

import org.springframework.shell.Input

@RunWith(SpringRunner.class)
@SpringBootTest(
	properties = [ "spring.shell.interactive.enabled=false" ])
@Slf4j
public class OpusUdslGeneratorApplicationTests {

	@Autowired
	private Shell shell;

	@Test
	public void testGenerateK8S() {
		
		def tempDir = Files.createTempDirectory("junit")
		def modelFile = new File(this.getClass().getResource("/sample1.udsl").toURI())
		def configFile = new File(this.getClass().getResource("/sample1.config").toURI())
		
		def result = shell.evaluate(new Input() {

			@Override
			public String rawText() {
				return "generate -i ${modelFile.absolutePath} -s ${configFile.absolutePath} -o ${tempDir} -e qa";
			}
		})
		
		if ( result instanceof Throwable ) {
			log.error "Erro executando comando: ${result.message}", result
			throw result
		}
		
	}
	
	@Test
	public void testGenerateK8SFromDir() {
		
		def tempDir = Files.createTempDirectory("junit")
		def modelFile = new File("src/test/resources")
		def configFile = new File(this.getClass().getResource("/sample1.config").toURI())
		
		def result = shell.evaluate(new Input() {

			@Override
			public String rawText() {
				return "generate -i ${modelFile.absolutePath} -s ${configFile.absolutePath} -o ${tempDir} -e qa";
			}
		})
		
		if ( result instanceof Throwable ) {
			log.error "Erro executando comando: ${result.message}", result
			throw result
		}
		
	}

	@Test
	public void testGenerateK8SFromConfigDir() {
		
		def tempDir = Files.createTempDirectory("junit")
		def modelFile = new File("src/test/resources/sample1.udsl")
		def configFile = new File("src/test/resources")
		
		def result = shell.evaluate(new Input() {

			@Override
			public String rawText() {
				return "generate -i ${modelFile.absolutePath} -s ${configFile.absolutePath} -o ${tempDir} -e qa";
			}
		})
		
		if ( result instanceof Throwable ) {
			log.error "Erro executando comando: ${result.message}", result
			throw result
		}
		
	}

	@Test
	public void testGenerateEC2() {
		
		
		def tempDir = Files.createDirectories(FileSystems.getDefault().getPath("target/ec2-generated"));
		def modelFile = new File(this.getClass().getResource("/sample1-ec2.udsl").toURI())
		def configFile = new File(this.getClass().getResource("/sample1-ec2.config").toURI())
		
		def result = shell.evaluate(new Input() {
	
			@Override
			public String rawText() {
				return "generate -i ${modelFile.absolutePath} -s ${configFile.absolutePath} -o ${tempDir} -e qa";
			}
		})
		
		if ( result instanceof Throwable ) {
			log.error "Erro executando comando: ${result.message}", result
			throw result
		}
		
	}

}