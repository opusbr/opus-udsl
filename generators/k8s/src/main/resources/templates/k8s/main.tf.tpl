/*
 * 
 */
 
 provider "kubernetes" {}
 
 // Ingress
 <% env.endpoints.each { ep -> %>
 module "ingress_${tf.moduleName(ep.name)}" {
 	source = "./ingress/${ep.name}"
 } 
  
 <% } %>
 
 // Deployments
 
<% env.deployments.each { dep -> %>
module "${tf.moduleName(dep.name)}" {
  source = "./${dep.name}"
} 
<% } %>
