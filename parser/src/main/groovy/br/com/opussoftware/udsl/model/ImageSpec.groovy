package br.com.opussoftware.udsl.model

import groovy.transform.Canonical

/**
 * A deployable component that belongs to a Deployment
 * This class is deprecated. New code should use ServiceSpec instead
 * @author Philippe
 * 
 */
@Canonical
@Deprecated
class ImageSpec extends AbstractSpec {
	
	List<RouteSpec> routes = [];
	
	def Route(Map params) {
		def spec = new RouteSpec(params)
		routes << spec
	}	
	
	
	def methodMissing(name,args) {
		println "[E17] methodMissing: name=${name}, args=${args}"
		throw new IllegalArgumentException("Element not supported: name=${name}")
	}
	
	@Override
	public List<AbstractSpec> getChildren() {
		return routes
	}

}

