/*
 * Módulo de segurança utilizando Keycloak
 * ARQUIVO GERADO AUTOMATICAMENTE: NÃO EDITAR !!! 
 */
<% def dollar = '$' %>


resource "random_password" "password" {
	length = 8
	special = true
	override_special = "_%@"
}

locals {
	
	kk_user = var.keycloak_admin_user
	kk_pass = var.keycloak_admin_password == "" ?random_password.password.result:var.keycloak_admin_password
	realm_file_exists = fileexists(var.keycloak_realm)
	realm_list  = local.realm_file_exists?[1]:[]
	realm_count = local.realm_file_exists?1:0
}

  

resource "kubernetes_deployment" "keycloak" {

  metadata {
    name = "keycloak"
    labels = {
      app = "keycloak"
    }
  }
  
  spec {
    selector {
      match_labels = {
        app = "keycloak"
      }
    }
    
    
    template {
      metadata {
        labels = {
	      app = "keycloak"
        }
      }

      spec {
        image_pull_secrets {
          name = "docker-config"
        }
    
        container {
          image = var.keycloak_image
          name  = "keycloak" 
          
          env {
            name = "KEYCLOAK_USER"
            value = local.kk_user
          }
          
          env {
            name = "KEYCLOAK_PASSWORD"
            value = local.kk_pass
          }
          
          
          env {
          	name = "PROXY_ADDRESS_FORWARDING"
          	value = "true"
          }
          
          dynamic "env" {
		    for_each = local.realm_list
			content {
				name = "KEYCLOAK_IMPORT"
				value = "/tmp/config/${dollar}{var.keycloak_realm}-config.json"
		    }
          } 
		  
		  dynamic "volume_mount" {
		      for_each = local.realm_list
			  content {
				  name = "realm-config-volume"
				  mount_path = "/tmp/config"	
			  }
		  }
          
          readiness_probe {
          	initial_delay_seconds = 10
      		success_threshold = 2
          	tcp_socket {
          		port = 8080
          	}
          }
		     
        }
		
		# ConfigMap com o realm
		dynamic "volume" {
			for_each = local.realm_list
			content {
				name = "realm-config-volume"
				config_map { 
					name = kubernetes_config_map.realm_config.metadata.0.name
				}
			}		
		}
	  }
	}
  }
}

##
## 
resource "kubernetes_service" "keycloak" {
	metadata {
	  name = "keycloak"
	}
	
	spec {
	
	  selector = {
		app = "keycloak"
	  }
	  
	  session_affinity = "ClientIP"
	  port {
		port = 8080
	  }
  
	  //type = "LoadBalancer"
	}
}
  


resource "kubernetes_config_map" "realm_config" {
	count = local.realm_count
	metadata {
	  name = "realm-config"
	}
  
	data = {
	  "${dollar}{var.keycloak_realm}-config.json" = "${dollar}{file(var.keycloak_realm)}"
	}
}
