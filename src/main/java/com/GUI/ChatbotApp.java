package com.GUI;

import com.server.ServerManager;
import com.Kafka.KafkaConsumerExample;
import com.Kafka.KafkaProducerExample;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ChatbotApp extends Application {

    private static VBox chatBox;
    private static TextArea inputArea;

    @Override
    public void start(Stage primaryStage) {

        // Zone de chat (VBox)
        chatBox = new VBox(10);
        chatBox.setPadding(new Insets(10));
        chatBox.setStyle("-fx-background-color: #fbfbfb;");
        chatBox.setPrefHeight(500);
        chatBox.setPrefWidth(700);

        ScrollPane scrollPane = new ScrollPane(chatBox);
        scrollPane.setFitToWidth(true);

        // Zone d'entrée de texte
        inputArea = new TextArea();
        inputArea.setPromptText("Type your message...");
        inputArea.setStyle("-fx-border-radius: 20px;");
        inputArea.setPrefHeight(50);

        // Bouton pour envoyer un message
        Button sendButton = new Button("Send");
        sendButton.setPrefWidth(50);
        sendButton.setPrefHeight(30);
        sendButton.setStyle("-fx-background-color: #7b1fa2; -fx-font-family: 'Roboto'; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5px;");
        sendButton.setOnAction(e -> {
            String userMessage = inputArea.getText().trim();
            if (!userMessage.isEmpty()) {
                // Afficher le message utilisateur
                displayMessage(userMessage, "user");

                // Envoi du message au Kafka Producer
                new Thread(() -> {
                    KafkaProducerExample producer = new KafkaProducerExample();
                    producer.runProducerLogic(userMessage); // Envoi du message à Kafka
                }).start();

                // Effacer le champ de saisie
                inputArea.clear();
            }
        });

        // Mise en page des champs d'entrée et des boutons
        HBox inputBox = new HBox(10, inputArea, sendButton);
        inputBox.setPadding(new Insets(5));
        inputBox.setAlignment(Pos.CENTER);

        VBox chatbotInput = new VBox(5, chatBox, inputBox);

        // Bouton et Label pour l'en-tête
        Button buttClear = new Button("⭯");
        buttClear.setStyle("-fx-font-size: 18px; -fx-background-color: #7b1fa2; -fx-text-fill: white; -fx-border-color: white; -fx-border-radius: 5px;");
        buttClear.setOnAction(e -> chatBox.getChildren().clear());

        Label label = new Label("Al Hidaya");
        label.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-font-family: 'Serif';");

        HBox headerBox = new HBox(100, label, buttClear);
        headerBox.setPadding(new Insets(10));
        headerBox.setStyle("-fx-background-color: #7b1fa2;");
        headerBox.setAlignment(Pos.CENTER);

        // Navigation latérale
        Button home = new Button("🏠 Home");
        Button localisation = new Button("📍 Localisation");
        Button aboutUs = new Button("ℹ️ About Us");
        Button products = new Button("🛒 List of Products");
        Button contactUs = new Button("📞 Contact Us");
        Button help = new Button("❓ Help");
        VBox leftBox = new VBox(10, home, localisation, aboutUs, products, contactUs, help);
        leftBox.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-font-family: 'Serif'; -fx-border-radius: 25px; -fx-background-color: #7b1fa2;");
        leftBox.setPadding(new Insets(5));

        // Mode sombre
        Button dark = new Button("Dark Mode");
        dark.setOnAction(e -> toggleDarkMode(leftBox, headerBox));
        leftBox.getChildren().add(dark);

        // Mise en page principale
        BorderPane root = new BorderPane();
        root.setTop(headerBox);
        root.setLeft(leftBox);
        root.setCenter(scrollPane);
        root.setBottom(inputBox);

        // Configuration de la scène
        Scene scene = new Scene(root, 700, 600);
        primaryStage.setTitle("Chatbot");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> Platform.exit());
        primaryStage.show();

        // Démarrage automatique des serveurs et du Kafka Consumer
        startServers();
        startConsumer();
        startMessage(); // Message de démarrage
    }

    private void startMessage() {
        displayMessage("Hello! I'm Al Hidaya's bot. How can I assist you today?", "bot");
    }

    private void startServers() {
        new Thread(() -> {
            ServerManager serverManager = new ServerManager();
            serverManager.startServer();
        }).start();
    }

    private void startConsumer() {
        new Thread(() -> {
            KafkaConsumerExample consumer = new KafkaConsumerExample();
            consumer.startConsumer();
        }).start();
    }

    private void toggleDarkMode(VBox leftBox, HBox headerBox) {
        boolean isDarkMode = leftBox.getStyle().contains("#333333");
        String newStyle = isDarkMode
                ? "-fx-background-color: #7b1fa2; -fx-text-fill: white;"
                : "-fx-background-color: #333333; -fx-text-fill: white;";
        leftBox.setStyle(newStyle);
        headerBox.setStyle(newStyle);
    }

    public static void displayMessage(String message, String sender) {
        Platform.runLater(() -> {
            HBox messageBox = new HBox(10);
            messageBox.setPadding(new Insets(5));
            messageBox.setAlignment(sender.equals("user") ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            Label bubble = new Label(message);
            bubble.setPadding(new Insets(10));
            bubble.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
            bubble.setWrapText(true);
            bubble.setMaxWidth(300);

            Label timeLabel = new Label(time);
            timeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");
            VBox bubbleWithTime = new VBox(bubble, timeLabel);

            Label avatar = new Label(sender.equals("user") ? "👤" : "🤖");

            if (sender.equals("user")) {
                messageBox.getChildren().addAll(bubbleWithTime, avatar);
            } else {
                messageBox.getChildren().addAll(avatar, bubbleWithTime);

                // Ajouter des boutons d'options après le message du bot
                HBox buttonBox = new HBox(10);
                buttonBox.setAlignment(Pos.CENTER_LEFT);
                String[] options = {"Store hours", "Return policy", "Product info", "Promotions"};
                for (String option : options) {
                    Button optionButton = new Button(option);
                    optionButton.setStyle("-fx-background-color: #7b1fa2; -fx-text-fill: white;");
                    optionButton.setOnAction(e -> displayMessage(option, "user"));
                    buttonBox.getChildren().add(optionButton);
                }
                chatBox.getChildren().add(buttonBox);
            }


            chatBox.getChildren().add(messageBox);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
