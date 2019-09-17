/*
 * Exemplo de ingress
 */
 
apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  name: endereco.entrada.exemplo.com.br
  namespace: novos-canais
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
spec:
  rules:
  - host: endereco.entrada.exemplo.com.br
    http:
      paths:
      - backend:
          serviceName: ex-api-auditoria
          servicePort: 80
        path: /ex-api-auditoria
      - backend:
          serviceName: ex-api-novos-canais-home
          servicePort: 80
        path: /ex-api-novos-canais-home
      - backend:
          serviceName: ex-api-cliente
          servicePort: 80
        path: /ex-api-cliente
      - backend:
          serviceName: ex-api-fatura
          servicePort: 80
        path: /ex-api-fatura
      - backend:
          serviceName: ex-api-feedback
          servicePort: 80
        path: /ex-api-feedback
      - backend:
          serviceName: ex-api-parcelamento
          servicePort: 80
        path: /ex-api-parcelamento
  tls:
  - hosts:
    - endereco.entrada.exemplo.com.br
    secretName: exemplo.com.br 