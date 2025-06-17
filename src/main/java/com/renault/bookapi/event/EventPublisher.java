package com.renault.bookapi.event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Connection;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
public class EventPublisher implements CommandLineRunner {

    private final Connection natsConnection;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EventPublisher(Connection natsConnection) {
        this.natsConnection = natsConnection;
    }

    @Override
    public void run(String... args) throws Exception {
        var resource = new ClassPathResource("author-news.json");
        List<Map<String, Object>> newsList = objectMapper.readValue(
                resource.getInputStream(),
                new TypeReference<>() {}
        );

        for (Map<String, Object> newsItem : newsList) {
            newsItem.put("date", Instant.now().toString());
            byte[] messageBytes = objectMapper.writeValueAsBytes(newsItem);
            natsConnection.publish("author.news", messageBytes);
        }
    }
}
