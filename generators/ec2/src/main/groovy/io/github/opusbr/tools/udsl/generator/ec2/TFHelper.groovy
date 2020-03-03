package io.github.opusbr.tools.udsl.generator.ec2

import io.github.opusbr.udsl.model.EndpointSpec

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
