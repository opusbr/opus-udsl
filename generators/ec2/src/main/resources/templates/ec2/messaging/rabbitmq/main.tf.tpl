#
# Generated code. DO NOT EDIT !!!
# This file will be overwritten every time you run the generator
# Any changes WILL BE LOST !!!
#
# Template: messaging/rabbitmq/main.tf.tpl
#

#
# Recupera AMI-ID da imagem RabbitMQ que vamos utilizar
#
data "aws_ami_ids" "rabbitmq" {
	owners = ["aws-marketplace"]
  
	filter {
	  name   = "name"
	  values = ["bitnami-rabbitmq-3.8.1-0-linux*"]
	}
}
