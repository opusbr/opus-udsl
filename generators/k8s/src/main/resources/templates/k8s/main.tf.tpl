/*
 * Defini��es do ambiente
 *
 * ARQUIVO GERADO AUTOMATICAMENTE: N�O EDITAR !!!
 */
<%
def dollar = '$'
%> 
 
provider "kubernetes" {}
 
 
//================================================================== Ingress
<% env.endpoints.each { ep -> %>
module "ingress_${tf.moduleName(ep.name)}" {
 	source = "./ingress/${ep.name}"
} 
  
<% } %>

//================================================================== Externals Endpoints
<% env.externalEndpoints.each { ep -> %>

// Endpoint: ${ep.name}
variable "external_${tf.moduleName(ep.name)}_target" {
    type = string
    description = "Endere�o f�sico do servi�o externo ${ep.name}"
    default = "${ep.target}" 
}

module "external_${tf.moduleName(ep.name)}" {
    source = "./external/${ep.name}"
    target_address = var.external_${tf.moduleName(ep.name)}_target
}

<% } %> 

 
//================================================================== Deployments
 
<% env.deployments.each { dep -> %>
module "${tf.moduleName(dep.name)}" {
  source = "./${dep.name}"
} 
<% } %>

<% if (!env.messageChannels.empty) { %>

//================================================================== Messaging

variable "management_endpoint" {
	type = string
	description = "Endpoint para configura��o do sistema de mensageria. Utilizar quando o TF n�o tiver acesso direto ao servidor"
	default = ""
}

module "messaging" {
	source = "./messaging/${config?.messaging?.provider?:'rabbitmq'}"
	rabbitmq_management_endpoint = var.management_endpoint
}

<% } %>


