package br.com.opussoftware.tools.udsl.generator.util

/**
 * Versão do FilteTreeBuilder padrão do Groovy que suporta sobrescrever arquivos existentes
 * @author Philippe
 *
 */
class FileTreeBuilder extends groovy.util.FileTreeBuilder {
	
	FileTreeBuilder(File baseDir) {
		super(baseDir)
	}
	
	File file(String name, boolean overwrite, content) {

		if ( overwrite ) {		
			def f = new File(baseDir,name)
			if ( f.exists() && f.isFile() ) {
				if ( !f.delete()) {
					throw new IOException("[E14] Não foi possível remover arquivo ${f}. Verifique se você tem permissão de escrita no direteório e/ou se ele está em uso")
				}
			}
		}

		file(name,content)		
	}
}
