/*
 * External endpoint: ${endpoint.name}
 */

variable "target_address" {
	type = string
	description = "Endere�o externo associado ao CNAME criado"
	default = "${endpoint.target}"
}
 
