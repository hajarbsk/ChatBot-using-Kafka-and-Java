package com.GUI;

import com.server.ServerManager;
import com.Kafka.KafkaConsumerExample;
import com.Kafka.KafkaProducerExample;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


public class ChatbotApp extends Application {

    private static VBox chatBox;
    private static TextArea inputArea;
    @Override
    public void start(Stage primaryStage) {

        // Zone de chat (VBox)
        chatBox = new VBox(10);
        chatBox.setPadding(new Insets(10));
        chatBox.setStyle("-fx-background-color: #fbfbfb; -fx-font-size: 13px;");
        chatBox.setPrefHeight(430);
        chatBox.setPrefWidth(500);

        TextArea inputArea = new TextArea();
        inputArea.setPromptText("Type your message...");
        inputArea.setStyle("-fx-border-radius: 20px;");
        inputArea.setPrefHeight(50); // Hauteur du champ de texte

        Button sendButton = new Button("Send");
        sendButton.setPrefWidth(90);  // Largeur du bouton
        sendButton.setPrefHeight(50);  // Hauteur du bouton
        sendButton.setStyle("-fx-background-color: #9370DB; -fx-font-family: 'Roboto'; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5px;");
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
// Créer un HBox pour contenir le champ de texte et le bouton d'envoi
        HBox inputBox = new HBox(10, inputArea, sendButton);
        inputBox.setPadding(new Insets(15)); // Ajouter de la marge autour de l'inputBox
        inputBox.setAlignment(Pos.CENTER); // Alignement du contenu dans le HBox
        inputBox.setStyle("-fx-background-color: #7b1fa;"); // Fond de l'inputBox

        VBox chatbotInput = new VBox(5, chatBox, inputBox);

        Image icon = new Image("file:src/main/resources/nlp.png");
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(102);  // Ajuster la largeur de l'icône
        iconView.setFitHeight(78); // Ajuster la hauteur de l'icône

// Ajouter l'ImageView dans un HBox
        HBox imageContainer = new HBox(iconView);
        imageContainer.setAlignment(Pos.CENTER_LEFT);


        Button buttClear = new Button("🔄");
        buttClear.setStyle("-fx-font-size: 15px; -fx-background-color: #9370DB; -fx-text-fill: white; -fx-border-color: white; -fx-border-radius: 5px;");
        buttClear.setOnAction(e -> {
            chatBox.getChildren().clear();
            startMessage();
        });

        Label label = new Label("Fashion Products");
        label.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-font-family: 'Serif';");


        HBox headerBox = new HBox(7, imageContainer,label, buttClear);
        headerBox.setPadding(new Insets(10, 50, 10, 10));  // Marge à gauche pour décaler
        headerBox.setStyle("-fx-background-color: #9370DB;");
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setSpacing(170);
        headerBox.setPrefHeight(55);

        // Navigation latérale
        // Création des boutons du menu
        Button home = new Button("🏠 Home");
        Button localisation = new Button("📍 Localisation");
        Button aboutUs = new Button("🌟 About Us");
        Button contactUs = new Button("📞 Contact Us");
        Button help = new Button("❓ Help");
        Button dark = new Button("🌓 Dark Mode");

// Appliquer un style commun aux boutons
        Button[] buttons = {home, localisation, aboutUs, contactUs, help, dark};
        for (Button btn : buttons) {
            btn.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-font-family: 'Serif'; -fx-background-color: transparent; -fx-border-width: 0; -fx-border-radius: 5px;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-font-family: 'Serif'; -fx-background-color: #9370DB; -fx-border-radius: 5px;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-font-family: 'Serif'; -fx-background-color: transparent; -fx-border-radius: 5px;"));
        }

// Ajouter les boutons au VBox (menu)
        VBox leftBox = new VBox(10, home, localisation, aboutUs, contactUs, help, dark);
        leftBox.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-font-family: 'Serif'; -fx-background-color: #9370DB; -fx-padding: 10; -fx-border-radius: 25px;");
        leftBox.setPadding(new Insets(5));

        dark.setOnAction(e -> toggleDarkMode(leftBox, headerBox));

        HBox bottomMenu = new HBox(10); // Espacement entre les bouton
        bottomMenu.setAlignment(Pos.CENTER);  // Centrer les boutons horizontalement à droite
        bottomMenu.setPadding(new Insets(10));


        Button storeHoursBtn = new Button("Store hours");
        storeHoursBtn.setStyle("-fx-font-size: 12px; -fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px;");

        Button returnPolicyBtn = new Button("Return policy");
        returnPolicyBtn.setStyle("-fx-font-size: 12px;-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px;");

        Button productInfoBtn = new Button("Product info");
        productInfoBtn.setStyle("-fx-font-size: 12px;-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px;");

        Button promotionsBtn = new Button("Promotions");
        promotionsBtn.setStyle("-fx-font-size: 12px;-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px;");

        Button deliveryPolicyBtn = new Button("Delivery policy");
        deliveryPolicyBtn.setStyle("-fx-font-size: 12px;-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px;");


        Button specialOffersBtn = new Button("Special offers");
        specialOffersBtn.setStyle("-fx-font-size: 12px;-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px;");


        storeHoursBtn.setOnAction(e -> handleOptionSelection("Store hours"));
        returnPolicyBtn.setOnAction(e -> handleOptionSelection("Return policy"));
        productInfoBtn.setOnAction(e -> handleOptionSelection("Product info"));
        promotionsBtn.setOnAction(e -> handleOptionSelection("Promotions"));
        deliveryPolicyBtn.setOnAction(e -> handleOptionSelection("Delivery policy"));

        specialOffersBtn.setOnAction(e -> handleOptionSelection("Special offers"));

// Ajouter les nouveaux boutons au bottomMenu
        bottomMenu.getChildren().addAll(storeHoursBtn, returnPolicyBtn, productInfoBtn, promotionsBtn,
                deliveryPolicyBtn,  specialOffersBtn);

        VBox menu=new VBox(10,bottomMenu,inputBox);

        menu.setAlignment(Pos.CENTER_RIGHT);
        VBox mainContent = new VBox(8, chatBox, menu);

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        BorderPane root = new BorderPane();
        root.setTop(headerBox);
        root.setLeft(leftBox);
        root.setCenter(scrollPane);


        // Configuration de la scène
        Scene scene = new Scene(root, 770, 670);
        primaryStage.setTitle("Chatbot");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> Platform.exit());
        primaryStage.show();

        startMessage(); // Message de démarrage
    }


    private void startMessage() {
        displayMessage("Hello! I'm Fashion Product bot. How can I assist you today?", "bot");
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
        // Vérifier si on est actuellement en mode sombre en regardant le style actuel du leftBox
        boolean isDarkMode = leftBox.getStyle().contains("#333333");

        // Styles pour le mode clair
        String lightModeStyle = "-fx-background-color: #9370DB; -fx-text-fill: white;";
        String lightModeChatBoxStyle = "-fx-background-color: #ffffff;";

        // Styles pour le mode sombre
        String darkModeStyle = "-fx-background-color: #333333; -fx-text-fill: white;";
        String darkModeChatBoxStyle = "-fx-background-color: #2b2b2b;";

        // Appliquer les styles appropriés en fonction du mode actuel
        leftBox.setStyle(isDarkMode ? lightModeStyle : darkModeStyle);
        chatBox.setStyle(isDarkMode ? lightModeChatBoxStyle : darkModeChatBoxStyle);
        headerBox.setStyle(isDarkMode ? lightModeStyle : darkModeStyle);
    }

    private static void displayProductCards(String category, GridPane productGrid) {
        Platform.runLater(() -> displayMessage(category, "user"));

        Platform.runLater(() -> {
            productGrid.getChildren().clear(); // Efface les anciennes cartes

            List<Product> products = handleProductSearchOption(category);

            productGrid.setHgap(15); // Espacement horizontal
            productGrid.setVgap(15); // Espacement vertical
            productGrid.setPadding(new Insets(15)); // Marge autour de la grille

            // Vérifier si des produits ont été trouvés
            if (products.isEmpty()) {
                Label noProductsLabel = new Label("No products found in this category.");
                noProductsLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic; -fx-text-fill: gray;");
                productGrid.add(noProductsLabel, 0, 0); // Ajouter le message si aucune carte
            } else {
                int row = 0;
                int col = 0;
                for (Product product : products) {
                    VBox card = new VBox();
                    card.setSpacing(5); // Réduire l'espacement à l'intérieur des cartes
                    card.setPadding(new Insets(10));
                    card.setStyle("-fx-border-color: #dddddd; -fx-border-radius: 8; -fx-background-color: #ffffff; -fx-min-width: 150px; -fx-min-height: 200px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 5, 0, 0, 5);");

                    // Nom du produit
                    Label nameLabel = new Label(product.getProductName());
                    nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;");

                    // Prix du produit
                    Label priceLabel = new Label("Price: $" + product.getPrice());
                    priceLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: normal; -fx-text-fill: #4CAF50;");

                    // Marque du produit
                    Label brand = new Label("Brand: " + product.getBrand());
                    brand.setStyle("-fx-font-size: 12px; -fx-font-weight: normal;");

                    // Couleurs disponibles
                    Label colorsLabel = new Label("Colors: " + product.getColor());
                    colorsLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #777777;");

                    // Tailles disponibles
                    Label sizesLabel = new Label("Sizes: " + product.getSize());
                    sizesLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #777777;");

                    // Évaluation du produit - Formatage de la note
                    double ratingValue = product.getRating();
                    String formattedRating = String.format("%.3f", ratingValue); // Format à 3 chiffres après la virgule
                    Label rating = new Label("Rating: " + formattedRating);
                    rating.setStyle("-fx-font-size: 12px; -fx-text-fill: #777777;");

                    // Ajouter les informations à la carte
                    card.getChildren().addAll(nameLabel, priceLabel, brand, colorsLabel, sizesLabel, rating);
                    card.setStyle("-fx-border-color: #dddddd; " +
                            "-fx-border-radius: 8; " +
                            "-fx-background-color: #ffffff; " +
                            "-fx-min-width: 150px; " +
                            "-fx-min-height: 180px; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 5, 0, 0, 5);");

                    card.setOnMouseEntered(e -> card.setStyle("-fx-border-color: #9370DB; " +
                            "-fx-background-color: #f5f5f5; " +
                            "-fx-border-radius: 8; " +
                            "-fx-min-width: 150px; " +
                            "-fx-min-height: 180px; " +
                            "-fx-effect: dropshadow(gaussian, rgba(123, 31, 162, 0.4), 8, 0, 0, 8);"));

                    card.setOnMouseExited(e -> card.setStyle("-fx-border-color: #dddddd; " +
                            "-fx-border-radius: 8; " +
                            "-fx-background-color: #ffffff; " +
                            "-fx-min-width: 150px; " +
                            "-fx-min-height: 180px; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 5, 0, 0, 5);"));

                    // Ajouter la carte à la grille
                    productGrid.add(card, col, row);

                    col++;
                    if (col == 3) { // Limite à 3 cartes par ligne
                        col = 0;
                        row++; // Passer à la ligne suivante
                    }
                }
            }

            chatBox.getChildren().add(productGrid); // Ajouter la grille à la boîte de chat
        });
    }

    private static List<Product> handleProductSearchOption(String category) {
        MongoDBManager dbManager = new MongoDBManager();
        List<Product> products = dbManager.getProductsByCategory(category);

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

            case "Delivery policy":
                displayMessage("We offer free delivery for orders above $50. Delivery takes 3-5 business days.", "bot");
                break;

            case "Special offers":
                displayMessage("Current special offers: \n- 10% off on your first purchase!\n- Free shipping on orders above $100.", "bot");
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
            buttonBox.setStyle("-fx-background-color: #fbfbfb;");

            // Colonnes JSON pour la recherche
            String[] productSearchOptions = {"Men's Fashion", "Women's Fashion", "Kids' Fashion"};

            // Conteneur principal pour les produits (GridPane pour afficher en grille)
            GridPane productGrid = new GridPane();
            productGrid.setHgap(10); // Espacement horizontal entre les cartes
            productGrid.setVgap(10); // Espacement vertical entre les cartes
            productGrid.setPadding(new Insets(10)); // Marges autour de la grille

            // Ajout des boutons pour chaque catégorie
            for (String searchOption : productSearchOptions) {
                Button optionButton = new Button(searchOption);
                optionButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px;");

                // Ajout de l'événement sur chaque bouton pour afficher les produits de la catégorie
                optionButton.setOnAction(e -> displayProductCards(searchOption, productGrid));

                buttonBox.getChildren().add(optionButton);
            }

            // Efface les anciens éléments et ajoute les nouveaux dans le chatBox
            chatBox.getChildren().addAll(buttonBox, productGrid);
        });
    }



    private static void displayMessage(String message, String sender) {
        Platform.runLater(() -> {
            HBox messageBox = new HBox(10);
            messageBox.setPadding(new Insets(5));
            messageBox.setAlignment(sender.equals("user") ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            Label bubble = new Label(message);
            bubble.setPadding(new Insets(10));
            bubble.setStyle(sender.equals("user")
                    ? "-fx-background-color: #9370DB; -fx-text-fill: white; -fx-background-radius: 10px;"
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

            // Animation d'apparition
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), messageBox);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.play();


            chatBox.getChildren().add(messageBox);
        });
    }



    public static void main(String[] args) {

        launch(args);
    }
}
