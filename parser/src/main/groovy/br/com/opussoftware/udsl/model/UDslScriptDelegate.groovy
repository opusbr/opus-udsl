package br.com.opussoftware.udsl.model

/**
 * Root delegate used as the base scripting class for uDSL files
 * @author Philippe
 *
 */
class UDslScriptDelegate {
	
	List<EnvironmentSpec> environments = [];
	
	UDslScriptDelegate(EnvironmentSpec initial = null) {
		if ( initial != null ) {
			environments << initial
		}
	}
	
	def Environment(Map args, @DelegatesTo(value=EnvironmentSpec, strategy=Closure.DELEGATE_FIRST) Closure spec) {
		def delegate = new EnvironmentSpec(args)
		spec.delegate = delegate
		spec.resolveStrategy = Closure.DELEGATE_FIRST
		spec.run()
		environments.add(delegate);
	}
	
	def Environment(String name, @DelegatesTo(value=EnvironmentSpec, strategy=Closure.DELEGATE_FIRST) Closure spec) {
		Environment([name:name], spec)
	}
	
	def Environment(String name, String[] tags, @DelegatesTo(value=EnvironmentSpec, strategy=Closure.DELEGATE_FIRST) Closure spec) {
		Environment([name:name,tags:tags], spec)		
	}

}
