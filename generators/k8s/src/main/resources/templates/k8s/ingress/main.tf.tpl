/*
 * Kubernetes Ingress module
 */



<% if ( config?.security?.enabled?: false ) { %>
<% def security_provider = config?.security?.provider?: 'keycloak' %>
variable "keycloak_host" {
	type = string
	description = "Hostname a ser utilizado para o keycloak"
	default = "${endpoint.name}"
}

<% } %>
	

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

                    path = "${route.path}"
                }
<% } %>
            }						
        }
		
<% if ( config?.security?.enabled?: false ) { %>
<% def security_provider = config?.security?.provider?: 'keycloak' %>
		rule {
			host = var.keycloak_host
			http {
				path {
				    backend {
					    service_name = "keycloak"
					    service_port = 8080
				    }
					
					path = "/"
			    }
			}
		}		
<% } %>


/*
    tls {
      secret_name = "tls-secret"
    }
*/    
  }
}

