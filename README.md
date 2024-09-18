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

## Open Feign 

Realizar a comunicação entre API´s 

```js
@Component
@FeignClient(name = "hr-worker",
        path = "/workers")
public interface WorkerFeignClient {

    @GetMapping(value = "/{id}")
    ResponseEntity<Worker> findById(@PathVariable Long id);

}
```

## Eureka Server 

O Eureka Server é uma parte fundamental da arquitetura de microservices, especialmente quando se utiliza o padrão de registro e descoberta de serviços. Ele é uma aplicação do Spring Cloud Netflix que atua como um servidor de registro de serviços. Em um ambiente de microservices, onde há muitos serviços sendo executados e se comunicando entre si, é importante ter uma maneira de descobrir dinamicamente onde esses serviços estão localizados na rede.

O Eureka Server permite que os serviços se registrem nele, informando seu nome e endereço de rede. Assim, outros serviços podem consultar o Eureka Server para descobrir onde um serviço específico está localizado. Isso é especialmente útil em ambientes em nuvem, onde os serviços podem ser escalados dinamicamente e podem ser movidos entre diferentes máquinas ou instâncias.

Em resumo, o Eureka Server fornece um registro centralizado de todos os serviços disponíveis na arquitetura de microservices e permite que outros serviços encontrem e se comuniquem com esses serviços de forma dinâmica e eficiente.


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

### Circuit Breaker - @Retry

A anotação @Retry é usada para definir uma política de tentativa automática quando um método falha devido a uma exceção.
Basicamente, ela permite que o método seja reexecutado um número específico de vezes antes de falhar definitivamente. Se todas as tentativas falharem, um método de fallback pode ser chamado.

```java
@GetMapping("/foo-bar")
    @Retry(name = "foo-bar")
    public String fooBar() {
       looger.info("Request to foo-bar is received!");
       var response =  new RestTemplate().getForEntity("http://localhost:8080/foo-bar", String.class);
       return response.getBody();
    }
```

application.yml

```yml
resilience4j:
  retry:
    instances:
      foo-bar:
        maxAttempts: 5
      default:
        maxAttempts: 5
        waitDuration: 1s
        enableExponentialBackoff: true
```
### Circuit Breaker - FallbackMethod 

Defini um método alternativo caso a falha venha a ocorrer. Deve-ser adicionar uma exception como argumento no fallbackMethod

```java
 @GetMapping("/foo-bar")
    @Retry(name = "foo-bar", fallbackMethod = "fallbackMethod")
    public String fooBar() {
       looger.info("Request to foo-bar is received!");
       var response =  new RestTemplate().getForEntity("http://localhost:8080/foo-bar", String.class);
       return response.getBody();
    }

    public String fallbackMethod(Exception e) {
        return "fallbackMethod foo-bar!!!";
    }
```

### CircuitBreaker - @CircuitBreaker

A anotação @CircuitBreaker implementa o padrão de design "Circuit Breaker". Um Circuit Breaker monitora as falhas em um método e pode abrir o circuito para impedir chamadas subsequentes ao método quando muitas falhas ocorrem em um curto período de tempo. Enquanto o circuito está aberto, as chamadas ao método falham automaticamente, e um método de fallback pode ser chamado. Depois de um tempo, o circuito pode ser fechado novamente, permitindo que novas chamadas ao método sejam feitas.

```java
    @GetMapping("/foo-bar")
    @CircuitBreaker(name = "default", fallbackMethod = "fallbackMethod")
    public String fooBar() {
       looger.info("Request to foo-bar is received!");
       var response =  new RestTemplate().getForEntity("http://localhost:8080/foo-bar", String.class);
       return response.getBody();
    }

    public String fallbackMethod(Exception e) {
        return "fallbackMethod foo-bar!!!";
    }
}

```

### Diferenças Principais: @Retry / @CircuitBreaker
* @Retry: Foca em reexecutar o método várias vezes antes de considerar que ele falhou definitivamente.

* @CircuitBreaker: Monitora falhas para abrir o circuito após muitas falhas seguidas, evitando novas chamadas ao método até que ele "feche" novamente.
Ambas as anotações podem ser usadas juntas, dependendo do cenário. Por exemplo, você pode querer várias tentativas (@Retry) antes de o circuito abrir (@CircuitBreaker).

### CircuitBreaker - @RateLimiter

O RateLimiter é usado para limitar o número de chamadas que podem ser feitas a um serviço em um determinado período de tempo, protegendo contra sobrecargas e garantindo que o serviço não seja bombardeado com requisições.
Isso é útil quando se quer controlar o fluxo de requisições para evitar atingir limites de API ou proteger recursos internos.

```ruby
    @GetMapping("/foo-bar")
    @RateLimiter(name = "foo-bar-rate")
    public String fooBar() {
       looger.info("Request to foo-bar is received!");
        return "Foo-Bar!!!";
    }
```

### CircuitBreaker - @Bulkhead

O Bulkhead permite que você limite o número de chamadas simultâneas a um serviço, criando "compartimentos" que isolam falhas e impedem que um subsistema sobrecarregado afete todo o sistema.
É especialmente útil em microserviços onde você deseja evitar que um serviço monopolize todos os recursos.

```ruby
    @GetMapping("/foo-bar")
    @Bulkhead(name = "foo-bar-bk")
    public String fooBar() {
       looger.info("Request to foo-bar is received!");
        return "Foo-Bar!!!";
    }
```

## Swagger OpenAPI

Documentação de API. 

### Swagger documentation api 
http://localhost:8080/v3/api-docs

### Swagger UI
http://localhost:8080/v3/swagger-ui.html 


### Dependencia pom.xml

```java
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.2.0</version>
        </dependency>
```

### Classe para definir as configurações da OpenApi Swagger

```ruby
@OpenAPIDefinition(info =
@Info(title = "Book Service API",
        version = "V1",
        description = "Documentation of Book Service API"))
@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .components(new Components())
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Book Service API")
                        .version("V1")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")
                        )
                );
    }
}
```
### application.yml 

```yml
springdoc:
  swagger-ui:
    operationsSorter: method
    path: /swagger-ui.html
  api-docs:
    path: /book-service/v3/api-docs
```

## Configuração do Swagger em uma API Gateway em um ambiente de microservce. 

### Classe para mapear as rotas 

Classe que mapeia todas as rotas e agrupa em uma lista para gerar uma documentação de API de todos os microserviços.
ela pega as informações definidas nas rotas no application.yml filtrando todas as rotas que contém -service e agrupa retornando uma lista.   

```java
@Configuration
public class OpenApiConfiguraiton {

    @Bean
    @Lazy(value = false)
    public List<GroupedOpenApi> apis (SwaggerUiConfigParameters config, RouteDefinitionLocator locator) {

        List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
        definitions.stream()
                .filter(routeDefinition ->
                        routeDefinition.getId()
                                .matches(".*-service"))
                .forEach(routeDefinition -> {
                    String name = routeDefinition.getId();
                    config.addGroup(name);
                    GroupedOpenApi.builder()
                            .pathsToMatch("/" + name + "/**")
                            .group(name).build();
                });
        return new ArrayList<>();
    }
}
```

### Configuração de rotas e swagger application.yml 

```css
    cloud:
      gateway:
        discovery:
          locator:
            enabled: true
        routes:
          - id: cambio-service
            uri: lb://cambio-service
            predicates:
              - name: Path
                args:
                  pattern: /cambio-service/**
          - id: book-service
            uri: lb://book-service
            predicates:
              - name: Path
                args:
                  pattern: /book-service/**
          - id: openapi
            uri: lb://api-gateway
            predicates:
              - name: Path
                args:
                  pattern: /v3/api-docs/**
            filters:
              - RewritePath=/v3/api-docs/(?<path>.*), /$\{path}/v3/api-docs
```

### Dockerização, entrega contínua com Github Actions e muito mais.


