package io.github.opusbr.tools.udsl.generator

import io.github.opusbr.udsl.model.EnvironmentSpec

/**
 * Interface that generators can implement if they also provide validation
 * capabilities
 * 
 * @author Philippe
 *
 */
interface ModelValidator {
	
	void validate(List<EnvironmentSpec> specs, ConfigObject config, ResourceLoader loader)
}
