package br.com.opussoftware.udsl;

import org.codehaus.groovy.transform.stc.GroovyTypeCheckingExtensionSupport

/*
 * TypeChecker extensions
 * @see http://docs.groovy-lang.org/docs/latest/html/documentation/type-checking-extensions.html
 */

class UDslScriptExtention extends GroovyTypeCheckingExtensionSupport.TypeCheckingDSL {
	
	@Override
	Object run() {

/*	TBD: Implement checks
 * 	
		methodNotFound { receiver, name, argList, argTypes, call ->

			println "methodNotFound: receiver=${receiver}, name=${name}, argTypes=${argTypes}"
			
			if ( name == "Environment" ) {				
				handled = true
				return newMethod("Environment",classNodeFor(Object))
			}
			
		}
		
		onMethodSelection { expr, method ->
			
			println "onMethodSelection: method=${method}"
			if ( method.name == "Environment") {
				
			}
			 
		}
*/
				
	}
}

