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

### Content Type negotiation
In Spring Cloud Stream, message transformation is accomplished with an     
`org.springframework.messaging.converter.MessageConverter`

In order to select the appropriate MessageConverter, the framework needs `contentType`.    
Spring Cloud Stream provides three mechanisms to define contentType (in order of precedence):    
- Message Header
  - use **org.springframework.messaging.MessageHeaders.CONTENT_TYPE** and **org.springframework.util.MimeTypeUtils**
- Binding contentType
  - use **--spring.cloud.stream.bindings.<bindingName>.content-type**
- Default *application/json*

### Schema evolution support
A schema defines the structure of message data. It defines allowed data types, their format, and relationships.     
A schema acts as a blueprint for data.    
Schema is also referred to as a `data contract`.    

The scenario involves two producers, one using an old schema and the other using an evolved schema,     
with a consumer capable of processing messages from both schema versions.    

> The spring-cloud-stream-schema module contains two types of message converters that can be used for Apache Avro serialization:     
>  - converters using the class information of the serialized/deserialized objects, or a schema with a location known at startup;    
>  - converters using a schema registry - they locate the schemas at runtime, as well as dynamically registering new schemas as domain objects evolve.

A schema registry lets you store schema information in a textual format (**typically JSON**) and      
makes that information accessible to various applications that need it to receive and send data in **binary format**.    
There are 2 options for schema registry:    
- Spring cloud Standalone Schema Registry Server 
- Confluent Schema Registry


### References
- [spring cloud stream doc](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html#spring-cloud-stream-overview-introducing)
- [confluent schema registry](https://docs.confluent.io/platform/current/schema-registry/index.html)
- [spring cloud stream schema evolution official samples](https://github.com/spring-cloud/spring-cloud-stream-samples/tree/main/spring-cloud-stream-schema-registry-integration)
- [spring cloud stream schema module doc](https://docs.spring.io/spring-cloud-stream/docs/Elmhurst.M1/reference/html/schema-evolution.html)
- [spring cloud schema registry doc](https://docs.spring.io/spring-cloud-schema-registry/docs/current/reference/html/spring-cloud-schema-registry.html)
