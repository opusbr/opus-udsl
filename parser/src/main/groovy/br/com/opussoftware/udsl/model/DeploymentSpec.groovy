package br.com.opussoftware.udsl.model

import groovy.transform.Canonical

@Canonical
class DeploymentSpec {	
	String name;
	List<ImageSpec> images = []
	List<MessageChannelSpec> channels = []
	List<EndpointSpec> endpoints = []
	List<DeploymentSpec> deploymentRefs = []
	
	public Image(Map params) {		
		def image = new ImageSpec(params)
		images.add(image)		
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
			throw new IllegalArgumentException("[E35] Referência inválida: deployment: ${this.name}, ref=${name}")
		}
		def dep = new DeploymentSpec(name:name)
		deploymentRefs.add(name)
	}
}
