package br.com.opussoftware.tools.udsl.generator

import br.com.opussoftware.tools.udsl.generator.ResourceLoader
import br.com.opussoftware.tools.udsl.generator.util.FileTreeBuilder
import br.com.opussoftware.tools.udsl.generator.util.TemplateHelper
import groovy.transform.Canonical
import groovy.util.logging.Slf4j

/**
 * Delegate para processar o script de configuração do gerador
 * @author Philippe
 *
 */
@Canonical
@Slf4j
class GeneratorDelegate {
	
	private FileTreeBuilder fileTreeBuilder
	private Map args;
	private ResourceLoader resourceLoader
	private boolean ftbHookInstalled = false
	private String prefix = ""
	
	
	private _globalBindings = [:]
	private _namedBindings = [:]
	
	public void globalBindings( Map bindings ) {		
		_globalBindings.putAll(bindings)
	}
	

	public void bindings( String name, Map<String,Object> bindings) {
		
		if ( _namedBindings[name] == null ) {
			_namedBindings[name] = [:]
		}
		
		_namedBindings[name].putAll(bindings)
		
	}
	
	public Map getArgs() {
		return this.args;
	} 
	
	public File dir(String name, Closure closure) {
		checkFtbHook()
		log.info ("[I46] Creating diretory ${name}")
		Object delegate = closure.getDelegate()
		return fileTreeBuilder.dir(name,closure)
	}

	public def file(Map args) {
		
		if ( !args.containsKey("name")) {
			throw new IllegalArgumentException("[E54] file name is required")
		}

		if ( !args.containsKey("template")) {
			throw new IllegalArgumentException("[E57] template name is required")
		}

		file(args.name,args.template,args.overwrite?:false,args.bindings?:[:])		
	}

	public def file(String name, String template, boolean overwrite, String bindings) {
		
		if ( _namedBindings.containsKey(bindings)) {
			Map b = _namedBindings[bindings]
			return file(name,template, overwrite,b)
		}
		else {
			throw new IllegalArgumentException("[E45] invalid binding name: ${bindings}")
		}
	}
	
	/**
	 * Cria o arquivo no local destino
	 * @param name
	 * @param template
	 * @param overwrite
	 * @param bindings
	 * @return
	 */
	public File file(String name, String template, boolean overwrite, Map bindings) {
		checkFtbHook()
		

		// Bindings passados para o script são os globais mais os específicos
		// do script
		def tplBindings = [:]
		tplBindings.putAll(_globalBindings)
		tplBindings.putAll(bindings)
		
		byte[] content = TemplateHelper.processTemplate(resourceLoader, prefix + template, tplBindings)
				
		return fileTreeBuilder.file(name, overwrite, content)
	}
	
	private void checkFtbHook() {
		if ( ftbHookInstalled ) {
			return
		}
		
		def self = this;
		
		fileTreeBuilder.metaClass.methodMissing = { name, args ->			
			if (args instanceof Object[] && ((Object[]) args).length == 1) {
				def arg = ((Object[]) args)[0]
				if (arg instanceof Closure) {
					dir(name, arg)
				} else if (arg instanceof CharSequence) {
					file(name, arg.toString())
				} else if (arg instanceof byte[]) {
					file(name, arg)
				} else if (arg instanceof File) {
					file(name, arg)
				} else if ( arg instanceof Map ) {
					self.file(arg)
				}
			}
		}
		
		fileTreeBuilder.metaClass.propertyMissing = { name ->
			
			if ( self.args[name] != null ) {
				return self.args[name]
			}
			else if ( name == "args") {
				return args
			}
			else {
				throw new IllegalArgumentException("[E133] Uknown property: ${name}")
			}
		}
		
		ftbHookInstalled = true
		
	}

}
