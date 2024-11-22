package com.Kafka;

import com.Traitement.chatbot;
import com.GUI.ChatbotApp;
import com.GUI.ChatbotApp.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import javafx.application.Platform;

public class KafkaConsumerExample {
    public void runConsumerLogic()  {
        // Configurer le consumer Kafka
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("test1"));
        chatbot chatbot = new chatbot();

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

            for (ConsumerRecord<String, String> record : records) {
                // Logique de réponse du chatbot
                String response = chatbot.respond(record.value());

                // Mettre à jour l'UI avec la réponse via JavaFX
                Platform.runLater(() -> {
                    ChatbotApp.getChat().appendText("Bot: " + response + "\n");
                });
            }
        }
    }
}
