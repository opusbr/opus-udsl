/*
 * Top-level variables
 */

<% env.externalEndpoints.each { ep -> %>
	 
 // Endpoint: ${ep.name}
 variable "external_${tf.moduleName(ep.name)}_target" {
	 type = string
	 description = "Endere�o f�sico do servi�o externo ${ep.name}"
	 default = "${ep.target}"
 }
 	 
<% } %>

<% if (!env.messageChannels.empty) { %>
	
variable "management_endpoint" {
	type = string
	description = "Endpoint para configura��o do sistema de mensageria. Utilizar quando o TF n�o tiver acesso direto ao servidor"
	default = ""
}

<% if ( "rabbitmq" == config?.messaging_provider ) { %>
variable "rabbitmq_management_username" {
	type = string
	description = "Usu�rio administrativo para o RabbitMQ"
	default = "admin"
}

variable "rabbitmq_management_password" {
	type = string
	description = "Senha do usu�rio administrativo do RabbitMQ"
	default = "admin"
}	
<% } %>
<%  if ( config.messaging.external ) { %>
variable "message_broker_address" {
	type = string
	description = "External broker address"
	default = "broker.example.com"
}
<% } %>


<% } %>



<% if ( config?.security?.enabled?: false ) { %>	
//================================================================== Security
variable "keycloak_host" {
	type = string
	description = "Keeycloak hostname"
	default = "keycloak.127.0.0.1.xip.io"
}
			
<% } %>



	


	

