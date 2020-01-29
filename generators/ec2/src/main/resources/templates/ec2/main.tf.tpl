/*
 * Definições do ambiente
 *
 * ARQUIVO GERADO AUTOMATICAMENTE: NÃO EDITAR !!!
 */
<%
def dollar = '$'
%>

locals {
	vpc_id = module.network.vpc_id
	services_subnet_id = module.network.services_subnet_id
	ingress_subnet_id = module.network.ingress_subnet_id
}


//================================================================== VPCs
module "network" {
	source = "./network"
}


 
//================================================================== Ingress
variable "certificate_arn" {
	type = string
	description = "ARN of the certificate to use for the ingress LB. If not informed, we'll create a dummy self-signed certificate"
	default = ""
}
 
module "ingress" {
    source = "./ingress"
	vpc_id = local.vpc_id
	subnet_ids = local.ingress_subnet_id
	certificate_arn = var.certificate_arn
}
  
 
//================================================================== Deployments
 
<% 
env.deployments.each { dep ->
  def targetGroups = ec2.targetGroupsForDeployment(env,dep)
  def sep = ""
  def moduleName = ec2.moduleName(dep.name) 
%>
module "${moduleName}" {
  source = "./${dep.name}"
  vpc_id = local.vpc_id
  services_subnet_id = local.services_subnet_id
  ami_owner_ids = ["${ec2.amiName(config,dep.name).owner}"]
  ami_name = "${ec2.amiName(config,dep.name).name}"
  lb_target_group_arns = [
    <% targetGroups.each { tg -> %>
		${sep}module.ingress.${tg}_arn
		<% sep = "," %>
	<%}%>
  ]
}
<% } %>

<% if (!env.messageChannels.empty) { %>

//================================================================== Messaging

<%
def messaging_provider = config?.messaging?.provider?:'rabbitmq'
%>

variable "management_endpoint" {
	type = string
	description = "Endpoint para configuração do sistema de mensageria. Utilizar quando o TF não tiver acesso direto ao servidor"
	default = ""
}

<% if ( "rabbitmq" == messaging_provider ) { %>
variable "rabbitmq_management_username" {
	type = string
	description = "Usuário administrativo para o RabbitMQ"
	default = "admin"
}

variable "rabbitmq_management_password" {
	type = string
	description = "Senha do usuário administrativo do RabbitMQ"
	default = "admin"
}

<% } %>

<% if ( config?.security?.enabled?: false ) { %>
variable "keycloak_host" {
	type = string
	description = "Hostname para acesso ao keycloak"
	default = "keycloak.127.0.0.1.xip.io"
}
	
<% } %>



module "messaging" {
	source = "./messaging/${messaging_provider}"
	vpc_id = local.vpc_id
	services_subnet_id = local.services_subnet_id	

<% if ( "rabbitmq" == messaging_provider ) { %>
	rabbitmq_management_endpoint = var.management_endpoint
	rabbitmq_management_username = var.rabbitmq_management_username
	rabbitmq_management_password = var.rabbitmq_management_password
<% } %>

}

<% } /* if env.messageChannels.empty */ %>

<% if ( config?.security?.enabled?: false ) { %>
//================================================================== Messaging
<% def security_provider = config?.security?.provider?: 'keycloak' %>

module "security" {
	source = "./security/${security_provider}"
}

<% } %>


