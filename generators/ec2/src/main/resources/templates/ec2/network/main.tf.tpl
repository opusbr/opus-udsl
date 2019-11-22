#
# Generated code. DO NOT EDIT !!!
# This file will be overwritten every time you run the generator
# Any changes WILL BE LOST !!!
#
# Template: network/main.tf.tpl
#


resource "aws_vpc" "main" {
	cidr_block       = "10.0.0.0/16"
  
	tags = {
	  Name = "${env.name}"
	}
}