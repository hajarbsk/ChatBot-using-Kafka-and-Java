package com.chatbot.chatbotapp;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import java.util.Properties;

public class KafkaProducerExample {

    private static final KafkaProducer<String, String> producer;

    static {
        // Configuration du producteur Kafka
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092"); // Adresse de Kafka
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<>(props);
    }

    public static void sendToKafka(String topic, String message) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
        producer.send(record, (RecordMetadata metadata, Exception exception) -> {
            if (exception == null) {
                System.out.println("Message envoyé à " + topic + ": " + message);
            } else {
                exception.printStackTrace();
            }
        });
    }
}
