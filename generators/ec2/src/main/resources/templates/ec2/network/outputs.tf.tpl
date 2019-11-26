#
# Generated code. DO NOT EDIT !!!
# This file will be overwritten every time you run the generator
# Any changes WILL BE LOST !!!
#
# Template: network/outputs.tf.tpl
#

output "vpc_id" {
	value = aws_vpc.main.id
	description = "Generated VPC id"
}

output "vpc_arn" {
	value = aws_vpc.main.arn
	description = "Generated VPC ARN"
}

output "services_subnet_id" {
	value = aws_subnet.services.id
	description = "Generated VPC Subnet for Services"
}

output "services_subnet_arn" {
	value = aws_subnet.services.id
	description = "Generated VPC Subnet for Services"
}

