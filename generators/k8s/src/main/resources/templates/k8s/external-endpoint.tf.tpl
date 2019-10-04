/*
 * External endpoint: ${endpoint.name}
 */

variable "target_address" {
	type = string
	description = "Endereço externo associado ao CNAME criado"
	default = "${endpoint.target}"
}
 
 
resource "kubernetes_service" "${endpoint.name}" {
  metadata {
    name = "${k8s.validName(endpoint.name)}"
  }
  spec {
    type = "ExternalName"
    external_name = var.target_address

  }
}