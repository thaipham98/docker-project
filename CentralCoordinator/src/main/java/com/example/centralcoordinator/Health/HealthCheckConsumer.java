package com.example.centralcoordinator.Health;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
/**
 * This class is responsible for processing health check messages from replicas.
 */
public class HealthCheckConsumer {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Instant> replicaLastMessageReceivedAt = new HashMap<>();
    private static final Logger logger = LogManager.getLogger(HealthCheckConsumer.class);


    @Value("${healthcheck.replicas}")
    private String[] replicas;

    @PostConstruct
    private void initReplicaLastMessageReceivedAt() {
        for (String replicaId : replicas) {
            logger.info(replicaId);
            replicaLastMessageReceivedAt.put(replicaId, null);
        }
    }

    @KafkaListener(topics = "#{T(java.util.Arrays).asList('${healthcheck.topics}'.split(','))}", groupId = "central_coordinator")
    public void processHealthCheckMessage(HealthCheckPayload payload) {
        //HealthCheckPayload payload = objectMapper.readValue(jsonPayload, HealthCheckPayload.class);
        String replicaId = payload.getReplicaId();
        replicaLastMessageReceivedAt.put(replicaId, Instant.now());
        // Process the health check payload, e.g., update replica status, trigger alerts, etc.
        logger.info("Received health check payload: " + payload);
    }

    @Scheduled(fixedRate = 5000) // Check every 5 seconds
    public void checkForMissedMessages() {
        Duration timeoutThreshold = Duration.ofSeconds(10); // Set the desired timeout threshold

        for (Map.Entry<String, Instant> entry : replicaLastMessageReceivedAt.entrySet()) {
            String replicaId = entry.getKey();
            Instant lastMessageReceivedAt = entry.getValue();

            if (lastMessageReceivedAt == null) {
                logger.info(replicaId + " is down");
                continue;
            }

            Duration timeSinceLastMessage = Duration.between(lastMessageReceivedAt, Instant.now());

            if (timeSinceLastMessage.compareTo(timeoutThreshold) > 0) {
                logger.info("ALERT: No health check message received from replica " + replicaId + " in the last " + timeSinceLastMessage.getSeconds() + " seconds.");
            }
        }
    }
}
