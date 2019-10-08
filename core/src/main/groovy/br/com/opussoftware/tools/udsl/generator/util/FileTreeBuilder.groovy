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

		def f = new File(baseDir,name)
		if ( overwrite ) {		
			if ( f.exists() && f.isFile() ) {
				if ( !f.delete()) {
					throw new IOException("[E14] Não foi possível remover arquivo ${f}. Verifique se você tem permissão de escrita no direteório e/ou se ele está em uso")
				}
			}			
		}
		else {
			if (f.exists()) {
				// Arquivo já existe. Deixa em paz...
				return f;
			}
		}
		
		// Cria arquivo
		file(name,content)

	}
}
