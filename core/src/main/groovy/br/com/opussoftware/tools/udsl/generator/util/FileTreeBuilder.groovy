package br.com.opussoftware.tools.udsl.generator.util

import br.com.opussoftware.tools.udsl.generator.ResourceLoader
import groovy.util.logging.Slf4j

/**
 * Versão do FilteTreeBuilder padrão do Groovy que suporta sobrescrever arquivos existentes
 * @author Philippe
 *
 */
@Slf4j
class FileTreeBuilder extends groovy.util.FileTreeBuilder {
	
	private ResourceLoader _resourceLoader
	
	FileTreeBuilder(File baseDir) {
		super(baseDir)
	}
	
	
	File file(String name, boolean overwrite, content) {

		log.info("[I23] Creating file: ${name}, overwrite=${overwrite}, baseDir=${baseDir}")
		
		def f = new File(baseDir,name)
		if ( overwrite ) {		
			if ( f.exists() && f.isFile() ) {
				log.info("[I28] Existing file will be overwritten.")
				if ( !f.delete()) {
					throw new IOException("[E14] Error removing file ${f}. Please check permissions.")
				}
			}			
		}
		else {			
			if (f.exists()) {
				log.info("[I35] Keeping existing file...")				
				// Arquivo já existe. Deixa em paz...
				return f;
			}
		}
		
		// Cria arquivo
		file(name,content)
		log.info("[I45] File created OK")
		
	}

	
	
}
