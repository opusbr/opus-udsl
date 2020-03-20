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

		ServiceSpec svc
		if ( params.containsKey("name")) {
			svc = new ServiceSpec(params)
			if (serviceNames.contains(svc.name)) {
				// Flag issue for this service
				svc.addIssue(Level.ERROR,"SVC001 - Duplicate service name: ${svc.name}")
			}
			else {
				serviceNames.add(svc.name)
			}
		}
		else {
			// Anonymous service. As we always need a name,
			// we'll inherit the Deployment's name. However,
			def aux = [name: this.name]
			aux << params
			if (serviceNames.contains(svc.name)) {
				// Flag issue for this service
				svc.addIssue(Level.ERROR,"SVC002 - There's already another anonymous service or a service named ${this.name}")
			}
			else {
				serviceNames.add(svc.name)
			}

			
			svc = new ServiceSpec(params)
		}

		services.add(svc)
	}

	public Service(Map params, @DelegatesTo(value=ServiceSpec, strategy=Closure.DELEGATE_FIRST ) Closure spec) {
		def delegate = new ServiceSpec(params)

		spec.delegate = delegate
		spec.resolveStrategy = Closure.DELEGATE_FIRST
		spec.run()

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
