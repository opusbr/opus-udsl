#
# Generated code. DO NOT EDIT !!!
# This file will be overwritten every time you run the generator
# Any changes WILL BE LOST !!!
#
# Template: network/main.tf.tpl
#
<%
def dollar='$'
%>

# Availability zones
data "aws_availability_zones" "available" {
  state = "available"
}


# Main VPC
#resource "aws_vpc" "main" {
#	cidr_block = var.cidr_block
#
#	tags = {
#	  Name = "${env.name}"
#	}
#}

data "aws_vpc" "main" {
	default = true
}

locals {
	vpc_id = data.aws_vpc.main.id
	vpc_cidr = data.aws_vpc.main.cidr_block
	vpc_arn = data.aws_vpc.main.arn
}



#
# Internet Gateway
#
#resource "aws_internet_gateway" "gw" {
#	vpc_id = aws_vpc.main.id
#	
#	tags = {
#	  Name = "${env.name}"
#	}
#}


# Services Subnet. This subnet will be used to place
# all application deployments
resource "aws_subnet" "services" {	
	cidr_block = cidrsubnet(local.vpc_cidr,8,10)
	vpc_id = local.vpc_id
	
	tags = {
		Name = "${env.name}-services"
	}  
}

# Ingress Subnet. Used to attach LBs
resource "aws_subnet" "ingress" {
	count = length(data.aws_availability_zones.available.names)
	cidr_block = cidrsubnet(local.vpc_cidr,8, 11 + count.index)
	vpc_id = local.vpc_id
	availability_zone = data.aws_availability_zones.available.names[count.index]

	map_public_ip_on_launch = true
	
	tags = {
		Name = "${env.name}-ingress-$dollar{count.index}"
	}
}

# Routing stuff
#resource "aws_route_table" "r" {
#    vpc_id = aws_vpc.main.id
#  
#    route {
#	  cidr_block = "0.0.0.0/0"
#      gateway_id = aws_internet_gateway.gw.id
#    }
#    
#    tags = {
#	  Name = "${env.name}-ingress-route"
#	}
#}

# Ingress route table
#resource "aws_route_table_association" "a1" {
#	count = length(data.aws_availability_zones.available.names)	
#	subnet_id      = aws_subnet.ingress[count.index].id
#	route_table_id = aws_route_table.r.id
#}
