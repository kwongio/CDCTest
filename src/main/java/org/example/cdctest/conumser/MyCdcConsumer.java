package org.example.cdctest.conumser;


import lombok.extern.slf4j.Slf4j;
import org.example.cdctest.model.Topic;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyCdcConsumer {

    private final CustomObjectMapper objectMapper = new CustomObjectMapper();
    private int retryCount = 0;

    @KafkaListener(groupId = "CDC-TEST", topics = Topic.MY_CDC_TOPIC, containerFactory = "batchFactory")
    public void listen(String message) {
        log.info("retryCount: {}", retryCount++);
        throw new RuntimeException();
    }
}
