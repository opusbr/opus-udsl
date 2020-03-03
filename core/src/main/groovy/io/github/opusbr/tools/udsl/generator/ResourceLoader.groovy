package io.github.opusbr.tools.udsl.generator

/**
 * Serviço utilizado pelos geradores para obter recursos. 
 * @author Philippe
 *
 */
interface ResourceLoader {
	
	InputStream getResourceAsStream(String name)
}
