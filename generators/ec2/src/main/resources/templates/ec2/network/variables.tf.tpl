#
# Generated code. DO NOT EDIT !!!
# This file will be overwritten every time you run the generator
# Any changes WILL BE LOST !!!
#
# Template: network/variables.tf.tpl
#
<%
def dollar = '$'
%>


variable "cidr_block" {
	type = string
	description = "CIDR Block for application VPC"
	default = "10.0.0.0/16"
}

