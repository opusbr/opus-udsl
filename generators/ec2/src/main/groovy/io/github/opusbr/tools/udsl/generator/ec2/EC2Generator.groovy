/**
 * 
 */
package io.github.opusbr.tools.udsl.generator.ec2

import org.codehaus.groovy.control.CompilerConfiguration

import io.github.opusbr.tools.udsl.generator.AbstractGenerator
import io.github.opusbr.tools.udsl.generator.Generator
import io.github.opusbr.tools.udsl.generator.ResourceLoader
import io.github.opusbr.tools.udsl.generator.util.FileTreeBuilder
import io.github.opusbr.udsl.model.EnvironmentSpec
import groovy.util.logging.Slf4j

/**
 * @author Philippe
 *
 */
@Slf4j
class EC2Generator extends AbstractGenerator {
	


	@Override
	public String getName() {
		return "ec2";
	}

	@Override
	public String getDescription() {
		return "Gerador de código Terraform para provisionamento de ambiente de microserviços em ambiente EC2";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "PMS";
	}


}
