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
 
