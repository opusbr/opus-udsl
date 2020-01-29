#
# Generated code. DO NOT EDIT !!!
# This file will be overwritten every time you run the generator
# Any changes WILL BE LOST !!!
#
# Template: ingress/main.tf.tpl
#
<%
def dollar='$'
%>


#
# LB security group
#
resource "aws_security_group" "ingress" {
  name        = "allow_tls"
  description = "Allow TLS inbound traffic"
  vpc_id      = var.vpc_id

<% if (config.ingress.https.enabled ) { %>  
  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
	cidr_blocks     = ["0.0.0.0/0"]
  }
<% } %>  
  
<% if (config.ingress.http.enabled ) { %>
  ingress {
	  from_port   = 80
	  to_port     = 80
	  protocol    = "tcp"
	  cidr_blocks     = ["0.0.0.0/0"]
  }
  <% } %>

  egress {
	from_port   = 0
	to_port     = 0
	protocol    = "-1"
	cidr_blocks = ["0.0.0.0/0"]
  }
  
  tags = {
    Name = "allow_http"
  }
}


#
# S3 Bucket for logs
#
data "aws_elb_service_account" "main" {}



resource "aws_s3_bucket" "ingress_logs" {
	acl = "log-delivery-write"
	bucket = "${ec2.awsName(env.name)}-access-logs"
	force_destroy = true

	tags = {
		Environment = "${env.name}"
		Name = "ingress_logs"
	}
	
	policy = <<POLICY
{
  "Id": "Policy",
  "Version": "2012-10-17",
  "Statement": [
	{
	  "Action": [
		"s3:PutObject"
	  ],
	  "Effect": "Allow",
	  "Resource": "arn:aws:s3:::${ec2.awsName(env.name)}-access-logs/*",
	  "Principal": {
		"AWS": [
		  "$dollar{data.aws_elb_service_account.main.arn}"
		]
	  }
	}
  ]
}
POLICY
}


#
# Key pair to use with a the self-signed cert
#
# Note: ELB Listeners do not support ECDSA
resource "tls_private_key" "pk" {
	algorithm   = "RSA"
	rsa_bits = 2048
}

<%
env.endpoints.each { ep ->	
  def epSuffix = tf.moduleName(ep.name)
%>

#
# Self-signed certificate for  ${ep.name} endpoint
#
resource "tls_self_signed_cert" "cert_${epSuffix}" {
  key_algorithm   = "RSA"
  private_key_pem = tls_private_key.pk.private_key_pem

  subject {
	common_name  = "${ep.name}"
	organization = "Self Signed, Inc"
  }

  validity_period_hours = 24*365

  allowed_uses = [
	"key_encipherment",
	"digital_signature",
	"server_auth",
  ]
}

<% if( config.ingress.https.enabled ) { %>
#
# AWS Certificate for ${ep.name} endpoint
#
resource "aws_acm_certificate" "cert_${epSuffix}" {
	private_key      = tls_private_key.pk.private_key_pem
	certificate_body = tls_self_signed_cert.cert_${epSuffix}.cert_pem
	
	tags = {
		Environment = "${env.name}"
		Name = "${ep.name}"
	}
	 
}
<% } %>


#
# Application LB for ${ep.name} endpoint
#
resource "aws_lb" "ingress_${epSuffix}" {
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.ingress.id]
  subnets            = var.subnet_ids

  enable_deletion_protection = false

  access_logs {
	bucket  = aws_s3_bucket.ingress_logs.bucket
	prefix  = "logs/${epSuffix}"
	enabled = true
  }

  tags = {
   Environment = "${env.name}"
   Endpoint = "${ep.name}"
  }
  
<% if (config.ingress.https.enabled) { %>  
  depends_on = [
	  aws_acm_certificate.cert_${epSuffix}
  ]
<% } %>  
}

#
# LB Listener for ${ep.name} endpoint
#
<% if (config.ingress.https.enabled) { %>

resource "aws_lb_listener" "listener_${epSuffix}_https" {
	load_balancer_arn = aws_lb.ingress_${epSuffix}.arn
	port              = "443"
	protocol          = "HTTPS"
	ssl_policy        = var.ssl_policy
	certificate_arn   = aws_acm_certificate.cert_${epSuffix}.arn
  
	default_action {
		type = "fixed-response"

		fixed_response {
			content_type = "text/plain"
			message_body = "Not found"
			status_code  = "404"
		}
	}	
}
<% } %>

<% if (config.ingress.http.enabled) { %>
resource "aws_lb_listener" "listener_${epSuffix}_http" {
	load_balancer_arn = aws_lb.ingress_${epSuffix}.arn
	port              = "80"
	protocol          = "HTTP"
  
	default_action {
		type = "fixed-response"

		fixed_response {
			content_type = "text/plain"
			message_body = "Not found"
			status_code  = "404"
		}
	}
}
<% } %>

  

#
# Ingress Routes for endpoint ${ep.name}
#
<%
def routeCounter = 0
ep.routes.each { route ->
	def routeName = tf.moduleName(route.deployment)
	routeCounter++
%>

<% if (config.ingress.https.enabled) { %>
resource "aws_lb_listener_rule" "sroute_${epSuffix}_${routeName}${routeCounter}" {
    listener_arn = aws_lb_listener.listener_${epSuffix}_https.arn
	action {
		type = "forward"
		target_group_arn = aws_lb_target_group.tg_${routeName}.arn
	}
	
	condition {
		path_pattern {
			values = ["${route.path}"]
		}
	}
}
<% } %>

<% if (config.ingress.http.enabled) { %>
resource "aws_lb_listener_rule" "route_${epSuffix}_${routeName}${routeCounter}" {
	listener_arn = aws_lb_listener.listener_${epSuffix}_http.arn
	action {
		type = "forward"
		target_group_arn = aws_lb_target_group.tg_${routeName}.arn
	}
	
	condition {
		path_pattern {
			values = ["${route.path}","${route.path}/*"]
		}
	}
}
<% } %>


<%} /*end: each route */%>
<%} /*end: each env */%>

#
# Target Groups
#
<%
ec2.targetGroupsForEnvironment(env).each { tg ->
%>
resource "aws_lb_target_group" "$tg" {
  port = ${config?.deployment?.port?:80}
  protocol = "HTTP"
  vpc_id = var.vpc_id
  
}

<%
}
%>



