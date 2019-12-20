#
# Generated code. DO NOT EDIT !!!
# This file will be overwritten every time you run the generator
# Any changes WILL BE LOST !!!
#
# Template: deployment/variables.tf.tpl
#
<%
def dollar = '$'
%>


variable "vpc_id" {
	type = string
	description = "VPC id where we'll create this deployment"
}

variable "services_subnet_id" {
	type = string
	description = "VPC Subnet Ids where we'll create this deployment"
}

variable "ami_name" {
	type = string
	description = "AMI name that we'll use for this deployment"
}

variable "ami_owner_ids" {
	type = list(string)
	description = "List of AMI Owner IDS and/or aliases for the image that we'll use for this deployment"
	default = ["aws-workplace","amazon"]
}


variable "instance_type" {
	type = string
	description = "Instance type to use for this deployment"
	default = "t3.micro"
}

variable "autoscale_min_size" {
	type = number
	description = "Autoscale Group minimum size"
	default = -1
}

variable "autoscale_max_size" {
	type = number
	description = "Autoscale Group maximum size"
	default = -1
}

variable "autoscale_desired_capacity" {
	type = number
	description = "Autoscale Group desired capacity"
	default = -1
}

variable "launch_template_version" {
	type = string
	description = "Launch template version to use"
	default = "${dollar}Default"
}

variable "lb_target_group_arns" {
	type = list(string)
	description = "ARNs of the LB target groups to attach the autoscaling group of this deployment to"
	default = []
}



