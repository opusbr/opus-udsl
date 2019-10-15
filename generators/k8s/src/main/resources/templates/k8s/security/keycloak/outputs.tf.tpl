/*
 * Vari�veis de sa�da do m�dulo
 */
 
output "keycloak_admin_user" {
	value = var.keycloak_admin_user
	description = "Usu�rio administrativo do Keycloak"
	sensitive = true
}

output "keycloak_admin_password" {
	value = var.keycloak_admin_password
	description = "Senha do usu�rio administrativo do Keycloak"
	sensitive = true
}