
# Microservices with Spring Cloud Netflix

A bare-bones microservices architecture implementation using Spring Cloud Netflix (Eureka Discovery Service, Zuul API Gateway, Ribbon Load Balancer, Feign REST Client, and Hystrix).

Note: most of the libraries used in this project are deprecated or in maintenance mode, and should be replaced by newer ones. Nevertheless, the main concepts remain the same.

![image](https://user-images.githubusercontent.com/25701657/187342958-ecf1e0a3-640b-4b5d-be86-aaa522b4177d.png)




## Contents of the project

- One Eureka Server instance, that acts as a Service Registry.

- One Zuul API Gateway instance.

- Two microservices that communicate with each other using Feign REST Client and Hystrix as a Circuit Breaker.

Diagram showing the architechture, with the different components and Spring Cloud libraries.

![image](https://user-images.githubusercontent.com/25701657/187344424-9a8b2585-475a-43fb-921e-fb0f452c4a8b.png)


##  Launch the components

- First, launch the Eureka Server instance. Eureka has to be deployed in the first place so that all the other services can register with it. After successful deployment, you should be able to access the Eureka dashboard on [http://localhost:8010/](http://localhost:8010/)

![image](https://user-images.githubusercontent.com/25701657/187342988-1405a2ba-6571-4041-912d-ecf380ca073f.png)



- You can then launch Zuul API Gateway and the two microservices (A and B) in any order. After successful deployment, you should be able to see them appear as registered instances on the Eureka dashboard.

![image](https://user-images.githubusercontent.com/25701657/187342999-d86d6d4c-c3a6-4d07-be48-bd6eae922b92.png)


## Test the API Gateway

Both microservices (microservice A and B) listen on random ports for requests, which they get assigned on start-up. You can access them through the API Gateway, which will query the Eureka Service Registry and get the corresponding service endpoint. Accessing these endpoints should result in both microservices printing the port they are currently listening on, and the port of the opposite microservice.

- Microservice A endpoint: [http://localhost:8011/microservicea/test/status/all](http://localhost:8011/microservicea/test/status) . Note that the URL is the concatenation of the Zuul URL (http://localhost:8011/) + the name of the target service + the resource path. It should print:

<img width="307" alt="image" src="https://user-images.githubusercontent.com/25701657/187344908-bfa10d1e-500b-4d5a-bfe4-a2121217f0c5.png">


- Microservice B endpoint: [http://localhost:8011/microserviceb/test/status/all](http://localhost:8011/microserviceb/test/status). Should print

<img width="307" alt="image" src="https://user-images.githubusercontent.com/25701657/187345129-2906a8e4-369b-4927-a99e-27283eddf3a6.png">





## Test the client-side load balancing

Ribbon, a client-side load balancer, is included by default in Zuul API Gateway. It is also integrated with Feign REST Client, which is being used by both microservices to communicate with each other. 

 - To test the Zuul API Gateway load balancing, after having initiated two or more instances of microservice A, execute the endpoint that prints the random port assigned to the web server for service A: http://localhost:8011/microservicea/test/status. It should print a different port each time. Zuul, by using Ribbon, is load balancing across the two instances, in a round-robin fashion.
- To test that Feign REST Client does the same, after having initiated two or more instances of microservice A, you can execute http://localhost:8011/microserviceb/test/status/all. Note that this endpoint, located in service B, will query service A using Feign. Because Feign client is integrated with Ribbon, the port assigned to A should change each time.
 
## Test the circuit breaker
The Feign Rest Client is configured to use Hystrix as a circuit breaker, a common pattern used in microservices architechture to avoid cascading failures. You can test this by executing http://localhost:8011/microserviceb/test/status/all after having stopped service A. You should see a fallback value returned instead of B´s port.

![image](https://user-images.githubusercontent.com/25701657/187344578-c2d036ab-a694-4a15-a377-29a858a331f6.png)


Because service A will be detected as being down when the number of consecutive failures crosses the configured threshold, the circuit breaker will trip, and for the duration of the timeout period, all attempts to invoke the remote service will fail immediately. In a real-world scenario, the fallback value would be something that makes more sense, maybe a cached value that is still considered valid.

## Some words on Eureka Server

In microservices based applications, services locations and the number of instances change dynamically, so communication between microservices becomes a challenge. One way to solve this problem is by using a Service Registry solution like Eureka. A Service Registry it´s basically a database of services, which can be queried by any service to find the location of any other- Eureka in particular is an implementation of Client-side Service Discovery: clients have to first get the registry from Eureka, and then call the target service (this is normally abstracted by using some Registry aware HTTP client).
On start-up, all Eureka Clients register themselves with the Eureka Service Registry, making themselves available for discovery. They also periodically send heart-beats to the Eureka Server to renew their registration, so that Eureka knows they are still alive. This is known as self-registrarion (other option would be having a 3rd party registration component). In this examples, both microservice applications (A and B) and Zuul API Gateway are annotated with the @EnableEurekaClient annotation, which marks them as clients of Eureka.

![image](https://user-images.githubusercontent.com/25701657/187344542-a3fc5dd3-c40d-4345-8b7d-7490521c2322.png)

