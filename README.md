### Event-Driven Architecture(EDA)   
Spring Cloud Stream is a framework for building message-driven microservice applications.    
Spring Cloud Stream uses `Spring Integration`  to provide connectivity to message brokers.    
It has the concepts of **persistent publish-subscribe semantics**, **consumer groups**, and **partitions**.    
It connects to Message Brokers(Messaging Middleware) through *Binders*, such as Kafka-binder, and RabbitMQ-binder.    

> The Binder abstraction makes it possible for a Spring Cloud Stream application to be flexible in how it connects to middleware.     
> For example, deployers can dynamically choose, at runtime, the mapping between the external destinations (such as the Kafka topics or RabbitMQ exchanges) and      
> inputs and outputs of the message handler (such as input parameter of the function and its return argument)

Spring Cloud Stream consumer groups are similar to and inspired by Kafka consumer groups.    
### Spring cloud Stream Programming Model
core concepts:     
- Binder
  - provides connectivity to message brokers
- Binding
    - Message-driven components(such as Function Beans)
- Message
    - base pattern from *Enterprise Integration Patterns*

### References
- [spring cloud stream doc](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html#spring-cloud-stream-overview-introducing)