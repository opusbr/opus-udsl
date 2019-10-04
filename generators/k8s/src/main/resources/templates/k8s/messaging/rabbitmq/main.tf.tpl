/*
 * Definição de recursos para criação de servidor de mensageria RabbitMQ
 * Ambiente: Kubernetes
 *
 * ARQUIVO GERADO AUTOMATICAMENTE: NÃO EDITAR !!!
 */
 
resource "kubernetes_service" "rabbitmq_server" {
  metadata {
    name = "rabbitmq"
  }
  
  spec {
  
    selector = {
      app = kubernetes_deployment.rabbitmq_deployment.metadata.0.labels.app
    }
    
    session_affinity = "ClientIP"
    port {
      name = "amqp"
      port = 5672
    }

    port {
      name = "management"
      port = 15672
    }

    type = "LoadBalancer"
  }
}

resource "kubernetes_deployment" "rabbitmq_deployment" {

  metadata {
    name = "rabbitmq"
    labels = {
      app = "rabbitmq"
    }
  }

  spec {
    replicas = 1

    selector {
      match_labels = {
        app = "rabbitmq"
      }
    }

    template {
      metadata {
        labels = {
	      app = "rabbitmq"
        }
      }

      spec {
        image_pull_secrets {
          name = "docker-config"
        }
      
        container {
          image = var.rabbitmq_image
          name  = "rabbitmq"   
        }

      }
    }
  }
}


 
 
