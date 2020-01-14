package br.com.opussoftware.tools.udsl.generator.k8s

import br.com.opussoftware.tools.udsl.generator.AbstractGenerator

class K8SGenerator extends AbstractGenerator {

	@Override
	public String getName() {
		return "k8s";
	}

	@Override
	public String getDescription() {
		return "Gerador de c�digo Terraform para provisionamento de ambiente de microservi�os em cluster Kubernetes";
	}

	@Override
	public String getVersion() {
		return "1.0"; // TODO: Usar a informa��o do git-info
	}

	@Override
	public String getAuthor() {
		return "PMS";
	}
	
	
}
