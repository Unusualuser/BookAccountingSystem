package ru.example.kafka;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Producer {
    public static final String RESET_COLOR_CONSOLE = "\033[0m";
    public static final String PURPLE_COLOR_CONSOLE = "\033[0;35m";
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final static Logger LOGGER = Logger.getLogger(Producer.class);

    @Value("${kafka.test.topic}")
    private String topicName;

    @Scheduled(cron = "*/10 * * * * *")
    public void sendMessage() {
        LocalDateTime currDateTime = LocalDateTime.now();

        LOGGER.info("-------------------------------Sending message to topic test-topic...-------------------------------");
        kafkaTemplate.send(topicName, currDateTime.toString());
    }
}
