package com.renault.bookapi.config;

import io.nats.client.Connection;
import io.nats.client.Nats;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Configuration
public class NatsConfig {

    private Connection natsConnection;

    @Bean
    public Connection natsConnection() throws IOException, InterruptedException {
        this.natsConnection = Nats.connect();
        return this.natsConnection;
    }

    @PreDestroy
    public void closeNatsConnection() throws InterruptedException {
        if (natsConnection != null && natsConnection.getStatus() == Connection.Status.CONNECTED) {
            try {
                natsConnection.flush(Duration.ofSeconds(2));
            } catch (InterruptedException | TimeoutException e) {
                Thread.currentThread().interrupt();
            }
            natsConnection.close();
        }
    }
}
