package com.food.delivery.app.common.kafka.consumer;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "kafka-consumer-data")
public class KafkaConsumerData {
    private String bootstrapServers;
    private String schemaRegistryUrl;
    private String schemaRegistryUrlKey;
    private String keyDeserializer;
    private String valueDeserializer;
    private String specificAvroReaderKey;
    private String specificAvroReader;
}
