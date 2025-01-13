package ir.bmi.EDA;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.cloud.stream.schema.registry.client.SchemaRegistryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class SampleController {
    private final StreamBridge streamBridge;
    private final SchemaRegistryClient schemaRegistryClient;

    public SampleController(StreamBridge streamBridge, SchemaRegistryClient schemaRegistryClient) {
        this.streamBridge = streamBridge;
        this.schemaRegistryClient = schemaRegistryClient;
    }
    @PostMapping("/kafka/publish")
    public Mono<ResponseEntity<String>> publishToKafka(@RequestBody UserMessage userMessage){
        Message<UserMessage> message = MessageBuilder.withPayload(userMessage)
                .setHeader(KafkaHeaders.KEY, "testingKey")
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE)
                .build();
        boolean explicitBinding = streamBridge.send("OutputExplicitBinding", message);
        return Mono.defer(()->{
            if (explicitBinding)
                return Mono.just(ResponseEntity.ok("published to Kafka!"));
            else
                return Mono.just(ResponseEntity.badRequest().body("failed to Publish to Kafka!"));
        });
    }
    @PostMapping("/kafka/avro-publish")
    public Mono<ResponseEntity<String>> publishAvroFormatToKafka(@RequestBody UserMessage userMessage){
        Message<UserMessage> message = MessageBuilder.withPayload(userMessage)
                .setHeader(KafkaHeaders.KEY, "testingKey")
                .setHeader(MessageHeaders.CONTENT_TYPE, "application/*+avro")
                .build();
        boolean avroOutputBinding = streamBridge.send("AvroOutputBinding", message);
        return Mono.defer(()->{
            if (avroOutputBinding)
                return Mono.just(ResponseEntity.ok("published to Kafka!"));
            else
                return Mono.just(ResponseEntity.badRequest().body("failed to Publish to Kafka!"));
        });
    }
    @PostMapping("/v2/kafka/avro-publish")
    public Mono<ResponseEntity<String>> publishEnrichedDataAvroFormatToKafka(@RequestBody NewUserMessage newUserMessage){
        Message<NewUserMessage> message = MessageBuilder.withPayload(newUserMessage)
                .setHeader(KafkaHeaders.KEY, "testingKey")
                .setHeader(MessageHeaders.CONTENT_TYPE, "application/*+avro")
                .build();
        boolean avroOutputBinding = streamBridge.send("AvroOutputBinding", message);
        return Mono.defer(()->{
            if (avroOutputBinding)
                return Mono.just(ResponseEntity.ok("published to Kafka!"));
            else
                return Mono.just(ResponseEntity.badRequest().body("failed to Publish to Kafka!"));
        });
    }
}
