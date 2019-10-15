/*
 * Vari�veis para o m�dulos Keycloak
 */
<% def dollar = '$' %> 
 
variable "keycloak_image" {
 	type = string
 	description = "Imagem Keycloak a ser utilizada"
 	default = "jboss/keycloak:7.0.0"
}

variable "keycloak_realm" {
 	type = string
 	description = "Arquivo contendo a defini��o do Realm inicial a ser criado no keycloak"
 	default = "${env.name}-realm.json"
}
 
variable "keycloak_admin_user" {
 	type = string
 	description = "Usu�rio administrativo inicial"
 	default = "admin"
}
 
variable "keycloak_admin_password" {
 	type = string
 	description = "Senha para usu�rio administrativo"
 	default = ""
}

 