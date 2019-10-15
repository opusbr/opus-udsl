/*
 * Variáveis de saída do módulo
 */
 
output "keycloak_admin_user" {
	value = var.keycloak_admin_user
	description = "Usuário administrativo do Keycloak"
	sensitive = true
}

output "keycloak_admin_password" {
	value = var.keycloak_admin_password
	description = "Senha do usuário administrativo do Keycloak"
	sensitive = true
}