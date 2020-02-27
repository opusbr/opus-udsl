/**
 * 
 */
package br.com.opussoftware.udsl.model

import groovy.transform.Canonical

/**
 * @author Philippe
 *
 */
@Canonical
class EnvironmentSpec extends AbstractSpec {
	
	List<EndpointSpec> endpoints = []
	List<EndpointSpec> externalEndpoints = []
	List<DeploymentSpec> deployments = []
	List<MessageChannelSpec> messageChannels = []
	
	public Endpoint( String name, @DelegatesTo(value=EndpointSpec, strategy=Closure.DELEGATE_FIRST ) Closure spec) {
		def delegate = new EndpointSpec(name)
		spec.delegate = delegate
		spec.resolveStrategy = Closure.DELEGATE_FIRST
		spec.run()
		
		endpoints.add(delegate)		
	}
	
	/**
	 * Service Endpoint
	 * @param name
	 * @param target
	 * @param proto
	 * @return
	 */
	public Endpoint( Map params) {		
		def ep = new EndpointSpec(params)		
		externalEndpoints.add(ep)
	}

	public Deployment( Map args, @DelegatesTo(value=DeploymentSpec, strategy=Closure.DELEGATE_FIRST ) Closure spec) {
		def delegate = new DeploymentSpec(name:name)
		spec.delegate = delegate
		spec.resolveStrategy = Closure.DELEGATE_FIRST
		spec.run()
		
		deployments.add(delegate)
	}
	
	public Deployment( String name, @DelegatesTo(value=DeploymentSpec, strategy=Closure.DELEGATE_FIRST ) Closure spec) {
		Deployment([name:name],spec)
	}
	
	public Deployment( String name, String[] tags, @DelegatesTo(value=DeploymentSpec, strategy=Closure.DELEGATE_FIRST ) Closure spec) {
		Deployment([name:name,tags:tags],spec)
	}


	public MessageChannel( Map params ) {
		def delegate = new MessageChannelSpec(params)
		
		messageChannels.add(delegate)

	}

}
