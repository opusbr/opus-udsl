/*
 * Variáveis de entrada do módulo
 * Ambiente: Kubernetes
 *
 * ARQUIVO GERADO AUTOMATICAMENTE: NÃO EDITAR !!!
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
 	description = "Endpoint para gestão do RabitMQ"
 	default = "" 	
}

variable "rabbitmq_management_username" {
 	type = string
 	description = "Usuário administrativo a ser criado"
 	default = "guest" 	
}

variable "rabbitmq_management_password" {
 	type = string
 	description = "Senha do usuário administrativo a ser criado"
 	default = "guest" 	
}

 
<% if ( config.messaging.external ) { %>
variable "message_broker_address" {
	type = string
	description = "Endereço externo do servidor RabbitMQ"
	default = "rabbitmq.example.com"
}

<% } %>
 
 
 
