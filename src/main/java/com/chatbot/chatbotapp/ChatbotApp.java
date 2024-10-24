package com.chatbot.chatbotapp;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChatbotApp extends Application {

    private ComboBox<String> topicComboBox;
    private TextArea questionTextArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chatbot");

        // ComboBox pour sélectionner le topic
        topicComboBox = new ComboBox<>();
        topicComboBox.getItems().addAll("topic-informations-produits", "topic-retours", "topic-livraison","topic-politique");
        topicComboBox.setValue("topic-informations-produits"); // Valeur par défaut

        // Zone de texte pour saisir la question
        questionTextArea = new TextArea();
        questionTextArea.setPromptText("Saisissez votre question ici...");
        questionTextArea.setLayoutX(250);
        questionTextArea.setLayoutY(200);

        // Bouton pour envoyer la question
        Button sendButton = new Button("Envoyer");
        sendButton.setOnAction(e -> sendQuestion());

        TextArea response = new TextArea();
        response.setLayoutX(250);
        response.setLayoutY(200);

        // Layout
        VBox layout = new VBox(10, topicComboBox, questionTextArea, sendButton,response);
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void sendQuestion() {
        String selectedTopic = topicComboBox.getValue();
        String question = questionTextArea.getText();

        // Appel à la méthode pour envoyer la question à Kafka
        KafkaProducerExample.sendToKafka(selectedTopic, question);

        // Vider la zone de texte après envoi
        questionTextArea.clear();
    }
}
