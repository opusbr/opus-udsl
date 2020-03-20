/*
 * Definições do ambiente
 *
 * ARQUIVO GERADO AUTOMATICAMENTE: NÃO EDITAR !!!
 */
<%
def dollar = '$'
%> 
  
 
//================================================================== Ingress
<% env.endpoints.each { ep -> %>
module "ingress_${tf.moduleName(ep.name)}" {
 	source = "./modules/ingress/${ep.name}"
	
<% if ( config?.security?.enabled?: false ) { %>
	keycloak_host = var.keycloak_host
<% } %>
	 
} 
  
<% } %>

//================================================================== Externals Endpoints
<% env.externalEndpoints.each { ep -> %>

module "external_${tf.moduleName(ep.name)}" {
    source = "./modules/external/${ep.name}"
    target_address = var.external_${tf.moduleName(ep.name)}_target
}

<% } %> 

 
//================================================================== Deployments
 
<% env.deployments.each { dep -> %>
module "${tf.moduleName(dep.name)}" {
  source = "./modules/${dep.name}"
} 
<% } %>

<% if (!env.messageChannels.empty) { %>

//================================================================== Messaging

<%
def messaging_provider = config?.messaging?.provider?:'rabbitmq'
%>

	
module "messaging" {
	source = "./modules/messaging/${messaging_provider}"
<% if ( "rabbitmq" == messaging_provider ) { %>
	rabbitmq_management_endpoint = var.management_endpoint
	rabbitmq_management_username = var.rabbitmq_management_username
	rabbitmq_management_password = var.rabbitmq_management_password	
<% } %>

<% if ( config.messaging.external ) { %>
	message_broker_address = var.message_broker_address
<% } %>	
}

<% } /* if env.messageChannels.empty */ %>

<% if ( config?.security?.enabled?: false ) { %>
	
//================================================================== Security
		
<% def security_provider = config?.security?.provider?: 'keycloak' %>

module "security" {
	source = "./modules/security/${security_provider}"	
}

<% } %>


