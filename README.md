# microservicios
Proof of concept - aplicación Java con arquitectura de microservicios con Spring Boot y Spring Cloud.

Este repositorio contiene 5 aplicaciones distintas con configuración mínima para demostrar una arquitectura de microservicios básica.
Los componentes son:

eurekaDiscoveryService: Eureka Server o también llamado Discovery Service, una aplicación que permite implementar Client-Side service discovery (https://microservices.io/patterns/client-side-discovery.html). Básicamente, contiene la información sobre todos los microservicios en un service registry, lo cúal permite que estos se puedan localizar fácilmente entre ellos. Los microservicios se registran en el servidor de Eureka, por lo que el registro contiene la IP y puerto en que se encuentran corriendo todas las instancias. Esto evita tener que hard codear hostname y puertos.

configServer: Spring Cloud Config Server, permite tener una configuración centralizada de la cual pueden leer todos los microservicios. El server se encuentra configurado para tomar datos de un application.properties de un repositorio de git privado. Los microservicios pueden leer propiedades directamente de ahí, facilitando el mantenimiento de los archivos de configuración. Las propiedades definidas en el server tienen mayor prioridad que las que se definen en los application.properties de los microservicios. 

zuulApiGateway: Zuul funciona como el punto de entrada de la aplicación, realizando el ruteo dinámico a los distintos microservicios. Se integra con Ribbon y proporciona load 
balancing.

microservicios: microservicioA y microservicioB, se trata de dos microservicios con una API REST "/test/status" que imprime el puerto en el cual se encuentra escuchando el microservicio y adicionalmente, el puerto en el que está escuchando el microservicio complementario.

La comunicación entre microservicios puede realizarse de forma sincrónica o asincrónica. En este caso se trata de una comunicación sincrónica utilizando Feign, un cliente HTTP declarativo. Además, los microservicios deben proporcionar alguna forma de reaccionar ante la eventual falta de disponibilidad del microservicio con el cúal se comunican. En este caso, se puede utilizar Hystrix Circuit Breaker, una librería que facilita la implementación del patrón Circuit Breaker (entre otros) (https://microservices.io/patterns/reliability/circuit-breaker.html). 
