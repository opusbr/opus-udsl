#
# Generated code. DO NOT EDIT !!!
# This file will be overwritten every time you run the generator
# Any changes WILL BE LOST !!!
#
# Template: deployment/outputs.tf.tpl
#

output "autoscaling_group_id" {
	value = aws_autoscaling_group.deployment.id
	description = "Generated Autoscaling Group Id"
}

output "autoscaling_group_arn" {
	value = aws_autoscaling_group.deployment.arn
	description = "Generated Autoscaling Group ARN"
}


