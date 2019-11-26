#
# Generated code. DO NOT EDIT !!!
# This file will be overwritten every time you run the generator
# Any changes WILL BE LOST !!!
#
# Template: network/main.tf.tpl
#

# Main VPC
resource "aws_vpc" "main" {
	cidr_block = var.cidr_block
  
	tags = {
	  Name = "${env.name}"
	}
}

# Services Subnet. This subnet will be used to place
# all application deployments
resource "aws_subnet" "services" {
	cidr_block = cidrsubnet(aws_vpc.main.cidr_block,8,1)
	vpc_id = aws_vpc.main.id
}