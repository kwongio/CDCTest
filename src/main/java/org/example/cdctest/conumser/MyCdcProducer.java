package org.example.cdctest.conumser;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.example.cdctest.model.MyCdcMessage;
import org.example.cdctest.model.Topic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MyCdcProducer {

    CustomObjectMapper objectMapper = new CustomObjectMapper();

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(MyCdcMessage message) throws JsonProcessingException {
        kafkaTemplate.send(
            Topic.MY_CDC_TOPIC,
            objectMapper.writeValueAsString(message)
        );
    }
}
