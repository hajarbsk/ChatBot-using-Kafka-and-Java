package com.Kafka;

import com.GUI.ChatbotApp;
import com.Reponses;
import com.Traitement.chatbot;
import javafx.application.Platform;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class SendResponseConsume {
        private static String resp;

        public static void setResp(String resp) {
            com.Kafka.SendResponseConsume.resp = resp;
        }

        public void runConsumerLogic()  {
            // Configurer le consumer Kafka
            Properties props = new Properties();
            props.put("bootstrap.servers", "localhost:9092");
            props.put("group.id", "test-group");
            props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
            consumer.subscribe(Collections.singletonList("responses"));
            chatbot boot = new chatbot();


            while (true) {
                System.out.println("Polling for messages...");
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                if (records.isEmpty()) {
                    System.out.println("No messages received");
                } else {
                    Reponses cboot = new Reponses();

                    for (ConsumerRecord<String, String> record : records) {
                        String userMessage = record.value();
                        String response = cboot.respond(userMessage);

                        try {
                            Thread.sleep(5000); // Attendre un peu que Zookeeper soit prêt
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Platform.runLater(() -> {
                            ChatbotApp.getChat().appendText("Bot: " + response + "\n");
                        });
                    }

//                    for (ConsumerRecord<String, String> record : records) {
//                        // Afficher les messages reçus dans le bon ordre
//                        String response = boot.respond(record.value());
//                        System.out.println("Received message: " + record.value());
//                        Platform.runLater(() -> {
//                            // Mettre la réponse dans l'interface graphique
//                            System.out.println("\nResponse est : "+response);
//                            ChatbotApp.getChat().appendText("Bot: " + response + "\n");
//
//                        });
//                    }
                }


            }
        }
    }

