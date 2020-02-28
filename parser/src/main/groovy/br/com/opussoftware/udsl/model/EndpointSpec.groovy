package br.com.opussoftware.udsl.model

import groovy.transform.Canonical

@Canonical
class EndpointSpec extends AbstractSpec {	
	String target;
	String proto;
	String port
	boolean authenticated
	List<RouteSpec> routes = []
	
	public Route(Map params) {		
		def route = new RouteSpec(params)
		routes.add(route)		
	}
	
	
	@Override
	public List<AbstractSpec> getChildren() {
		return routes
	}

}

