# Projeto do curso de Microserviço com Spring

## O que foi implementado
  * Configuração do servidor Eureka (Spring Cloud) para registro Service Discovery e Service Registry
  * Configuração de servidor Gateway para centralização da entrada de requisições para os microserviços e LoadBalancer, funcionado lado a lado com o Servidor Eureka
  * Configuração dos serviços Pagamentos e Pedidos para registro no servidor Eureka no application.yml
  * Configuração do instance-id dos clientes (Pagamentos e Pedidos) para funcionar o escalonamento vertical da aplicação com Load Balancer
  * Configuração do Flyway com migrações para cada microservico
  * Uso da biblioteca ModelMapper para mapeamento de DTO para Entidades
  * Utilização da arquitetura de software MVC, com a definição organizada por pasta das resposabilidades da classe
  * Criação de uma API REST para os microserviços de Pagamentos e Pedidos
  * Utilização do OpenFeign (Spring Cloud) para comunição assíncrona entre serviços de Pagamentos e Pedidos com acrescimo de fallback para atualização do estado de um Pedido
  * Configuração do resilience4j para ação de Circuit Breker na rota de confirmarPagamento no serviço de Pagamentos,
## Tecnologias Utilizadas
  * Spring Framework (Spring Cloud, Spring MVC, Spring Data, Spring Boot)
  * Java
  * Maven
  * Flyway
  * Resilience4j
  * SQL
  * GIT
