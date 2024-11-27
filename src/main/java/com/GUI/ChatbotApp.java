package com.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import com.Kafka.KafkaProducerExample;
import com.Kafka.KafkaConsumerExample;

public class ChatbotApp extends Application {

    static TextArea chat;
    static TextArea input;
    static Button sendButton;

    @Override
    public void start(Stage primaryStage) {
        // Initialisation des éléments via JavaFXHelper
        initializeElements();

        // Création de la disposition via JavaFXHelper
        GridPane gridPane = JavaFXHelper.createGridPane();
        JavaFXHelper.addItems(gridPane);

        // Démarrer le consommateur Kafka en arrière-plan
        new Thread(() -> KafkaConsumerExample.startConsumer(ChatbotApp::displayResponse)).start();

        // Configurer la scène et l'afficher
        Scene scene = new Scene(gridPane, 600, 700);
        primaryStage.setTitle("ChatBot Fusion Products");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> Platform.exit());
        primaryStage.show();
    }

    public static void initializeElements() {
        chat = new TextArea();
        chat.setEditable(false); // Empêcher la modification de la zone de discussion

        input = new TextArea();
        sendButton = new Button("Envoyer");

        // Configure le bouton d'envoi et la touche "Enter"
        sendButton.setOnAction(EventHandlers.onMessageSend());
        input.setOnKeyPressed(EventHandlers.onEnterPressed());
    }

    public static void displayResponse(String response) {
        Platform.runLater(() -> chat.appendText("Bot : " + response + "\n"));
    }

    public static void appendToChat(String message) {
        Platform.runLater(() -> chat.appendText(message + "\n"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
