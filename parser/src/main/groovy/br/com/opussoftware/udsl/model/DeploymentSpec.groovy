package br.com.opussoftware.udsl.model

import groovy.transform.Canonical

@Canonical
class DeploymentSpec {	
	String name;
	List<ImageSpec> images = []
	List<MessageChannelSpec> channels = []
	List<EndpointSpec> endpoints = []
	
	public Image(Map params) {		
		def image = new ImageSpec(params)
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

}
