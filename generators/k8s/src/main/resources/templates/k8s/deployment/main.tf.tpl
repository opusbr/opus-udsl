/*
 * ${deployment.name} deployment resources
 */

resource "kubernetes_deployment" "${deployment.name}" {

  metadata {
    name = "${k8s.validName(deployment.name)}"
    namespace = "${config?.deployment?.defaultNamespace?:'default'}"
    labels = {
      app = "${deployment.name}"
    }
  }

  spec {
    replicas = ${config.deployment[deployment.name].replicas?:1}

    selector {
      match_labels = {
        app = "${deployment.name}"
      }
    }

    template {
      metadata {
        labels = {
	      app = "${deployment.name}"
        }
      }

      spec {
        image_pull_secrets {
          name = "docker-config"
        }
        
<% deployment.images.each { image -> %>      
        container {
          image = "${image.name}"
          name  = "${k8s.validName(image.name)}"   
        }
<% } %>
      }
    }
  }
}

resource "kubernetes_service" "${deployment.name}" {
	metadata {
	  name = "${k8s.validName(deployment.name)}"
    namespace = "${config?.deployment[deployment.name].namespace?:config?.deployment?.defaultNamespace?:'default'}"
	}
	
	spec {
	
	  selector = {
		app = "${deployment.name}"
	  }
	  
	  session_affinity = "ClientIP"
	  port {
		  port = ${config?.deployment[deployment.name].port?:config?.deployment?.defaultPort?:8080}
	  }
  
	  //type = "LoadBalancer"
	}
}
  