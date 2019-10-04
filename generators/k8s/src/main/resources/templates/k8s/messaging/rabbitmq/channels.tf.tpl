/*
 * Cria objetos no servidor de mensageria
 * Ambiente: Kubernetes
 *
 * ARQUIVO GERADO AUTOMATICAMENTE: NÃO EDITAR !!!
 */
<%
def dollar = '$'
%>


locals  {
  management_endpoint = var.rabbitmq_management_endpoint == "" ? "http://${dollar}{kubernetes_service.rabbitmq_server.load_balancer_ingress.0.ip}:15672" : var.rabbitmq_management_endpoint
}

provider "rabbitmq" {
	endpoint = local.management_endpoint
	username = "guest"
	password = "guest"
}


// Define o VHost
resource "rabbitmq_vhost" "main" {
  name = var.rabbitmq_vhost  
  depends_on = [ kubernetes_service.rabbitmq_server ]
}

<% env.messageChannels.each { channel -> %>
// Canal: ${channel.name} modo: ${channel.mode}

resource "rabbitmq_queue" "${tf.moduleName(channel.name)}_queue" {
  name  = "${channel.name}"
  vhost = rabbitmq_vhost.main.name

  settings {
    durable     = false
    auto_delete = false
  }
}

resource "rabbitmq_exchange" "${tf.moduleName(channel.name)}_exchange" {
  name  = "${channel.name}"
  vhost = rabbitmq_vhost.main.name

  settings {
    type        = "${channel.mode == "p2p"?"direct":"fanout"}"
    durable     = true
    auto_delete = false
  }
}

resource "rabbitmq_binding" "${tf.moduleName(channel.name)}_binding" {
  source           =  rabbitmq_exchange.${tf.moduleName(channel.name)}_exchange.name
  vhost = rabbitmq_vhost.main.name
  destination      = rabbitmq_queue.${tf.moduleName(channel.name)}_queue.name
  destination_type = "queue"
}


<% } %>

