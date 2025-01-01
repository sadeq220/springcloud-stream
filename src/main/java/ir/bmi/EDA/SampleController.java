package ir.bmi.EDA;

import org.springframework.cloud.stream.function.StreamBridge;
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

    public SampleController(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
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
}
