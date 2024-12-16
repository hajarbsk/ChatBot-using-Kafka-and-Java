package com.Kafka;

import com.GUI.ChatbotApp;
import com.ChatResponse.NLP.nlpProcessorBot;
import javafx.application.Platform;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerExample {

    private nlpProcessorBot process; // Instance du bot NLP pour traiter les messages

    // Constructeur qui initialise l'objet de traitement NLP
    public KafkaConsumerExample() {
        this.process = new nlpProcessorBot(); // Initialisation du bot NLP
    }

    public void startConsumer() {
        // Configuration du consumer Kafka
        Properties props = new Properties();
        props.put("auto.offset.reset", "earliest"); // Lit les messages depuis le début
        props.put("bootstrap.servers", "localhost:9092"); // Adresse du serveur Kafka
        props.put("group.id", "test-group"); // Identifiant de groupe pour le consommateur
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("questions")); // S'abonner au topic "questions"

        // Lancer un thread pour éviter de bloquer l'interface graphique

            try {
                while (true) {
                    // Polling des messages Kafka
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                    if (records.isEmpty()) {
                        System.out.println("No messages received. On hold...");
                    } else {
                        for (ConsumerRecord<String, String> record : records) {
                            String userMessage = record.value();

                            System.out.println("Message received from Kafka : " + userMessage);

                            try {
                                // Obtenir la réponse via la classe NLP (process)
                                String botResponse = process.respond(userMessage);

                                // Mise à jour de l'interface utilisateur (JavaFX)
                                Platform.runLater(() -> {
                                    // Affichage du message utilisateur et de la réponse du bot
                                    ChatbotApp.displayMessage(botResponse, "bot"); // Affichage de la réponse du bot
                                    System.out.println("\nReponse : " + botResponse);
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                Platform.runLater(() -> {
                                    ChatbotApp.displayMessage("Désolé, une erreur s'est produite dans le traitement de la réponse.", "bot");
                                });
                            }
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


    }
}
