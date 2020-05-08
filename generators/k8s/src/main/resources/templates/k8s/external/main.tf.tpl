/*
 * External endpoint: ${endpoint.name}
 */

 
resource "kubernetes_service" "${endpoint.name}" {
  metadata {
    name = "${k8s.validName(endpoint.name)}"
    namespace = "${config?.deployment?.defaultNamespace?:'default'}"
  }
  spec {
    type = "ExternalName"
    external_name = var.target_address

  }
}