package com.asem.rest_service.Service;

import com.asem.rest_service.Model.Summation;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Value("${output.file-path}")
    private String filePath;

    @KafkaListener(topics = "${topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        try {
            // Convert the JSON string into a Message
            Summation msg = objectMapper.readValue(message, Summation.class);
            int resultValue = msg.getResult();
            int currentSummation = readCurrentSummation();
            int newTotal = currentSummation + resultValue;
            writeSummation(newTotal);
            System.out.println(" Updated summation saved ! ");
        } catch (IOException e) {
            e.printStackTrace();
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
        try (FileWriter writer = new FileWriter(filePath, false)) { // false = overwrite
            writer.write(String.valueOf(total));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

