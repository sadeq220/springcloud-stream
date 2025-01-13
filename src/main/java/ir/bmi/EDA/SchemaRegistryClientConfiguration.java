package ir.bmi.EDA;

import org.springframework.cache.CacheManager;
import org.springframework.cloud.stream.schema.registry.avro.AvroSchemaMessageConverter;
import org.springframework.cloud.stream.schema.registry.avro.AvroSchemaRegistryClientMessageConverter;
import org.springframework.cloud.stream.schema.registry.avro.AvroSchemaServiceManager;
import org.springframework.cloud.stream.schema.registry.client.EnableSchemaRegistryClient;
import org.springframework.cloud.stream.schema.registry.client.SchemaRegistryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
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
     *
     *  If you provide a custom converter, then the default AvroSchemaMessageConverter bean is not created.
     *  to use Avro MessageConverter that reads schemas from fileSystem
     */
    //@Bean
    public MessageConverter messageConverter(AvroSchemaServiceManager avroSchemaServiceManager){
        AvroSchemaMessageConverter avroSchemaMessageConverter = new AvroSchemaMessageConverter(MimeType.valueOf("avro/bytes"), avroSchemaServiceManager);
        avroSchemaMessageConverter.setSchemaLocation(new ClassPathResource("schemas/User.avro"));
        return avroSchemaMessageConverter;
    }

    /**
     * to use Avro MessageConverter that reads schemas from schema registry
     * to override the default one declare the following bean
     */
    public AvroSchemaRegistryClientMessageConverter avroSchemaRegistryClientMessageConverter(SchemaRegistryClient schemaRegistryClient,
                                                                                             CacheManager cacheManager, // spring abstract cache manager
                                                                                             AvroSchemaServiceManager avroSchemaServiceManager){ // to generate a Schema out of a POJO class
        AvroSchemaRegistryClientMessageConverter avroSchemaRegistryClientMessageConverter = new AvroSchemaRegistryClientMessageConverter(schemaRegistryClient, cacheManager, avroSchemaServiceManager);
        avroSchemaRegistryClientMessageConverter.setDynamicSchemaGenerationEnabled(true);//when message payload is not a GenericRecord, Schema should be generated out of a POJO class
        return avroSchemaRegistryClientMessageConverter;
    }
}
