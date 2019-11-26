#
# Generated code. DO NOT EDIT !!!
# This file will be overwritten every time you run the generator
# Any changes WILL BE LOST !!!
#
# Template: deployment/variables.tf.tpl
#

variable "vpc_id" {
	type = string
	description = "VPC id where we'll create the RabbitMQ instance"
}


variable "services_subnet_id" {
	type = string
	description = "VPC Subnet where we'll create the RabbitMQ instance"
}
