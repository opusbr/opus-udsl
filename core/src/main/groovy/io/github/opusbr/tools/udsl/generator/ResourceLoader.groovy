package io.github.opusbr.tools.udsl.generator

/**
 * Servi�o utilizado pelos geradores para obter recursos. 
 * @author Philippe
 *
 */
interface ResourceLoader {
	
	InputStream getResourceAsStream(String name)
}
