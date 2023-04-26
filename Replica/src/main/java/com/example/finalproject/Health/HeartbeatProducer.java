package com.example.finalproject.Health;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;

@Component
public class HeartbeatProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
    private final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();


    @Value("${replica.id}")
    private String replicaId;
    @Value("${healthcheck.topic}")
    private String healthCheckTopic;
    @Value("${replica.url}")
    private String replicaURL;

    @Scheduled(fixedRate = 10000)
    public void sendHeartbeat() {
        HealthCheckPayload payload = new HealthCheckPayload();
        payload.setReplicaId(replicaId);
        payload.setTimestamp(Instant.now());
        payload.setStatus("healthy");

        // Get CPU usage (in percentage)
        double cpuLoad = osBean.getSystemLoadAverage();
        payload.setCpuUsage(cpuLoad);

        // Get memory usage (in percentage)
        double usedMemory = memoryBean.getHeapMemoryUsage().getUsed();
        double maxMemory = memoryBean.getHeapMemoryUsage().getMax();
        double memoryUsage = (usedMemory / maxMemory) * 100;
        payload.setMemoryUsage(memoryUsage);

        // Measure response time for a representative request
        double responseTime = measureResponseTime();
        payload.setResponseTime(responseTime);


        try {
            String jsonPayload = objectMapper.writeValueAsString(payload);
            System.out.println("Sending heartbeat: " + jsonPayload);
            kafkaTemplate.send(healthCheckTopic, jsonPayload);
        } catch (JsonProcessingException e) {
            System.err.println("Failed to serialize health check payload: " + e.getMessage());
        }

    }

    private double measureResponseTime() {
        // Measure the response time for a representative request in your application
        // This depends on the specific services your application exposes
        // For example, if you're using RestTemplate or WebClient, you can time a request to your service
        double responseTime = 0.0;
        try {
            long start = System.currentTimeMillis();
            URL url = new URL(replicaURL); // Replace with the URL of your replica
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int statusCode = connection.getResponseCode();
            if (statusCode == 200) {
                long end = System.currentTimeMillis();
                responseTime = (end - start) / 1000.0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseTime;
    }
}
