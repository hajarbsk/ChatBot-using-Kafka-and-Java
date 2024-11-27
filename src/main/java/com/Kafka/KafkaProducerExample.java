package com.Kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaProducerExample {

    private static KafkaProducer<String, String> producer;

    static {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());
        producer = new KafkaProducer<>(props);
    }

    public static void sendMessage(String message) {
        ProducerRecord<String, String> record = new ProducerRecord<>("user-queries", null, message);
        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
            }
        });
    }

    public static void closeProducer() {
        producer.close();
    }
}
