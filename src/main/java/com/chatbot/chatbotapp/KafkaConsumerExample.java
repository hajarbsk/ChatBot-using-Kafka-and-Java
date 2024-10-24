package com.chatbot.chatbotapp;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerExample {

    private static final KafkaConsumer<String, String> consumer;

    static {
        // Configuration du consommateur Kafka
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "chatbot-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("topic-informations-produits")); // Remplacez par le topic de votre choix
    }

    public static void listenForMessages() {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("Message reçu de %s: %s%n", record.topic(), record.value());
                // Traitement du message (par exemple, consulter un fichier CSV)
                processMessage(record.value());
            }
        }
    }

    private static void processMessage(String message) {
        // Ici, vous pouvez ajouter la logique pour traiter le message
        // Par exemple, vérifier la disponibilité d'un produit dans un fichier CSV
        System.out.println("Traitement de la question: " + message);
        // Logique pour répondre à la question...
    }
}
