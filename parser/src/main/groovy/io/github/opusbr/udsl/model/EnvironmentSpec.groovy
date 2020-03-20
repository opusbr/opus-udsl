/**
 * 
 */
package io.github.opusbr.udsl.model

import groovy.transform.Canonical
import io.github.opusbr.udsl.model.SpecIssue.Level

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
	
	Set<String> deploymentNames = []
	Set<String> endpointNames = []
	Set<String> externalEndpointNames = []
	
	/**
	 * Outbound endpoint declaration
	 */
	public Endpoint( Map params) {		
		def ep = new EndpointSpec(params)
		
		if ( externalEndpointNames.contains(ep.name)) {
			ep.addIssue(Level.ERROR, "E036 - Outbound Endpoint '${ep.name}' must be unique in the Environment '${this.name}'")
		}
		else {
			externalEndpointNames.add(ep.name)
		}
		
		externalEndpoints.add(ep)
	}

	/**
	 * Inbound Endpoint declaration
	 * @param name
	 * @param spec
	 * @return
	 */
	public Endpoint( String name, @DelegatesTo(value=EndpointSpec, strategy=Closure.DELEGATE_FIRST ) Closure spec) {
		Endpoint( [name:name], spec)
	}
	

	/**
	 * Inbound Endpoint declaration
	 * @param name
	 * @param tags
	 * @param spec
	 * @return
	 */
	public Endpoint( String name, Map tags, @DelegatesTo(value=EndpointSpec, strategy=Closure.DELEGATE_FIRST ) Closure spec) {
		Endpoint( [name:name, tags: tags], spec)
	}

	/**
	 * Inbound Endpoint declaration
	 * @param args
	 * @param spec
	 * @return
	 */
	public Endpoint( Map args , @DelegatesTo(value=EndpointSpec, strategy=Closure.DELEGATE_FIRST ) Closure spec) {
		def delegate = new EndpointSpec(args)
		spec.delegate = delegate
		spec.resolveStrategy = Closure.DELEGATE_FIRST
		spec.run()
		
		if ( endpointNames.contains(delegate.name)) {
			delegate.addIssue(Level.ERROR, "E61 - Inbound endpoint ${delegate.name} must be unique in the Environment '${this.name}'")
		}
		
		endpoints.add(delegate)
	}


	/**
	 * Deployment declaration
	 * @param args
	 * @param spec
	 * @return
	 */
	public Deployment( Map args, @DelegatesTo(value=DeploymentSpec, strategy=Closure.DELEGATE_FIRST ) Closure spec) {
		
		if ( !args.containsKey("name")) {
			def aux = [name: "${this.name}_${deployments.size()}"]
			aux << args
			args = aux
		}
		
		def delegate = new DeploymentSpec(args)
		spec.delegate = delegate
		spec.resolveStrategy = Closure.DELEGATE_FIRST
		spec.run()
		
		if ( deploymentNames.contains(delegate.name)) {
			delegate.addIssue(Level.ERROR, "E001 - Deployment '${args.name}' must be unique in Environment '${this.name}'")
		}
		else {
			deploymentNames.add(delegate.name)
		}
		
		
		deployments.add(delegate)
	}
	
	
	public Deployment( String name, @DelegatesTo(value=DeploymentSpec, strategy=Closure.DELEGATE_FIRST ) Closure spec) {
		Deployment([name:name],spec)
	}
	
	public Deployment( String name, Map tags, @DelegatesTo(value=DeploymentSpec, strategy=Closure.DELEGATE_FIRST ) Closure spec) {
		Deployment([name:name,tags:tags],spec)
	}


	public MessageChannel( Map params ) {
		def delegate = new MessageChannelSpec(params)
		
		messageChannels.add(delegate)

	}
	
	@Override
	public List<AbstractSpec> getChildren() {
		return endpoints + externalEndpoints + deployments + messageChannels
	}

}
