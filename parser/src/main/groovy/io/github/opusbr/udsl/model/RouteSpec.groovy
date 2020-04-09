package io.github.opusbr.udsl.model

import groovy.transform.Canonical

@Canonical
class RouteSpec extends AbstractSpec {	
	String path;
	
	/**
	 * Forward requests to another deployment
	 */
	String deployment;
	
	/**
	 * Optional contract attached to this route
	 */
	String contract
	
	/**
	 * Forward request to another endpoint
	 */
	String endpoint
	
	/**
	 * The external client must be authenticated to access this route
	 */
	Boolean authenticated
	
	@Override
	public List<AbstractSpec> getChildren() {
		return []
	}

}

