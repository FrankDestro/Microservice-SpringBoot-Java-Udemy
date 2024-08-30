# Microservice SpringBoot Java Udemy
Curso de Microservices Udemy:

https://www.udemy.com/course/microservices-do-0-a-gcp-com-spring-boot-kubernetes-e-docker/learn/lecture/25443416#content

Artigos 
Microservices - a definition of this new architectural term.

https://martinfowler.com/articles/microservices.html

https://medium.com/introducao-a-arquitetura-de-microservicos/introdu%C3%A7%C3%A3o-a-microsservi%C3%A7os-25378269e6f9

## Conceitos 
* 1 - Spring Cloud Configuration;
* 2 - Spring Boot Actuator;
* 3 - OpenFeign;
* 4 - Service Discovery e Service Registry com Eureka;
* 5 - Load Balancing com Eureka, Feign e Spring Cloud LoadBalancer;
* 6 - API Gateway e RouteLocator com Spring Cloud Gateway;
* 7 - Circuit Breaker com Resilience4j;
* 8 - Configuraremos o Swagger OpenAPI nos microsserviços;
* 9 - Distributed Tracing com Docker, Zipkin, Eureka e Sleuth;
* 10 - Dockerização, entrega contínua com Github Actions e muito mais.


## Spring Cloud Configuration

O Spring Cloud Config Server é um componente do Spring Cloud que permite a centralização e gestão de configurações de aplicações em ambientes distribuídos, como em arquiteturas de microservices. Ele fornece um servidor centralizado onde as configurações de todas as aplicações e ambientes são armazenadas e podem ser acessadas dinamicamente.


## Spring Cloud Actuator 

O Spring Boot Actuator é um projeto do Spring Boot que fornece uma série de funcionalidades para monitoramento e gestão de aplicações em produção. Ele inclui endpoints que permitem acessar informações sobre o estado da aplicação, como métricas, detalhes de configuração, informações de saúde (health checks), logs e muito mais.

![image](https://github.com/user-attachments/assets/68c35d47-8916-41a3-bb2b-9f2f9173e7bd)


## Spring Cloud LoadBalance

O spring-cloud-starter-loadbalancer é uma biblioteca do Spring Cloud que fornece funcionalidades para realizar balanceamento de carga de forma programática.
Ele oferece uma maneira fácil de integrar balanceamento de carga em aplicativos Spring que precisam se comunicar com serviços distribuídos

![image](https://github.com/user-attachments/assets/ab2a9567-8d5f-4e81-9b6e-b747f2a93200)


## RouteLocator

"RouteLocator" é uma interface utilizada para definir e localizar rotas. Essas rotas mapeiam caminhos de URL a serviços ou endpoints específicos dentro de uma aplicação distribuída. O RouteLocator permite configurar regras de roteamento dinâmicas, como direcionar uma requisição para diferentes microserviços baseados em parâmetros específicos, headers, ou outras características da requisição.

```js
@Configuration
public class ApiGatewayConfiguration {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/get")
                                     .filters(f -> f
                                         .addRequestHeader("Hello", "World")
                                         .addRequestParameter("Hello", "World"))
                        .uri("http://httpbin.org:80"))
                .route(p -> p.path("/cambio-service/**")
                        .uri("lb://cambio-service"))
                .route(p -> p.path("/books/**")
                        .uri("lb://book-service"))
                .build();
    }
}

```

## Circuit Breaker (Resilience4J)

O Circuit Breaker é um padrão de design usado em sistemas distribuídos, como microservices, para aumentar a resiliência e a estabilidade dos sistemas. Ele atua como um "disjuntor" em chamadas de serviços externos (APIs, bancos de dados, etc.), interrompendo temporariamente as tentativas de conexão a um serviço que está com falhas repetidas.

Como funciona:
Closed (Fechado): No estado inicial, o circuito está "fechado", ou seja, as chamadas de serviço ocorrem normalmente. Se as chamadas falham repetidamente, o Circuit Breaker muda para o estado "Open".

Open (Aberto): No estado "aberto", as chamadas são bloqueadas imediatamente, sem tentar acessar o serviço problemático. Esse estado permanece por um tempo configurado (timeout), após o qual o Circuit Breaker muda para o estado "Half-Open".

Half-Open (Meio-Aberto): Neste estado, o Circuit Breaker permite que algumas chamadas passem para testar se o serviço voltou a funcionar. Se essas chamadas forem bem-sucedidas, o Circuit Breaker volta ao estado "Closed". Se falharem, retorna ao estado "Open".

![image](https://github.com/user-attachments/assets/ddf978dd-91d0-486a-8dc5-5f982cc65569)

references : https://digitalvarys.com/what-is-circuit-breaker-design-pattern/ 
              https://resilience4j.readme.io/docs/circuitbreaker
