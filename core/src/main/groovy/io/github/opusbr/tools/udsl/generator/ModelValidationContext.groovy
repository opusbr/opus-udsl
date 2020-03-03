package io.github.opusbr.tools.udsl.generator

import io.github.opusbr.udsl.model.AbstractSpec

/**
 * ModelValidationContext passed to validators at validation time.
 * Contains methods that allow a validator to raise issues that are reported to
 * the end user. Any "ERROR" Level issue will act as a blocker and will prevent
 * the actual generation to happen.
 * 
 * @author Philippe
 *
 */
interface ModelValidationContext {

	/**
	 * Possible levels for a given validation issue.	
	 * @author Philippe
	 *
	 */
	enum Level {
		INFO(0),
		WARN(1),
		ERROR(2)
	}
	
	/**
	 * Returns the configuration info passed to the generator
	 * @return
	 */
	ConfigObject getConfig()
	
	/**
	 * Returns current resource loader. Useful if your validator needs
	 * to load some additional configuration info.
	 * @return
	 */
	ResourceLoader getResouceLoader()
	
	/**
	 * Add a validation issue
	 * @param element
	 * @param level
	 * @param message
	 */
	void addValidationIssue(AbstractSpec element, Level level, String message)
	
}
