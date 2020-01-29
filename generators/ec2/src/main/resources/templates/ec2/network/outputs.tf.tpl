#
# Generated code. DO NOT EDIT !!!
# This file will be overwritten every time you run the generator
# Any changes WILL BE LOST !!!
#
# Template: network/outputs.tf.tpl
#

output "vpc_id" {
	value = local.vpc_id
	description = "Generated VPC id"
}

output "vpc_arn" {
	value = local.vpc_arn
	description = "Generated VPC ARN"
}

output "services_subnet_id" {
	value = aws_subnet.services.id
	description = "Generated VPC Subnet ID for Services"
}

output "services_subnet_arn" {
	value = aws_subnet.services.arn
	description = "Generated VPC Subnet ARN for Services"
}


output "ingress_subnet_id" {
	value = aws_subnet.ingress[*].id
	description = "Generated Ingress VPC Subnet Id"
}

output "ingress_subnet_arn" {
	value = aws_subnet.ingress[*].arn
	description = "Generated Ingress VPC Subnect ARN"
}

