package br.com.opussoftware.tools.udsl.generator.util

import br.com.opussoftware.tools.udsl.generator.ResourceLoader
import groovy.text.StreamingTemplateEngine
import groovy.util.logging.Slf4j

/**
 * Helper para geração de arquivos a partir de um template
 * @author Philippe
 *
 */
@Slf4j
class TemplateHelper {

	static byte[] processTemplate(ResourceLoader loader, String template, Map bindings) {
		
		log.info("Processando template: ${template}")
		
		Reader r= locateTemplate(loader, template);
		try {
			def tpl = new StreamingTemplateEngine().createTemplate(r);
			
			StringWriter sw = new StringWriter()
			tpl.make(bindings).writeTo(sw)
			return sw.toString().getBytes("UTF-8");
		}
		finally {
			r.close();
		}
		
	}
	
	/**
	 * Retorna <code>Reader</code> para leitura do template
	 * @param loader ResourceLoader a ser utilizado
	 * @param tpl Nome do template
	 * @return
	 */
	static Reader locateTemplate(ResourceLoader loader, String tpl) {
				
		InputStream is = loader.getResourceAsStream(tpl)
		if ( is == null ) {
			throw new IllegalArgumentException("Template não encontrado: ${tpl}")
		}
		return is.newReader()
		
	}

}
