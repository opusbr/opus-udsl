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
	 description = "RabbitMQ AMI image name filter used to run AMI. The created instance will use the most recent AMI that matches this filter."
	 default = "bitnami-rabbitmq-3.8.*-linux*"
}

variable "rabbitmq_instance_type" {
	type = string
	description = "Instance type to use for RabbitMQ"
	default = "t3.micro"
}

variable "vpc_id" {
	type = string
	description = "VPC id where we'll create the RabbitMQ instance"
}

variable "services_subnet_id" {
	type = string
	description = "VPC Subnet where we'll create the RabbitMQ instance"
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


variable "rabbitmq_launch_template_version" {
	 type = string
	 description = "Launch template version to use"
	 default = "${dollar}Default"
}


variable "rabbitmq_autoscale_desired_capacity" {
	type = number
	description = "Autoscale Group desired capacity"
	default = 1
}

variable "rabbitmq_autoscale_min_size" {
	type = number
	description = "Autoscale Group minimum size"
	default = 1
}

variable "rabbitmq_autoscale_max_size" {
	type = number
	description = "Autoscale Group maximum size"
	default = 1
}
