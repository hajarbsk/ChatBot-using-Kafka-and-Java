package com.Kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaProducerExample {

    public void runProducerLogic(String message) {
        // Configuration du producteur Kafka
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());

        // Créer le producteur Kafka
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        // Envoyer un message au topic "test1"
        String topic = "test1";
        String key = "user1";  // Peut être dynamique si vous souhaitez identifier l'utilisateur
        producer.send(new ProducerRecord<>(topic, key, message), (metadata, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
            } else {
                System.out.println("Message envoyé au topic : " + topic);
            }
        });

        // Fermer le producteur
        producer.close();
    }
}
