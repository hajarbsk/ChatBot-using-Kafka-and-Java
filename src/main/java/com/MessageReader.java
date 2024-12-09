package com;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class MessageReader {
    public static void main(String[] args) {
        // Configuration du consumer Kafka
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092"); // Adresse du broker Kafka
        props.put("group.id", "message-reader-group");     // Identifiant du groupe
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // Créer un consumer Kafka
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        // S'abonner au topic "questions"
        consumer.subscribe(Collections.singletonList("questions"));

        System.out.println("En attente des messages du topic 'questions'...");

        try {
            while (true) {
                // Lire les messages du topic
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf(
                            "Partition : %d, Offset : %d, Clé : %s, Valeur : %s%n",
                            record.partition(),
                            record.offset(),
                            record.key(),
                            record.value()
                    );
                }
            }
        } finally {
            consumer.close();
        }
    }
}
