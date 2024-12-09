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
import java.util.ArrayList;
import java.util.List;


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
        buttClear.setOnAction(e ->{
            chatBox.getChildren().clear();
            startMessage();
        });

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

        // Ajouter les boutons en bas
        HBox bottomMenu = new HBox(10); // Espacement entre les boutons
        bottomMenu.setAlignment(Pos.CENTER);
        bottomMenu.setPadding(new Insets(10));

        // Créer les boutons de menu
        Button storeHoursBtn = new Button("Store hours");
        Button returnPolicyBtn = new Button("Return policy");
        Button productInfoBtn = new Button("Product info");
        Button promotionsBtn = new Button("Promotions");

        // Actions des boutons
        storeHoursBtn.setOnAction(e -> handleOptionSelection("Store hours"));
        returnPolicyBtn.setOnAction(e -> handleOptionSelection("Return policy"));
        productInfoBtn.setOnAction(e -> handleOptionSelection("Product info"));
        promotionsBtn.setOnAction(e -> handleOptionSelection("Promotions"));

        // Ajouter les boutons au bottomMenu
        bottomMenu.getChildren().addAll(storeHoursBtn, returnPolicyBtn, productInfoBtn, promotionsBtn);

        VBox menu=new VBox(10,bottomMenu,inputBox);
        // Mise en page principale
        BorderPane root = new BorderPane();
        root.setTop(headerBox);
        root.setLeft(leftBox);
        root.setCenter(scrollPane);
        root.setBottom(menu); // Ajouter les boutons en bas

        // Configuration de la scène
        Scene scene = new Scene(root, 700, 600);
        primaryStage.setTitle("Chatbot");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> Platform.exit());
        primaryStage.show();

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

    private static void displayProductCards(String category, VBox productContainer) {
        // Efface les cartes existantes dans le conteneur
        displayMessage(category,"user");
        // Récupère les produits pour la catégorie sélectionnée
        List<Product> products = handleProductSearchOption(category);

        // Crée des cartes pour chaque produit
        for (Product product : products) {
            // Conteneur principal pour chaque carte
            VBox card = new VBox();
            card.setSpacing(10);
            card.setPadding(new Insets(10));
            card.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: #f9f9f9;");

            // Titre du produit
            Label nameLabel = new Label(product.getNamePr());
            nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            // Prix
            Label priceLabel = new Label("Price: $" + product.getPricePr());

            // Couleurs disponibles
            Label colorsLabel = new Label("Colors: " + String.join(", ", product.getColors()));

            // Tailles disponibles
            Label sizesLabel = new Label("Sizes: " + String.join(", ", product.getSizes()));

            // Stock disponible
            Label stockLabel = new Label("Stock: " + product.getQuantite());

            // Ajout des éléments au conteneur de la carte
            card.getChildren().addAll(nameLabel, priceLabel, colorsLabel, sizesLabel, stockLabel);

            // Ajout de la carte au conteneur principal
            productContainer.getChildren().add(card);

        }chatBox.getChildren().add(productContainer);
    }
    private static List<Product> handleProductSearchOption(String category) {
        // Liste de produits à retourner
        List<Product> products = new ArrayList<>();
        switch (category) {
            case "Men":
                products.add(new Product(
                        "Jacket",
                        new String[]{"Black", "Gray"},
                        new String[]{"M", "L", "XL"},
                        50.0,
                        10
                ));
                products.add(new Product(
                        "Shoes",
                        new String[]{"Brown", "Black"},
                        new String[]{"40", "41", "42", "43"},
                        79.99,
                        15
                ));
                break;

            case "Women":
                products.add(new Product(
                        "Dress",
                        new String[]{"Red", "Blue", "Green"},
                        new String[]{"S", "M", "L"},
                        59.99,
                        20
                ));
                products.add(new Product(
                        "Handbag",
                        new String[]{"Black", "Beige", "White"},
                        new String[]{"One Size"},
                        89.99,
                        8
                ));
                break;

            case "Kids":
                products.add(new Product(
                        "T-Shirt",
                        new String[]{"Yellow", "Pink", "Blue"},
                        new String[]{"XS", "S", "M"},
                        19.99,
                        25
                ));
                products.add(new Product(
                        "Sneakers",
                        new String[]{"White", "Black", "Red"},
                        new String[]{"28", "30", "32"},
                        29.99,
                        12
                ));
                break;

            default:
                System.out.println("Invalid category selected.");
                break;
        }

        return products;
    }



    private static void handleOptionSelection(String option) {
        displayMessage(option, "user");
        switch (option) {
            case "Store hours":
                displayMessage("Our store is open from 9:00 AM to 9:00 PM every day.", "bot");
                break;

            case "Return policy":
                displayMessage("You can return products within 30 days with the receipt.", "bot");
                break;

            case "Product info":
                displayMessage("Please specify the category for more details:", "bot");
                displayProductSearchOptions(); // Afficher les sous-options
                break;

            case "Promotions":
                displayMessage("Current promotions: \n- Buy 1 Get 1 Free on select items\n- 20% off on all winter clothing!", "bot");
                break;

            default:
                displayMessage("Sorry, I don't recognize this option.", "bot");
                break;
        }
    }

    private static void displayProductSearchOptions() {
        Platform.runLater(() -> {
            // Conteneur pour les boutons
            HBox buttonBox = new HBox(10); // Espacement entre les boutons
            buttonBox.setPadding(new Insets(10));
            buttonBox.setAlignment(Pos.CENTER_LEFT);
            buttonBox.setStyle("-fx-background-color: #fbfbfb;");

            // Colonnes JSON pour la recherche
            String[] productSearchOptions = {"Men", "Women", "Kids"};

            // Conteneur principal pour les produits
            VBox productContainer = new VBox();
            productContainer.setSpacing(10);
            productContainer.setPadding(new Insets(10));

            // Ajout des boutons pour chaque catégorie
            for (String searchOption : productSearchOptions) {
                Button optionButton = new Button(searchOption);
                optionButton.setStyle("-fx-background-color: #7b1fa2; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px;");

                // Configure l'action de clic pour afficher les produits de la catégorie
                optionButton.setOnAction(e -> displayProductCards(searchOption, productContainer));

                buttonBox.getChildren().add(optionButton);
            }

            // Efface les anciens éléments et ajoute les nouveaux dans le chatBox
            chatBox.getChildren().addAll(buttonBox, productContainer);
        });
    }
    private static void displayChatbotOptions() {
        Platform.runLater(() -> {
            HBox buttonBox = new HBox(10); // Espacement entre les boutons
            buttonBox.setPadding(new Insets(5));
            buttonBox.setAlignment(Pos.CENTER_LEFT);
            buttonBox.setStyle("-fx-background-color: #fbfbfb;");

            // Options à afficher
            String[] options = {"Store hours", "Return policy", "Product info", "Promotions"};
            for (String option : options) {
                Button optionButton = new Button(option);
                optionButton.setStyle("-fx-background-color: #7b1fa2; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px;");

                // Gérer l'action en fonction du texte du bouton
                optionButton.setOnAction(e -> handleOptionSelection(option));
                buttonBox.getChildren().add(optionButton);
                buttonBox.setVisible(true);
            }

            // Ajouter la boîte des boutons au chatBox
            chatBox.getChildren().add(buttonBox);
        });
    }


    public static void displayMessage(String message, String sender) {
        Platform.runLater(() -> {
            HBox messageBox = new HBox(10);
            messageBox.setPadding(new Insets(5));
            messageBox.setAlignment(sender.equals("user") ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            Label bubble = new Label(message);
            bubble.setPadding(new Insets(10));
            bubble.setStyle(sender.equals("user")
                    ? "-fx-background-color: #7b1fa2; -fx-text-fill: white; -fx-background-radius: 10px;"
                    : "-fx-background-color: #e0e0e0; -fx-text-fill: black; -fx-background-radius: 10px;");
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

            }
            chatBox.getChildren().add(messageBox);
        });
    }



    public static void main(String[] args) {

        launch(args);
    }
}
