package ir.bmi.EDA;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.binder.kafka.support.ConsumerConfigCustomizer;
import org.springframework.cloud.stream.binder.kafka.support.ProducerConfigCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.function.Consumer;
import java.util.function.Function;

@SpringBootApplication
/**
 * To understand the Spring cloud stream programming model, you should be familiar with the following core concepts:
 *
 * Destination Binders: Components responsible to provide integration with the external messaging systems.
 * Binder is responsible for data type conversion
 *
 * Bindings: Bridge between the external messaging systems and application provided Producers and Consumers of messages (created by the Destination Binders).
 *
 * Message: The canonical data structure used by producers and consumers to communicate with Destination Binders (and thus other applications via external messaging systems).
 */
public class EdaApplication {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
		SpringApplication.run(EdaApplication.class, args);
	}

	@Bean
	/**
	 * If you want advanced customization of consumer and producer configuration that is
	 * used for creating ConsumerFactory and ProducerFactory in Kafka
	 */
	public ConsumerConfigCustomizer kafkaConsumerConfigCustomizer(){
	return ((consumerProperties, bindingName, destination) -> {
		consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
		// consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
		consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");// read AUTO_OFFSET_RESET_DOC
		consumerProperties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,3);// to make sure we don't ran out of memory
		consumerProperties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,20_000);
	});
	}
	@Bean
	public ProducerConfigCustomizer kafkaProducerCustomizer(){
		return (producerProperties, bindingName, destination) -> {
			producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
			// producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");// spring cloud stream converts message body to byte[] already
			producerProperties.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,120_000);
			producerProperties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,true);
			producerProperties.put(ProducerConfig.ACKS_CONFIG,"all");
			producerProperties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,15_000);//kafka.timeout , default is 30s , wait time for a reply from the broker
		};
	}
	@Bean
	/**
	 * Message Handler with functional programming model
	 *
	 * Since Spring Cloud Stream v2.1, another alternative for defining stream handlers and sources is
	 * to use build-in support for Spring Cloud Function where
	 * they can be expressed as beans of type java.util.function.[Supplier/Function/Consumer].
	 *
	 * The functional programming model defaults to a simple convention when it comes to binding names
	 * By default the 'input' and 'output' binding names will be uppercase-in-0 and uppercase-out-0
	 *
	 * So if for example you would want to map the input of this function to a remote destination (e.g., topic, queue etc)
	 * called "my-topic" you would do so with the following property:
	 * --spring.cloud.stream.bindings.uppercase-in-0.destination=my-topic
	 *
	 * To specify which functional bean to bind to the external destination(s) exposed by the bindings,
	 * you must provide spring.cloud.function.definition property.
	 */
	public Function<UserMessage,String> uppercase(){
		return s->s.getName().toUpperCase();
	}

	@Bean
	public Function<String,String> wrapInQuotes(){
		return s -> String.format("\"%s\"",s);
	}

	/**
	 * It’s important to understand the difference between a writer schema (the application that wrote the message) and a reader schema (the receiving application)
	 * We call the schema used to write the data as the writer's schema, and the schema that the application expects the reader's schema.
	 * It is an error if the two schemas do not match.
	 * for example when both schemas are records then:
	 * 	if the reader's record schema has a field with no default value, and writer's schema does not have a field with the same name, an error is signalled.
	 */
	@Bean
	public Consumer<Message<UserMessage>> avroDeserializerConsumer(){
		return userMessage -> {
			logger.info("avro deserialized object:"+userMessage.getPayload()+" with content type: "+userMessage.getHeaders().get(MessageHeaders.CONTENT_TYPE));
		};
	}
}
