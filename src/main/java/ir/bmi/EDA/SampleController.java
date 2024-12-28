package ir.bmi.EDA;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
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
    public Mono<ResponseEntity<String>> publishToKafka(@RequestBody String body){
        boolean explicitBinding = streamBridge.send("OutputExplicitBinding", body);
        return Mono.defer(()->{
            if (explicitBinding)
                return Mono.just(ResponseEntity.ok("published to Kafka!"));
            else
                return Mono.just(ResponseEntity.badRequest().body("failed to Publish to Kafka!"));
        });
    }
}
