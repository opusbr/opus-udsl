package br.com.opussoftware.tools.udsl.generator.k8s

import br.com.opussoftware.tools.udsl.generator.Generator
import br.com.opussoftware.udsl.model.EnvironmentSpec
import groovy.text.StreamingTemplateEngine
import groovy.util.logging.Slf4j

@Slf4j
class K8SGenerator implements Generator {
	
	static String TEMPLATE_PREFIX = "/templates/k8s/"

	@Override
	public String getName() {
		return "kubernetes"
	}

	@Override
	public String getDescription() {
		return "Gerador de código Terraform para provisionamento de ambiente de microserviços";
	}

	@Override
	public String getVersion() {
		return "1.0"; // TODO: Usar a informação do git-info
	}

	@Override
	public String getAuthor() {
		return "PMS";
	}

	
	@Override
	public int generate(List<EnvironmentSpec> envSpec, ConfigObject config, File outputDir) {
		
		def generatedFiles = 0
		
		def helper = new K8SHelper()
		def tfHelper = new TFHelper()
		
		envSpec.each { env ->

			def ftb = new FileTreeBuilder(outputDir)
			
			ftb.dir(env.name) { 
				
				// entry point do módulo
				file("main.tf",K8SGenerator.processTemplate("main.tf.tpl",[
								env: env,
								config: config,
								k8s: helper,
								tf: tfHelper
								]))
							generatedFiles++
				
				// Gera um módulo para cada ingress pendurado no environment
				// Estes endpoint são os de entradas
				dir("ingress") {
					env.endpoints.each { ep ->
						dir(ep.name) {
							file("main.tf",K8SGenerator.processTemplate("ingress.tf.tpl",[
								env: env,
								endpoint: ep,
								config: config,
								k8s: helper
								]))
							generatedFiles++
						}
					}
				}

				// Para cada deployment crio um deployment em si e um
				// serviço
				env.deployments.each { deployment ->
					dir(deployment.name) {
												
						file("deployment.tf",K8SGenerator.processTemplate("deployment.tf.tpl",[							
							env: env,
							config: config,
							k8s: helper,
							deployment:deployment]))
						
						file("service.tf",K8SGenerator.processTemplate("service.tf.tpl",[
							env: envSpec,
							config: config,
							k8s: helper,
							deployment:deployment]))
					}
				}
			}
			
		}
		
		return generatedFiles
	}
	
	
	protected static byte[] processTemplate(String template, Map bindings) {
		
		log.info("Processando template: ${template}")
		
		Reader r= locateTemplate(template);
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
	
	protected static Reader locateTemplate(String tpl) {
		
		if ( !tpl.startsWith("/")) {
			tpl = "${TEMPLATE_PREFIX}${tpl}"
		}
		
		InputStream is = getClass().getResourceAsStream(tpl)
		if ( is == null ) {
			throw new IllegalArgumentException("Template não encontrado: ${tpl}")
		}
		return is.newReader()
		
	}
}
