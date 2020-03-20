/*
 * External endpoint: ${endpoint.name}
 */

variable "target_address" {
	type = string
	description = "Endereço externo associado ao CNAME criado"
	default = "${endpoint.target}"
}
 
