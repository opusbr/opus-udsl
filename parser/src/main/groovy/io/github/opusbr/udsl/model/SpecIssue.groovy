package io.github.opusbr.udsl.model

import groovy.transform.Canonical

/**
 * A semantical issue captured during the build phase and stored
 * in a Spec.
 * @author Philippe
 *
 */
@Canonical
public class SpecIssue {
	
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
	
	private Level level;
	private AbstractSpec spec;
	private String issue;

}
