package ir.bmi.EDA;

import org.springframework.cloud.stream.schema.registry.avro.AvroSchemaMessageConverter;
import org.springframework.cloud.stream.schema.registry.avro.AvroSchemaServiceManager;
import org.springframework.cloud.stream.schema.registry.client.EnableSchemaRegistryClient;
import org.springframework.cloud.stream.schema.registry.client.SchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.util.MimeType;

/**
 * Spring Cloud Schema Registry (<a href="https://docs.spring.io/spring-cloud-schema-registry/docs/current/reference/html/spring-cloud-schema-registry.html#_schema_registry_client_properties">doc</a>)
 * The client-side abstraction for interacting with schema registry servers is the SchemaRegistryClient interface
 * By default MessageConverter caches the schema and registry client doesn't
 */
@Configuration
@EnableSchemaRegistryClient // provides default bean of type SchemaRegistryClient
public class SchemaRegistryClientConfiguration {

    /**
     * define it as a bean to override the default SchemaRegistryClient
     */
    public SchemaRegistryClient schemaRegistryClient(){
        return null;
    }

    /**
     * apache avro <a href="https://avro.apache.org/docs/1.7.6/spec.html">doc</a>
     * plus primitive types(null, boolean, int, long, float, double, bytes, string) apache avro supports six complex types(records, enums, arrays, maps, unions and fixed).
     * Avro specifies two serialization encodings: binary and JSON.
     */
    @Bean
    public MessageConverter messageConverter(AvroSchemaServiceManager avroSchemaServiceManager){
        return new AvroSchemaMessageConverter(MimeType.valueOf("avro/bytes"),avroSchemaServiceManager);
    }
}
