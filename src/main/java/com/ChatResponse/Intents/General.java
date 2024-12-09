package com.ChatResponse.Intents;


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

public class General extends Application {

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

                // Effacer le champ de saisie
                inputArea.clear();
            }
        });

        // Mise en page des champs d'entrée et des boutons
        HBox inputBox = new HBox(10, inputArea, sendButton);
        inputBox.setPadding(new Insets(5));
        inputBox.setAlignment(Pos.CENTER);

        // Initialisation de la scène principale
        BorderPane root = new BorderPane();
        root.setCenter(scrollPane); // Zone de chat
        root.setBottom(inputBox); // Zone d'entrée

        // Configuration de la scène
        Scene scene = new Scene(root, 700, 600);
        primaryStage.setTitle("Chatbot");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> Platform.exit());
        primaryStage.show();

        // Message initial
        startConversation();
    }

    private void startConversation() {
        displayMessage("Hello! I'm Al Hidaya's bot. How can I assist you today?", "bot");
        displayOptions();
    }

    private void displayOptions() {
        Platform.runLater(() -> {
            HBox optionsBox = new HBox(10);
            optionsBox.setPadding(new Insets(10));
            optionsBox.setAlignment(Pos.CENTER);

            // Boutons pour les choix
            Button productInfoButton = new Button("Infos des produits");
            productInfoButton.setOnAction(e -> handleUserChoice("Infos des produits", "Here are the details about our products..."));

            Button openingHoursButton = new Button("Horaires d'ouverture");
            openingHoursButton.setOnAction(e -> handleUserChoice("Horaires d'ouverture", "We are open from 9 AM to 9 PM every day."));

            Button returnPolicyButton = new Button("Politique de retour");
            returnPolicyButton.setOnAction(e -> handleUserChoice("Politique de retour", "You can return products within 30 days of purchase."));

            // Ajouter les boutons au conteneur
            optionsBox.getChildren().addAll(productInfoButton, openingHoursButton, returnPolicyButton);

            // Ajouter les options à la boîte de chat
            chatBox.getChildren().add(optionsBox);
        });
    }

    private void handleUserChoice(String userChoice, String botResponse) {
        // Afficher le choix de l'utilisateur
        displayMessage(userChoice, "user");

        // Supprimer les boutons de choix après la sélection
        chatBox.getChildren().removeIf(node -> node instanceof HBox);

        // Afficher la réponse du bot
        displayMessage(botResponse, "bot");

        // Réafficher les options pour une autre interaction
        displayOptions();
    }

    // Méthode pour afficher les messages avec l'heure et le style
    public static void displayMessage(String message, String sender) {
        Platform.runLater(() -> {
            HBox messageBox = new HBox(10); // Espace entre les éléments
            messageBox.setPadding(new Insets(5));
            messageBox.setAlignment(sender.equals("user") ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

            // Obtenir l'heure actuelle
            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

            // Style de la bulle
            Background messageBackground = new Background(new BackgroundFill(
                    sender.equals("user") ? Color.LIGHTBLUE : Color.LIGHTGRAY,
                    new CornerRadii(10), Insets.EMPTY
            ));

            Label bubble = new Label(message);
            bubble.setPadding(new Insets(10));
            bubble.setBackground(messageBackground);
            bubble.setStyle("-fx-font-size: 14px; -fx-font-family: 'Roboto'; -fx-text-fill: black;");
            bubble.setWrapText(true);
            bubble.setMaxWidth(300);

            // Ajouter l'heure
            Label timeLabel = new Label(time);
            timeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");

            VBox bubbleWithTime = new VBox(bubble, timeLabel);

            // Avatar
            Label avatar = new Label(sender.equals("user") ? "👤" : "🤖");
            avatar.setStyle("-fx-font-size: 16px;");

            // Organisation des éléments
            if (sender.equals("user")) {
                messageBox.getChildren().addAll(bubbleWithTime, avatar);
            } else {
                messageBox.getChildren().addAll(avatar, bubbleWithTime);
            }

            chatBox.getChildren().add(messageBox);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

