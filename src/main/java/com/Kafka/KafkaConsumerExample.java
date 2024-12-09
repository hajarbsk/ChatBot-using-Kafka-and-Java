package com.Kafka;

import com.Reponses;
import com.GUI.ChatbotApp;
import javafx.application.Platform;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerExample {

    private Reponses reponses;

    public KafkaConsumerExample() {
        // Initialisation de l'objet Reponses
        this.reponses = new Reponses();
    }

    public void startConsumer() {
        // Configuration du consumer Kafka
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092"); // Adresse du serveur Kafka
        props.put("group.id", "test-group"); // Identifiant de groupe pour le consommateur
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("questions")); // S'abonner au topic "questions"

        // Lancer un thread pour éviter de bloquer l'interface graphique
        new Thread(() -> {
            try {
                while (true) {
                    // Polling des messages Kafka
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                    if (records.isEmpty()) {
                        System.out.println("Aucun message reçu. En attente...");
                    } else {
                        for (ConsumerRecord<String, String> record : records) {
                            String userMessage = record.value();
                            System.out.println("Message reçu de Kafka : " + userMessage);

                            // Obtenir la réponse via la classe Reponses
                            String botResponse = reponses.respond(userMessage);

                            // Mise à jour de l'interface utilisateur (JavaFX)
                            Platform.runLater(() -> {
                                // Afficher le message utilisateur
                                try {
                                    Thread.sleep(3000); // Simuler une attente pour la réponse
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                                // Afficher la réponse du bot
                                ChatbotApp.displayMessage(botResponse, "bot");
                                System.out.println("\nReponse : "+ botResponse);
                            });
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Erreur lors de la consommation des messages Kafka.");
            } finally {
                consumer.close();
                System.out.println("Kafka Consumer fermé proprement.");
            }
        }).start();
    }
}
