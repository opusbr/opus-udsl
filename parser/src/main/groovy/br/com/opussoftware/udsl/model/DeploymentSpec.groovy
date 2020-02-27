package br.com.opussoftware.udsl.model

import groovy.transform.Canonical

@Canonical
class DeploymentSpec  extends AbstractSpec {	
	List<ImageSpec> images = []
	List<MessageChannelSpec> channels = []
	List<EndpointSpec> endpoints = []
	List<DeploymentSpec> deploymentRefs = []
	
	public Image(Map params) {		
		def image = new ImageSpec(params)
		images.add(image)		
	}

	public Image(Map params, @DelegatesTo(value=ImageSpec, strategy=Closure.DELEGATE_FIRST ) Closure spec) {
		def delegate = new ImageSpec(params)
				
		spec.delegate = delegate
		spec.resolveStrategy = Closure.DELEGATE_FIRST
		spec.run()

		images.add(delegate)
	}

	public Image(String name) {
		def image = new ImageSpec(name:name)
		images.add(image)
	}

	
	public MessageChannel(Map params) {
		def channel = new MessageChannelSpec(params)
		channels.add(channel)
	}

	public Endpoint(Map params) {
		def ep = new EndpointSpec(params)
		endpoints.add(ep)
	}

	public Endpoint(String name) {
		def ep = new EndpointSpec(name:name)
		endpoints.add(ep)
	}

	public Deployment(String name) {
		if ( name == this.name ) {
			throw new IllegalArgumentException("[E35] Invalid self-reference: deployment: ${this.name}, ref=${name}")
		}
		def dep = new DeploymentSpec(name:name)
		deploymentRefs.add(name)
	}
}
