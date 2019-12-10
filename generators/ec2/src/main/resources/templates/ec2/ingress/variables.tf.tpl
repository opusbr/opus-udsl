#
# Generated code. DO NOT EDIT !!!
# This file will be overwritten every time you run the generator
# Any changes WILL BE LOST !!!
#
# Template: ingress/variables.tf.tpl
#

variable "vpc_id" {
	type = string
	description = "VPC id where we'll create the RabbitMQ instance"
}


variable "subnet_id" {
	type = string
	description = "VPC subnet to attach to the ingress LB"
}


#
# Valid values are described in the folloing link
# https://docs.aws.amazon.com/elasticloadbalancing/latest/application/create-https-listener.html#describe-ssl-policies
#
variable "ssl_policy" {
	type = string
	description = "Named SSL policy to use. Please check AWS docs for information on valid values"
	default = "ELBSecurityPolicy-2016-08"
}

variable "certificate_arn" {
	type = string
	description = "ARN of the certificate to use for the ingress LB. If not informed, we'll create a dummy self-signed certificate"
	default = ""
}