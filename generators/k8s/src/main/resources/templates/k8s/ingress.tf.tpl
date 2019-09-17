/*
 * Kubernetes Ingress module
 */

provider "kubernetes" {}

variable "ingress_name" {
    type = string
    description = "Nome do objeto a ser criado no Kubernetes para o ingress. Se nao informado um nome aleatorio sera gerado"
    default = "${k8s.ingressName(endpoint)}"
}

variable "ingress_host" {
    type = string
    description = "Hostname a ser utilizado no ingress"
    default = "${endpoint.name}"
}

locals {
    iname = var.ingress_name == "" ? join("-",["ingress",sha1(uuid())]) : var.ingress_name
}

resource "kubernetes_ingress" "ingress" {

    metadata {
        name = local.iname
        annotations = map(
            "nginx.ingress.kubernetes.io/rewrite-target","/"
        )
    }

    spec {
        rule {
            http {
            
<%  endpoint.routes.each { route -> %>            
                path {
                    backend {
                        service_name = "${k8s.validName(route.deployment)}"
                        service_port = ${config?.deployment[route.deployment].port?:config?.deployment?.defaultPort?:8080}
                    }

                    path = "${route.path}/*"
                }
<% } %>
            }
        }

    tls {
      secret_name = "tls-secret"
    }
  }
}

