/*
 * Vari�veis de entrada do m�dulo
 * Ambiente: Kubernetes
 *
 * ARQUIVO GERADO AUTOMATICAMENTE: N�O EDITAR !!!
 */
<%
def dollar = '$'
%>
 

 variable "rabbitmq_image" {
 	type = string
 	description = "Imagem RabbitMQ a ser utilizada"
 	default = "rabbitmq:3-management"
 }
 
 variable "rabbitmq_vhost" {
 	type = string
 	description = "Nome do VHost a ser utilizado com o RabbitMQ"
 	default = "/"
 }
 
 
variable "rabbitmq_management_endpoint" {
 	type = string
 	description = "Endpoint para gest�o do RabitMQ"
 	default = "" 	
}

variable "rabbitmq_management_username" {
 	type = string
 	description = "Usu�rio administrativo a ser criado"
 	default = "guest" 	
}

variable "rabbitmq_management_password" {
 	type = string
 	description = "Senha do usu�rio administrativo a ser criado"
 	default = "guest" 	
}

 
<% if ( config.messaging.external ) { %>
variable "message_broker_address" {
	type = string
	description = "Endere�o externo do servidor RabbitMQ"
	default = "rabbitmq.example.com"
}

<% } %>
 
 
 
