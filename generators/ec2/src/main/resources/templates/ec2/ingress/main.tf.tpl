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
resource "aws_security_group" "public_tls" {
  name        = "allow_tls"
  description = "Allow TLS inbound traffic"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
	cidr_blocks     = ["0.0.0.0/0"]
  }

  tags = {
    Name = "allow_all"
  }
}


#
# S3 Bucket for logs
#
resource "aws_s3_bucket" "ingress_logs" {
	acl = "private"

	tags = {
		Environment = "${env.name}"
		Name = "ingress_logs"
	}	
}


#
# Key pair to use with a the self-signed cert
#
resource "tls_private_key" "pk" {
	algorithm   = "ECDSA"
	ecdsa_curve = "P384"
}

<%
env.endpoints.each { ep ->	
  def epSuffix = tf.moduleName(ep.name)
%>

#
# Self-signed certificate for  ${ep.name} endpoint
#
resource "tls_self_signed_cert" "cert_${epSuffix}" {
  key_algorithm   = "ECDSA"
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

#
# AWS Certificate for ${ep.name} endpoint
#
resource "aws_acm_certificate" "cert_${epSuffix}" {
	private_key      = tls_private_key.pk.private_key_pem
	certificate_body = tls_self_signed_cert.cert_${epSuffix}.cert_pem
}


#
# Application LB for ${ep.name} endpoint
#
resource "aws_lb" "ingress_${epSuffix}" {
  name_prefix        = "${ec2.awsName(env.name)}"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.public_tls.id]
  subnets            = [var.subnet_id]

  enable_deletion_protection = true

  access_logs {
	bucket  = aws_s3_bucket.ingress_logs.bucket
	prefix  = "${env.name}/logs/${epSuffix}"
	enabled = true
  }

  tags = {
   Environment = "${env.name}"
   Endpoint = "${ep.name}"
  }
}

#
# LB Listener for ${ep.name} endpoint
#
resource "aws_lb_listener" "listener_${epSuffix}" {
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

#
# Ingress Routes for endpoint ${ep.name}
#
<%
def routeCounter = 0
ep.routes.each { route ->
	def routeName = tf.moduleName(route.deployment)
	routeCounter++
%>

resource "aws_lb_listener_rule" "route_${epSuffix}_${routeName}${routeCounter}" {
    listener_arn = aws_lb_listener.listener_${epSuffix}.arn
	action {
		type = "forward"
		target_group_arn = aws_lb_target_group.tg_${routeName}.arn
	}
	
	condition {
		field = "path-pattern"
		values = ["${route.path}"]
	}
}

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



