#
# Generated code. DO NOT EDIT !!!
# This file will be overwritten every time you run the generator
# Any changes WILL BE LOST !!!
#
# Template: network/main.tf.tpl
#

# Availability zones
data "aws_availability_zones" "available" {
  state = "available"
}


# Main VPC
resource "aws_vpc" "main" {
	cidr_block = var.cidr_block
  
	tags = {
	  Name = "${env.name}"
	}
}

#
# Internet Gateway
#
resource "aws_internet_gateway" "gw" {
	vpc_id = aws_vpc.main.id
  
	tags = {
	  Name = "${env.name}"
	}
}

# Services Subnet. This subnet will be used to place
# all application deployments
resource "aws_subnet" "services" {
	cidr_block = cidrsubnet(aws_vpc.main.cidr_block,8,0)
	vpc_id = aws_vpc.main.id
}

# Ingress Subnet. Used to attach LBs
resource "aws_subnet" "ingress" {
	count = length(data.aws_availability_zones.available.names)
	cidr_block = cidrsubnet(aws_vpc.main.cidr_block,8, 1 + count.index)
	vpc_id = aws_vpc.main.id
}
