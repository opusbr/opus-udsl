#
# AWS Provider configuration
#
<%
def dollar = '$'
%>


variable "aws_provider" {
	type = string
	description = "AWS provider to use. Supported values are: 'standard' ==> normal AWS access; 'localstack' ==> mocked AWS stack"
	default = "standard"
}

variable "aws_region" {
	type = string
	description = "AWS Region"
	default = "sa-east-1"
}


variable "localstack_host" {
	type = string
	description = "Localstack host address (ip or hostname)"
	default = "localhost"
}


provider "aws" {
	region                      = var.aws_region
	s3_force_path_style         = var.aws_provider != "standard"? true : false
	skip_credentials_validation = var.aws_provider != "standard"? true : false
	skip_metadata_api_check     = var.aws_provider != "standard"? true : false
	skip_requesting_account_id  = var.aws_provider != "standard"? true : false
	
	dynamic "endpoints" {
		for_each = var.aws_provider == "standard" ? [] : ["1"]
		content {
			apigateway     = "http://$dollar{var.localstack_host}:4567"
			cloudformation = "http://$dollar{var.localstack_host}:4581"
			cloudwatch     = "http://$dollar{var.localstack_host}:4582"
			dynamodb       = "http://$dollar{var.localstack_host}:4569"
			es             = "http://$dollar{var.localstack_host}:4578"
			firehose       = "http://$dollar{var.localstack_host}:4573"
			iam            = "http://$dollar{var.localstack_host}:4593"
			kinesis        = "http://$dollar{var.localstack_host}:4568"
			lambda         = "http://$dollar{var.localstack_host}:4574"
			route53        = "http://$dollar{var.localstack_host}:4580"
			redshift       = "http://$dollar{var.localstack_host}:4577"
			s3             = "http://$dollar{var.localstack_host}:4572"
			secretsmanager = "http://$dollar{var.localstack_host}:4584"
			ses            = "http://$dollar{var.localstack_host}:4579"
			sns            = "http://$dollar{var.localstack_host}:4575"
			sqs            = "http://$dollar{var.localstack_host}:4576"
			ssm            = "http://$dollar{var.localstack_host}:4583"
			stepfunctions  = "http://$dollar{var.localstack_host}:4585"
			sts            = "http://$dollar{var.localstack_host}:4592"
			ec2            = "http://$dollar{var.localstack_host}:4597"
		}		
	}  
}
  