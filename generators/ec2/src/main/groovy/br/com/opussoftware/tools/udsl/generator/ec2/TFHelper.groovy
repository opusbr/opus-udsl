package br.com.opussoftware.tools.udsl.generator.ec2

import br.com.opussoftware.udsl.model.EndpointSpec

class TFHelper {
	
	static String moduleName(String name) {
		
		return name.collect({
			if ( it =~ /[0-9A-Za-z]/ || it == '_' || it == '-') {
				return it
			}
			else {
				return '_'
			}
		}).join()

	}
}
