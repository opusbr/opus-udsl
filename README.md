![CI](https://github.com/opusbr/opus-udsl/workflows/CI/badge.svg)

## O que é o Opus uDSL Generator ?

É um gerador de código que tem como objetivo **acelerar o desenvolvimentos de sistemas com arquitetura baseada
em microserviços**.

## Por que utilizar o uDSL Generator ?

  * Simplifica e padroniza a geração de artefatos necessários para adoção da prática de "infraestrutura como código"
  * Facilita o reuso de componentes e melhores práticas entre projetos  que adotam arquiteturas de microserviços
  * Separação clara entre a definição da arquitetura de serviços de aplicação e componentes de suporte necessários
    para execução da aplicação em determinada plataforma

## Features

  * Permite descrever em alto nível uma aplicação baseada em microserviços por meio de 
    uma DSL (Domain-Specific Language)
  * Uma mesma descrição de sistema pode ser reutilizada para diferentes plataforma-alvo, simplesmente
    alterando o arquivo de parametrização utilizado na geração
  * Arquitetura extensível, permitindo a adição de novos módulos de geração voltados para novas plataformas-alvo
    de forma descritiva
  * Geradores "out-of-the-box" disponíveis:
     * Terraform + Kubernetes: Gera artefatos Terraform para deploy da aplicação em um cluster Kubernetes padrão
     * Terraform + EC2: Gera artefatos Terraform para deploy da aplicação em instâncias EC2 da AWS.

## Como usar o Opus uDSL Generator ?

  1. Descreva os macro-componentes de sua arquitetura utilizando a linguagem uDSL
  2. Escolha a plataforma alvo para execução do sistema (EC2, K8S, etc) e crie o arquivo
     de parametrização específico
  3. Execute o gerador, passando os arquivos contendo o modelo e parâmetros.
  4. Utilize os artefatos gerados para efetuar o deploy da aplicação na plataforma-alvo.
