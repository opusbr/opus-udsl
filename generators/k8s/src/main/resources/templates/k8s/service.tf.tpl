/*
 * Definicao de servico para ${deployment.name}
 */

resource "kubernetes_service" "${deployment.name}" {
  metadata {
    name = "${k8s.validName(deployment.name)}"
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

