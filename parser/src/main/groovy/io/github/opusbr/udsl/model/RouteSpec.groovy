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
	
	@Override
	public List<AbstractSpec> getChildren() {
		return []
	}

}

