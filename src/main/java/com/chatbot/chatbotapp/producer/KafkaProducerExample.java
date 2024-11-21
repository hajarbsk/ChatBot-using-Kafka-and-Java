package com.chatbot.chatbotapp.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;

public class KafkaProducerExample {
    // Méthode contenant toute la logique du producer
    public void runProducerLogic() {
        // Configurer le producer
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        // Envoi d'un message au topic
        String topic = "test1";
        String key = "user1";
        String value = "Quelle est la promotion d'aujourd'hui ?";
        producer.send(new ProducerRecord<>(topic, key, value));

        System.out.println("Message envoyé au topic : " + topic);
        producer.close();
    }

    public static void main(String[] args) {
        KafkaProducerExample producerExample = new KafkaProducerExample();
        producerExample.runProducerLogic(); // Appeler la logique du producer
    }
}
