/*
 * Kubernetes Ingress module
 */


variable "ingress_name" {
    type = string
    description = "Ingress name. Defaults to a random name."
    default = "${k8s.ingressName(endpoint)}"
}

variable "ingress_host" {
    type = string
    description = "Ingress hostname"
    default = "${endpoint.name}"
}

<% if ( config?.security?.enabled?: false ) { %>
<% def security_provider = config?.security?.provider?: 'keycloak' %>
variable "keycloak_host" {
	type = string
	description = "Keycloak hostname"
	default = "${endpoint.name}"
}

<% } %>
	

