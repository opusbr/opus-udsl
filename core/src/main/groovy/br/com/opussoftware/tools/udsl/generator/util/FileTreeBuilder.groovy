package br.com.opussoftware.tools.udsl.generator.util

/**
 * Vers�o do FilteTreeBuilder padr�o do Groovy que suporta sobrescrever arquivos existentes
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
					throw new IOException("[E14] N�o foi poss�vel remover arquivo ${f}. Verifique se voc� tem permiss�o de escrita no direte�rio e/ou se ele est� em uso")
				}
			}			
		}
		else {
			if (f.exists()) {
				// Arquivo j� existe. Deixa em paz...
				return f;
			}
		}
		
		// Cria arquivo
		file(name,content)

	}
}
