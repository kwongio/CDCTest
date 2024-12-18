package org.example.cdctest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonContainerStoppingErrorHandler;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.ExponentialBackOff;

@Configuration
public class KafkaConfig {

    @Bean
    public DefaultKafkaProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.RETRIES_CONFIG, 10);
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 10000);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "CDC-TEST");
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

//    @Bean
//    @Primary
//    CommonErrorHandler errorHandler() {
//        CommonContainerStoppingErrorHandler cseh = new CommonContainerStoppingErrorHandler();
//        AtomicReference<Consumer<?, ?>> consumer2 = new AtomicReference<>();
//        AtomicReference<MessageListenerContainer> container2 = new AtomicReference<>();
//
//        return new DefaultErrorHandler((rec, ex) -> {
//            cseh.handleRemaining(ex, Collections.singletonList(rec), consumer2.get(), container2.get());
//        }, generateBackOff()) {
//
//            @Override
//            public void handleRemaining(
//                    Exception thrownException,
//                    List<ConsumerRecord<?, ?>> records,
//                    Consumer<?, ?> consumer,
//                    MessageListenerContainer container
//            ) {
//                consumer2.set(consumer);
//                container2.set(container);
//                super.handleRemaining(thrownException, records, consumer, container);
//            }
//        };
//    }

    //KafkaListner 어노테이션은 기본적으로 kafkaListenerContainerFactory 이름을 참고함

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> batchFactory() {
//        DefaultErrorHandler errorHandler = new DefaultErrorHandler(new FixedBackOff(1000L, 2L));
//        ExponentialBackOff exponentialBackOff = new ExponentialBackOff(1000L, 2L);
//        exponentialBackOff.setInitialInterval(10000);
//        new CommonContainerStoppingErrorHandler(); // 컨테이너 멈추기 가능
//        errorHandler.addNotRetryableExceptions(IllegalArgumentException.class);

        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        // DLQ에 넣기 때문에 producer도 있어야함
        ExponentialBackOff backOff = new ExponentialBackOff(1000, 2);
        DefaultErrorHandler commonErrorHandler = new DefaultErrorHandler(
                new DeadLetterPublishingRecoverer(kafkaTemplate()),
                backOff);
        factory.setCommonErrorHandler(commonErrorHandler);
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        factory.setBatchListener(true);
        return factory;
    }


//    private BackOff generateBackOff() {
//        ExponentialBackOff backOff = new ExponentialBackOff(1000, 2);
////        backOff.setMaxAttempts(1);
//        backOff.setMaxElapsedTime(10000);
//        return backOff;
//    }
}
