package br.com.opussoftware.tools.udsl.generator.k8s

import br.com.opussoftware.udsl.model.EndpointSpec

class K8SHelper {
	
	static String ingressName(EndpointSpec ep) {
		return ingressName(ep.name)
	}
	
	static String ingressName(String epName) {
		return validName(epName)
	}
	
	
	static String validName(String name) {
		
		return name.collect({
			if ( it =~ /[0-9A-Za-z]/ || it == '.' || it == '-') {
				return it.toLowerCase()
			}
			else {
				return '-'
			}
		}).join()

	}
}
