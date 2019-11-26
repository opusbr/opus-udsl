#
# Generated code. DO NOT EDIT !!!
# This file will be overwritten every time you run the generator
# Any changes WILL BE LOST !!!
#
# Template: messaging/rabbitmq/main.tf.tpl
#
<%
def dollar = '$'
%>


#
# Recupera AMI-ID da imagem RabbitMQ que vamos utilizar
#
data "aws_ami_ids" "rabbitmq" {
	owners = ["aws-marketplace"]
  
	filter {
	  name   = "name"
	  values = [var.rabbitmq_image]
	}
}

#
# Recupera VPCs em que o RabbitMQ será implantado
#
data "aws_security_groups" "rabbitmq_security_group" {
  filter {
	name   = "vpc-id"
	values = ["$dollar{var.vpc_id}"]
  }
}

data "aws_region" "current" {}

data "aws_availability_zones" "available" {
  state = "available"
}

#
# RabbitMQ launch template
#
resource "aws_launch_template" "tpl" {
  description = "RabbitMQ Launch Template"
  
  name_prefix   = "rabbitmq_ltpl"
  image_id      =  data.aws_ami_ids.rabbitmq.ids[0]
  instance_type = var.rabbitmq_instance_type  
}



resource "aws_autoscaling_group" "rabbitmq" {
  desired_capacity   = var.rabbitmq_autoscale_desired_capacity
  max_size           = var.rabbitmq_autoscale_max_size
  min_size           = var.rabbitmq_autoscale_min_size
  
  vpc_zone_identifier = [var.services_subnet_id]

  launch_template {
    id      = aws_launch_template.tpl.id
    version = var.rabbitmq_launch_template_version
  }
}