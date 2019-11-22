#
# Generated code. DO NOT EDIT !!!
# This file will be overwritten every time you run the generator
# Any changes WILL BE LOST !!!
#
# Template: messaging/rabbitmq/variables.tf.tpl
#

<%
def dollar = '$'
%>
 

variable "rabbitmq_image" {
	 type = string
	 description = "Imagem AMI RabbitMQ a ser utilizada"
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

