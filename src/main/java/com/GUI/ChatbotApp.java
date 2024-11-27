package com.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import com.server.ServerManager;
import com.Kafka.KafkaConsumerExample;
import com.Kafka.KafkaProducerExample;

public class ChatbotApp extends Application {

    static Button startServerButton;
    static Button sendButton;
    static TextArea chat = new TextArea();
    static TextArea input = new TextArea();

    public static TextArea getChat() {
        return chat;
    }

    @Override
    public void start(Stage primaryStage) {
        // Création des éléments de l'UI
        startServerButton = new Button("Start Server");

        // Action de démarrage du serveur Kafka/Zookeeper
        startServerButton.setOnAction(e -> new Thread(() -> {
            ServerManager serverManager = new ServerManager();
            serverManager.startServer();  // Démarre Zookeeper et Kafka dans des processus séparés
        }).start());

        // Action pour envoyer un message via Kafka Producer et consommer la réponse
        sendButton = new Button("Send Message");
        sendButton.setOnAction(e -> {
            String message = input.getText();  // Récupérer le message de l'utilisateur
            if (!message.trim().isEmpty()) {
                new Thread(() -> {
                    // Partie producteur : envoyer le message à Kafka
                    KafkaProducerExample producer = new KafkaProducerExample();
                    producer.runProducerLogic(message);  // Passer le message à Kafka

                    // Afficher le message envoyé dans la fenêtre chat
                    Platform.runLater(() -> {
                        chat.appendText("User: " + message + "\n");
                        input.clear();  // Effacer l'input après envoi
                    });

                    try {
                        Thread.sleep(1000); // Petite pause pour permettre à Kafka de traiter le message
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }

                    // Partie consommateur : consommer la réponse du message envoyé
                    KafkaConsumerExample consumer = new KafkaConsumerExample();
                    consumer.runConsumerLogic();  // Consommer le message et obtenir la réponse
                }).start();
            }
        });

        // Mise en place de la scène
        chat.setEditable(false);  // Le chat ne sera pas modifiable par l'utilisateur
        input.setPromptText("Type your message...");

        GridPane gridPane = JavaFXHelper.createGridPane();
        gridPane.add(startServerButton, 0, 0);
        gridPane.add(chat, 0, 1, 3, 1);  // Ajouter le champ chat (qui prend 3 colonnes)
        gridPane.add(input, 0, 2, 2, 1);  // Ajouter le champ input (qui prend 2 colonnes)
        gridPane.add(sendButton, 2, 2);   // Ajouter le bouton envoyer

        Scene scene = new Scene(gridPane, 535, 660);
        primaryStage.setTitle("ChatBot Fushion Products");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> Platform.exit());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
