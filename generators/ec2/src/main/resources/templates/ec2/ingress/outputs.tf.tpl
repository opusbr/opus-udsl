#
# Generated code. DO NOT EDIT !!!
# This file will be overwritten every time you run the generator
# Any changes WILL BE LOST !!!
#
# Template: ingress/outputs.tf.tpl
#
<%
def dollar = '$'
%>

<% 
ec2.targetGroupsForEnvironment(env).each { tg ->
%>


output "${tg}_id" {
  value = aws_lb_target_group.${tg}.id
  description = "LB Target group ID for ${tg}"
}

output "${tg}_arn" {
  value = aws_lb_target_group.${tg}.arn
  description = "LB Target Group ARN for ${tg}"
}
<%} /* each: endpoint */ %>
