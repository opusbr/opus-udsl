package io.github.opusbr.udsl.model

import groovy.transform.Canonical
import io.github.opusbr.udsl.model.SpecIssue.Level

@Canonical
class DeploymentSpec  extends AbstractSpec {
	List<ServiceSpec> services = []
	List<MessageChannelSpec> channels = []
	List<EndpointSpec> endpoints = []
	List<DeploymentSpec> deploymentRefs = []
	Set<String> serviceNames = []

	public Service(Map params) {
		if ( !params.containsKey("name")) {
			params = params + [name:this.name]				
		}
	}
	
	public Service(@DelegatesTo(value=ServiceSpec, strategy=Closure.DELEGATE_FIRST ) Closure spec) {
		Service([name:this.name],spec)
	}

	public Service(Map params, @DelegatesTo(value=ServiceSpec, strategy=Closure.DELEGATE_FIRST ) Closure spec) {
		def delegate = new ServiceSpec(params)

		spec.delegate = delegate
		spec.resolveStrategy = Closure.DELEGATE_FIRST
		spec.run()
		
		if ( serviceNames.contains(delegate.name)) {
			delegate.addIssue(Level.ERROR,"SVC060 - Service ${delegate.name} must be unique in Deployment ${this.name}")			
		}
		else {
			serviceNames.add(delegate.name)
		}

		services.add(delegate)
	}

	public Service(String name) {
		def image = new ServiceSpec(name:name)
		services.add(image)
	}


	public Image(Map params) {
		Service(params)
	}

	public Image(Map params, @DelegatesTo(value=ImageSpec, strategy=Closure.DELEGATE_FIRST ) Closure spec) {
		Service(params,spec)
	}

	public Image(String name) {
		Service(name)
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

	/**
	 * Compatibily method. This will be removed in a future version
	 * @return
	 */
	@Deprecated
	public List<ImageSpec> getImages() {
		return services
	}

	@Override
	public List<AbstractSpec> getChildren() {
		return services + channels + endpoints + deploymentRefs
	}
}
