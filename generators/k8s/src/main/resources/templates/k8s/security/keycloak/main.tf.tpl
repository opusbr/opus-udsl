/*
 * M�dulo de seguran�a utilizando Keycloak
 * ARQUIVO GERADO AUTOMATICAMENTE: N�O EDITAR !!! 
 */
<% def dollar = '$' %>
provider "helm" {}

//
data "helm_repository" "kc_repo" {
    name = "kc_repo"
    url  = "https://codecentric.github.io/helm-charts"
}

resource "helm_release" "keycloak" {
	name = "keycloak"
	repository = data.helm_repository.kc_repo.metadata.0.name
    chart      = "codecentric/keycloak"
}