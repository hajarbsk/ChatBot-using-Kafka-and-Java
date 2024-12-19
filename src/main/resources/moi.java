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
    private static VBox historyBox; // Pour l'historique
    private static ToggleButton buttHistory;


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
        sendButton.setPrefWidth(15);
        sendButton.setPrefHeight(20);
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
        inputBox.setPadding(new Insets(10));
        inputBox.setAlignment(Pos.CENTER_RIGHT);



        // Conteneur principal
        historyBox = new VBox(10);
        historyBox.setPadding(new Insets(10));
        historyBox.setPrefWidth(15);
        historyBox.setPrefHeight(600);
        historyBox.setStyle("-fx-background-color: #7b1fa2;"); // Couleur pour l'historique
        historyBox.setVisible(false);

        Label label=new Label("Fushion Products");
        label.setStyle("-fx-font-size: 22px; -fx-; fx-font-family: 'Azkadinya'; -fx-background-color: White; -fx-text-fill: 7b1fa2; -fx-font-weight: bold ; ");
        Label labelHist =new Label("History");
        label.setStyle("-fx-font-size: 17px; fx-font-family: 'Roboto'; -fx-text-fill: white; -fx-font-weight: bold;");
        label.setVisible(true);
        // Exemple : Ajouter des conversations fictives dans l'historique
        Label historyItem1 = new Label("Conversation id1");
        Label historyItem2 = new Label("Conversation id2");
        historyItem1.setStyle("-fx-font-size: 12px; -fx-text-fill: white;-fx-border-radius: 20px;-fx-border-color:grey;");
        historyItem2.setStyle("-fx-font-size: 12px; -fx-text-fill: white;-fx-border-radius: 20px;-fx-border-color:grey;");
        historyBox.getChildren().addAll(labelHist,historyItem1, historyItem2);

        //Zone de masquer/afficher l'historique
        buttHistory=new ToggleButton("≡");
        buttHistory.setStyle("-fx-font-size: 16px; -fx-background-color: #7b1fa2;  -fx-text-fill: #7b1fa2; ");
        buttHistory.setOnAction(e -> {
            System.out.println("Bouton historique cliqué");
            if (buttHistory.isSelected()) {
                labelHist.setVisible(true);
                historyBox.setPrefWidth(150);
                historyBox.setVisible(true); // Afficher l'historique
                primaryStage.setWidth(800); // Agrandir la fenêtre
            } else {
                labelHist.setVisible(false);
                historyBox.setPrefWidth(15);
                historyBox.setVisible(false); // Masquer l'historique
                primaryStage.setWidth(650); // Réduire la fenêtre
            }
        });




        // En-tête contenant le bouton de l'historique
        HBox headerBox = new HBox(5, buttHistory, labelHist);// Espacement de 10px entre les éléments
        HBox headerTitle =new HBox(70,headerBox,label);
        headerBox.setPrefWidth(150);
        headerBox.setPadding(new Insets(5));
        headerBox.setStyle("-fx-background-color: #7b1fa2;");
        headerTitle.setStyle("-fx-background-color: White;");

        headerBox.setAlignment(Pos.CENTER_LEFT); // Aligner les éléments à gauche

        // Ajouter l'en-tête dans le haut du BorderPane

        headerBox.setStyle("-fx-background-color: #7b1fa2; -fx-border-width: 0 0 0 5;");
        headerBox.setAlignment(Pos.CENTER_LEFT);




        BorderPane root = new BorderPane();
        root.setTop(headerTitle);//ajouter la  button dans l'en-tete
        root.setLeft(historyBox); // Ajouter la boîte d'historique à gauche
        root.setCenter(scrollPane); // Zone de chat
        root.setBottom(inputBox); // Zone d'entrée

        // Configuration de la scène
        Scene scene = new Scene(root, 650, 600);
        primaryStage.setTitle("Chatbot");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> Platform.exit());
        primaryStage.show();

        // Démarrage automatique des serveurs, du Kafka Consumer et du Kafka Producer
        startServers(); // Zookeeper et Kafka
        startConsumer(); // Kafka Consumer
    }
    private void startServers(){
        new Thread(() -> {
            ServerManager serverManager = new ServerManager();
            serverManager.startServer(); // Démarrage de Zookeeper et Kafka
        }).start();
    }
    private void startConsumer(){
        new Thread(() -> {
            KafkaConsumerExample consumer = new KafkaConsumerExample();
            consumer.startConsumer(); // Écoute des messages Kafka
        }).start();
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
                    Color.web("#f5f5f5"),
                    new CornerRadii(10), Insets.EMPTY
            ));

            Label bubble = new Label(message);
            bubble.setPadding(new Insets(10));
            bubble.setBackground(messageBackground);
            bubble.setTextFill(Color.web("7b1fa2"));
            bubble.setStyle("-fx-font-size: 14px; -fx-font-family: 'Roboto';");

            
            bubble.setWrapText(true);
            bubble.setMaxWidth(300);

            // Ajouter l'heure
            Label timeLabel = new Label(time);
            timeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: black; -fx-font-family : 'Serif'");

            VBox bubbleWithTime = new VBox(bubble, timeLabel);
            bubbleWithTime.setAlignment(Pos.CENTER_RIGHT);


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

    public static VBox getChatBox() {
        return chatBox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
