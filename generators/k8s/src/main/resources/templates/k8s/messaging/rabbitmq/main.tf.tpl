/*
 * Definição de recursos para criação de servidor de mensageria RabbitMQ
 * Ambiente: Kubernetes
 *
 * ARQUIVO GERADO AUTOMATICAMENTE: NÃO EDITAR !!!
 */
<% def dollar = '$' %>
<% if ( !config.messaging.external ) { %>

// Internal RabbitMQ service
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
          
          env { 
          		name = "RABBITMQ_DEFAULT_USER"
          		value = var.rabbitmq_management_username
          }
          
          env { 
          		name = "RABBITMQ_DEFAULT_PASS"
          		value = var.rabbitmq_management_password
          }          	
          
          readiness_probe {
          	initial_delay_seconds = 10
      		success_threshold = 2
          	tcp_socket {
          		port = 15672
          	}
          }   
        }
      }
    }
  }
}

<% } %>

<% 
if ( config.messaging.external ) {
%>
// External RabbitMQ Service
 
resource "kubernetes_service" "rabbitmq_server" {
  metadata {
    name = "rabbitmq_server"
  }
  spec {
    type = "ExternalName"
    external_name = var.message_broker_address
  }
}

<%
}
%>

 
 
