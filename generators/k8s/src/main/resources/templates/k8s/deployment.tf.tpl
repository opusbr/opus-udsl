/*
 * Deployment para o servico ${deployment.name}
 */

resource "kubernetes_deployment" "${deployment.name}" {

  metadata {
    name = "${k8s.validName(deployment.name)}"
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