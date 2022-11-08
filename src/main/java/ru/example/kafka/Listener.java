package ru.example.kafka;

import org.apache.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Listener {
    private final static Logger LOGGER = Logger.getLogger(Listener.class);

    @KafkaListener(topics = "${kafka.test.topic}")
    public void listenGroupTestGroup(String message) {
        LOGGER.info(String.format("-------------------------------Received Message in test-topic: %s-------------------------------", message));
    }
}
