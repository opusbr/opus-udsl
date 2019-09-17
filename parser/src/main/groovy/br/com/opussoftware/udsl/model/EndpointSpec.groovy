package br.com.opussoftware.udsl.model

import groovy.transform.Canonical

@Canonical
class EndpointSpec {	
	String name;
	String target;
	String proto;
	boolean authenticated
	List<RouteSpec> routes = []
	
	public Route(Map params) {		
		def route = new RouteSpec(params)
		routes.add(route)		
	}
}

