package ir.bmi.EDA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.binder.kafka.support.ConsumerConfigCustomizer;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
/**
 * To understand the Spring cloud stream programming model, you should be familiar with the following core concepts:
 *
 * Destination Binders: Components responsible to provide integration with the external messaging systems.
 *
 * Bindings: Bridge between the external messaging systems and application provided Producers and Consumers of messages (created by the Destination Binders).
 *
 * Message: The canonical data structure used by producers and consumers to communicate with Destination Binders (and thus other applications via external messaging systems).
 */
public class EdaApplication {

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

	});
	}
	@Bean
	/**
	 * Since Spring Cloud Stream v2.1, another alternative for defining stream handlers and sources is
	 * to use build-in support for Spring Cloud Function where
	 * they can be expressed as beans of type java.util.function.[Supplier/Function/Consumer].
	 *
	 * The functional programming model defaults to a simple convention when it comes to binding names
	 * By default the 'input' and 'output' binding names will be upperCase-in-0 and upperCase-out-0
	 */
	public Function<String,String> upperCase(){
		return s->s.toUpperCase();
	}
}
