/*
 * Variáveis para o módulos Keycloak
 */
<% def dollar = '$' %> 
 
variable "keycloak_image" {
 	type = string
 	description = "Imagem Keycloak a ser utilizada"
 	default = "jboss/keycloak:7.0.0"
}

variable "keycloak_realm" {
 	type = string
 	description = "Arquivo contendo a definição do Realm inicial a ser criado no keycloak"
 	default = "${env.name}-realm.json"
}
 
variable "keycloak_admin_user" {
 	type = string
 	description = "Usuário administrativo inicial"
 	default = "admin"
}
 
variable "keycloak_admin_password" {
 	type = string
 	description = "Senha para usuário administrativo"
 	default = ""
}

 