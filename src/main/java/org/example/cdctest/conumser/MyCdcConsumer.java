package org.example.cdctest.conumser;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.cdctest.model.MyCdcMessage;
import org.example.cdctest.model.Topic;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MyCdcConsumer {

    private final CustomObjectMapper objectMapper = new CustomObjectMapper();

    @KafkaListener(groupId = "CDC-TEST", topics = Topic.MY_CDC_TOPIC)
    public void listen(String message) throws JsonProcessingException {
        MyCdcMessage myCdcMessage = objectMapper.readValue(message, MyCdcMessage.class);
        System.out.println("[Cdc Consumer] " + myCdcMessage.getOperationType() + " Message arrived! (id: " + myCdcMessage.getId() + ") - " + myCdcMessage.getPayload());
    }
}
