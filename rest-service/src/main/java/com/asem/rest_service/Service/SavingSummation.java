package com.asem.rest_service.Service;

import com.asem.rest_service.Model.Summation;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class SavingSummation {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DistributionSummary kafkaLatencySummary;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SavingSummation.class);

    @Autowired
    public SavingSummation(MeterRegistry registry) {
        this.kafkaLatencySummary = DistributionSummary
                .builder("kafka_message_latency_ms")
                .description("Time a message stayed in Kafka before being consumed")
                .baseUnit("milliseconds")
                .register(registry);
    }

    @Value("${output.file-path}")
    private String filePath;

    @KafkaListener(topics = "${topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        try {
            Summation msg = objectMapper.readValue(message, Summation.class);

            long produceTs = msg.getProduceTimestamp();
            long now = System.currentTimeMillis();
            long latency = now - produceTs;

            kafkaLatencySummary.record(latency);

            log.info("Kafka latency = {} ms", latency);

            int resultValue = msg.getResult();
            int currentSummation = readCurrentSummation();
            int newTotal = currentSummation + resultValue;

            writeSummation(newTotal);
            log.info("Updated summation saved! New total = {}", newTotal);

        } catch (IOException e) {
            log.error("Error processing message", e);
        }
    }

    public int readCurrentSummation() {
        try {
            Path path = Path.of(filePath);
            if (!Files.exists(path))
                return 0;
            String content = Files.readString(path).trim();
            if (content.isEmpty())
                return 0;
            return Integer.parseInt(content);
        } catch (Exception e) {
            return 0;
        }
    }

    private void writeSummation(int total) {
        try (FileWriter writer = new FileWriter(filePath, false)) {
            writer.write(String.valueOf(total));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


