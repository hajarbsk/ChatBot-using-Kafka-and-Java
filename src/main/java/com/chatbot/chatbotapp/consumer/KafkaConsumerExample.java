package com.chatbot.chatbotapp.consumer;

import com.chatbot.chatbotapp.Traitement.chatbot;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerExample {
    public void runConsumerLogic()  {
        // Configurer le consumer
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        // S'abonner au topic
        String topic = "test1";
        consumer.subscribe(Collections.singletonList(topic));
        chatbot chatbot = new chatbot();

        // Lecture des messages
        System.out.println("En attente des messages...");
        while (true) {
            // Polling pour récupérer les messages
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

            // Traitement des messages reçus
            for (ConsumerRecord<String, String> record : records) {
                // Appeler la méthode respond du chatbot pour générer une réponse
                String response = chatbot.respond(record.value());
                System.out.println("Message reçu : " + record.value());
                System.out.println("Réponse envoyée : " + response);
            }
        }
    }

    public static void main(String[] args) {
        KafkaConsumerExample consumerExample=new KafkaConsumerExample();
        consumerExample.runConsumerLogic();
    }
}
