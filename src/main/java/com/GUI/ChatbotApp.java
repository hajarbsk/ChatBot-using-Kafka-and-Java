package com.GUI;
import  com.LeftMenu.*;

import com.Kafka.KafkaConsumerExample;
import com.server.ServerManager;
//import com.Kafka.KafkaConsumerExample;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.bson.types.ObjectId;


public class ChatbotApp extends Application {

    private static VBox chatBox;
    private static TextArea inputArea;
    private Button buttClear = new Button("🔄");


    public Button getButtClear() {
        return buttClear;
    }

    //private static final String colorFix="#FFF7D1";
    @Override
    public void start(Stage primaryStage) {

        // Zone de chat (VBox)
        chatBox = new VBox(10);
        chatBox.setPadding(new Insets(10));
        chatBox.setStyle("-fx-background-color: #fbfbfb; -fx-font-size: 13px;");
        chatBox.setMinHeight(430);
        chatBox.setMinWidth(500);


         inputArea = new TextArea();
        inputArea.setPromptText("Type your message...");
        inputArea.setStyle("-fx-border-radius: 20px;");
        inputArea.setPrefHeight(50); // Hauteur du champ de texte
        Button sendButton = new Button("Send");
        sendButton.setPrefWidth(90);  // Largeur du bouton
        sendButton.setPrefHeight(50);  // Hauteur du bouton
        sendButton.setStyle("-fx-background-color: #6261af; -fx-font-family: 'Roboto'; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5px;");
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
        // Lorsque l'utilisateur appuie sur la touche "Entrée" dans le champ de texte
        inputArea.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                String userMessage = inputArea.getText().trim();
                if (!userMessage.isEmpty()) {
                    // Afficher le message de l'utilisateur (en utilisant une méthode comme displayMessage)
                    displayMessage(userMessage, "user");

                    // Envoi du message au Kafka Producer dans un thread séparé
                    new Thread(() -> {
                        KafkaProducerExample producer = new KafkaProducerExample();
                        producer.runProducerLogic(userMessage); // Envoi du message à Kafka
                    }).start();

                    // Effacer le champ de saisie
                    inputArea.clear();
                }
            }
        });
// Créer un HBox pour contenir le champ de texte et le bouton d'envoi
        HBox inputBox = new HBox(10);
        inputBox.getChildren().addAll(inputArea,sendButton);
        inputBox.setPadding(new Insets(15)); // Ajouter de la marge autour de l'inputBox
        inputBox.setAlignment(Pos.CENTER_RIGHT); // Alignement du contenu dans le HBox
        inputBox.setStyle("-fx-background-color: #f0f0f0;"); // Fond de l'inputBox

        Image icon = new Image("file:src/main/resources/robot.png");
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(102);  // Ajuster la largeur de l'icône
        iconView.setFitHeight(78); // Ajuster la hauteur de l'icône

// Ajouter l'ImageView dans un HBox
        HBox imageContainer = new HBox(iconView);
        imageContainer.setAlignment(Pos.CENTER_LEFT);



        buttClear.setStyle("-fx-font-size: 15px; -fx-background-color: #6261af; -fx-text-fill: white; -fx-border-color: white; -fx-border-radius: 5px;");
        buttClear.setOnAction(e -> {
            chatBox.getChildren().clear();
            startMessage();
        });

        Label label = new Label("Fashion Products");//#6e61af
        label.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-font-family: 'Roboto'; -fx-background-color: #6261af;-fx-border-radius: 10px; -fx-border-color: white;");


        HBox headerBox = new HBox(7, imageContainer,label,buttClear);
        headerBox.setPadding(new Insets(10, 50, 10, 10));  // Marge à gauche pour décaler
        headerBox.setStyle("-fx-background-color: #f0f0f0;");
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setSpacing(190);
        headerBox.setPrefHeight(40);




        Button storeHoursBtn = new Button("Store hours");
        storeHoursBtn.setStyle("-fx-font-size: 12px; -fx-background-color: #6261af; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px;");

        Button returnPolicyBtn = new Button("Return policy");
        returnPolicyBtn.setStyle("-fx-font-size: 12px;-fx-background-color: #6261af; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px;");

        Button productInfoBtn = new Button("Product info");
        productInfoBtn.setStyle("-fx-font-size: 12px;-fx-background-color: #6261af; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px;");

        Button promotionsBtn = new Button("Promotions");
        promotionsBtn.setStyle("-fx-font-size: 12px;-fx-background-color: #6261af; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px;");

        Button deliveryPolicyBtn = new Button("Delivery policy");
        deliveryPolicyBtn.setStyle("-fx-font-size: 12px;-fx-background-color: #6261af; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px;");


        Button specialOffersBtn = new Button("Special offers");
        specialOffersBtn.setStyle("-fx-font-size: 12px;-fx-background-color: #6261af; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px;");


        storeHoursBtn.setOnAction(e -> handleOptionSelection("Store hours"));
        returnPolicyBtn.setOnAction(e -> handleOptionSelection("Return policy"));
        productInfoBtn.setOnAction(e -> handleOptionSelection("Product info"));
        promotionsBtn.setOnAction(e -> handleOptionSelection("Promotions"));
        deliveryPolicyBtn.setOnAction(e -> handleOptionSelection("Delivery policy"));

        specialOffersBtn.setOnAction(e -> handleOptionSelection("Special offers"));

        HBox bottomMenu=new HBox(10);
// Ajouter les nouveaux boutons au bottomMenu
        bottomMenu.getChildren().addAll(storeHoursBtn, returnPolicyBtn, productInfoBtn, promotionsBtn,
                deliveryPolicyBtn,  specialOffersBtn);

// Navigation latérale
        // Création des boutons du menu
        Button home = new Button("🏠 Home       ");
        Button localisation = new Button("📍 Location    ");
        Button aboutUs = new Button("🌟 About Us   ");
        Button contactUs = new Button("📞 Contact Us ");

        Button dark = new Button("🌓 Dark Mode ");
        home.setOnAction(e->{updateChatBox(chatBox);
            startMessage();sendButton.setVisible(true); inputBox.setVisible(true);bottomMenu.setVisible(true);});
        contactUs.setOnAction(e ->{ updateChatBox(new ContactUs().getContent());sendButton.setVisible(false); inputBox.setVisible(false);bottomMenu.setVisible(false);});
        localisation.setOnAction(e -> {updateChatBox(new Localisation().getContent());sendButton.setVisible(false); inputBox.setVisible(false);bottomMenu.setVisible(false);});
        aboutUs.setOnAction(e ->{updateChatBox(new AboutUs().getContent());sendButton.setVisible(false); inputBox.setVisible(false);bottomMenu.setVisible(false);});


// Appliquer un style commun aux boutons
        Button[] buttons = {home, localisation, aboutUs, contactUs, dark};
        for (Button btn : buttons) {
            btn.setStyle("-fx-font-size: 16px; -fx-text-fill: #6261af; -fx-font-family: 'Serif'; -fx-background-color:  #f0f0f0;-fx-border-color:  #6261af; -fx-border-radius: 10px;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-font-size: 16px; -fx-text-fill: #6261af; -fx-font-family: 'Serif';  -fx-background-color: #f0f0f0; -fx-border-radius: 10px;-fx-border-color:  #6261af;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-font-size: 16px; -fx-text-fill: #6261af; -fx-font-family: 'Serif';-fx-background-color: #f0f0f0; -fx-border-radius: 10px;-fx-border-color:  #6261af;"));
        }

// Ajouter les boutons au VBox (menu)
        VBox leftBox = new VBox(10, home, localisation, aboutUs, contactUs, dark);
        leftBox.setStyle("-fx-font-size: 16px; -fx-text-fill: #6261af; -fx-font-family: 'Serif'; -fx-background-color: #f0f0f0; -fx-padding: 10; -fx-border-radius: 25px;");
        leftBox.setPadding(new Insets(5));


        bottomMenu.setAlignment(Pos.CENTER);  // Centrer les boutons horizontalement à droite
        bottomMenu.setPadding(new Insets(10));
        bottomMenu.setStyle("-fx-background-color: #f0f0f0;");
        VBox menu=new VBox(10,bottomMenu,inputBox);
        dark.setOnAction(e -> toggleDarkMode(leftBox, headerBox,menu,bottomMenu,inputBox));



        menu.setAlignment(Pos.BOTTOM_CENTER);
        VBox mainContent = new VBox(8);
        mainContent.getChildren().addAll(chatBox,menu);



        BorderPane rootInput=new BorderPane();
        rootInput.setBottom(menu);
        rootInput.setCenter(chatBox);
        rootInput.setVisible(true);
        ScrollPane scrollPane = new ScrollPane(rootInput);
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

       startServers();
       startConsumer();
        startMessage(); // Message de démarrage
    }
    private void updateChatBox(VBox newContent) {
        if (newContent != chatBox) { // Assurez-vous que le nouveau contenu n'est pas le parent
            chatBox.getChildren().clear(); // Efface l'ancien contenu
            chatBox.getChildren().add(newContent); // Ajoute le nouveau contenu
            getButtClear().setVisible(false);

        }
        if(newContent==chatBox){
            newContent.getChildren().clear();
            getButtClear().setVisible(true);

        }
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
//
    private void toggleDarkMode(VBox leftBox, HBox headerBox,VBox m,HBox n, HBox b){
        // Vérifier si on est actuellement en mode sombre en regardant le style actuel du leftBox
        boolean isDarkMode = leftBox.getStyle().contains("#333333");

        // Styles pour le mode clair
        String lightModeStyle = "-fx-background-color: #ececec; -fx-text-fill: white;";
        String lightModeChatBoxStyle = "-fx-background-color: #ffffff;";

        // Styles pour le mode sombre
        String darkModeStyle = "-fx-background-color: #333333; -fx-text-fill: white;";
        String darkModeChatBoxStyle = "-fx-background-color: #2b2b2b;";

        // Appliquer les styles appropriés en fonction du mode actuel
        leftBox.setStyle(isDarkMode ? lightModeStyle : darkModeStyle);
        n.setStyle(isDarkMode ? lightModeStyle : darkModeStyle);
        chatBox.setStyle(isDarkMode ? lightModeChatBoxStyle : darkModeChatBoxStyle);
        m.setStyle(isDarkMode ? lightModeChatBoxStyle : darkModeChatBoxStyle);
        headerBox.setStyle(isDarkMode ? lightModeStyle : darkModeStyle);
        b.setStyle(isDarkMode ? lightModeStyle : darkModeStyle);

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
                displayMessage("Current promotions: ", "bot");
                createPromotionButtons();
                break;

            case "Delivery policy":
                displayMessage("We offer free delivery for orders above $50. Delivery takes 3-5 business days.", "bot");
                break;

            case "Special offers":
                displayMessage("Current special offers:\n - 10% off on your first purchase!\n Free shipping on orders above $100.", "bot");
                break;

            default:
                displayMessage("Sorry, I don't recognize this option.", "bot");
                break;
        }
    }
    private static void displayPromotionDetails(String userChoice) {
        switch (userChoice) {
            case "1":
                displayMessage("Buy 1 Get 1 Free: Available on selected items.", "bot");
                // Créer une liste de produits en promotion pour "Buy 1 Get 1 Free"
                Product[] buy1Get1FreeProducts = {
                        new Product(new ObjectId(), 1, 130, "T-shirt", "Nike", "Kids", 20, 4.5, "Red", "M"),
                        new Product(new ObjectId(), 2, 100, "Jeans", "Zara", "Men", 50, 4.7, "Blue", "32"),
                        new Product(new ObjectId(), 3, 105, "Sweater", "Adidas", "Men", 40, 4.6, "Black", "L"),
                        new Product(new ObjectId(), 4, 200, "Dress", "H&M", "Women", 100, 4.6, "Black", "L")
                };
                // Appeler la méthode pour afficher les produits
                displayPromotionalProducts(buy1Get1FreeProducts);
                break;

            case "2":
                displayMessage("20% off on winter clothing: Includes jackets, scarves, and boots.", "bot");
                // Créer une liste de produits en promotion pour "20% off on winter clothing"
                Product[] winterClothingProducts = {
                        new Product(new ObjectId(), 4, 104, "Winter Jacket", "NorthFace", "Clothing", 120, 4.8, "Black", "XL"),
                        new Product(new ObjectId(), 5, 105, "Woolen Scarf", "Gucci", "Accessories", 50, 4.9, "Gray", "One Size"),
                        new Product(new ObjectId(), 6, 106, "Snow Boots", "Timberland", "Footwear", 150, 4.7, "Brown", "40"),
                        new Product(new ObjectId(), 7, 106, "Boots", "Levis", "Footwear", 190, 4.7, "black", "38")
                };
                // Appeler la méthode pour afficher les produits
                displayPromotionalProducts(winterClothingProducts);
                break;

            case "3":
                displayMessage("Extra 10% off: Applies automatically at checkout for orders above $100.", "bot");
                displayMessage("All items are eligible for this promotion if the total order exceeds $100.", "bot");
                break;
            default:
                displayMessage("Invalid selection. Please choose a valid promotion number.", "bot");
                break;
        }
    }

    private static void displayPromotionalProducts(Product[] products) {
        Platform.runLater(() -> {

            HBox productBox = new HBox(15); // Espacement de 10 entre les produits
            productBox.setPadding(new Insets(10));
            productBox.setStyle("-fx-background-color: #6261af;");

            for (Product product : products) {
                // Conteneur pour un produit (VBox)
                VBox productCard = new VBox(8); // Espacement vertical entre les détails
                productCard.setPadding(new Insets(10));
                productCard.setStyle(
                        "-fx-border-color: #d8d6f4; " +
                                "-fx-border-width: 2px; " +
                                "-fx-border-radius: 5px; " +
                                "-fx-background-color: #ffffff; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.25), 5, 0, 1, 1);"
                );

                // Texte pour les détails du produit
                Text productName = new Text(product.getProductName());
                productName.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                Text productDetails = new Text(String.format(
                        "Brand: %s\nPrice: $%d\nCategory: %s\nRating: %.1f/5\nColor: %s\nsize: %s",
                        product.getBrand(),
                        product.getPrice(),
                        product.getCategory(),
                        product.getRating(),
                        product.getColor(),
                        product.getSize()
                ));
                productDetails.setStyle("-fx-font-size: 12px; -fx-fill: #555;");
                productCard.getChildren().addAll(productName, productDetails);

                productBox.getChildren().add(productCard);
                productBox.setAlignment(Pos.CENTER); //

            }

            chatBox.getChildren().add(productBox);
        });
    }
    private static void createPromotionButtons() {
        Platform.runLater(() -> {
            // Conteneur pour les boutons
            HBox buttonBox = new HBox(10); // Espacement entre les boutons
            buttonBox.setPadding(new Insets(10));
            buttonBox.setStyle("-fx-background-color: white;");

            // Noms des promotions
            String[] promotionOptions = {
                    "Buy 1 Get 1 Free",
                    "20% off on winter clothing",
                    "Extra 10% off above $100"
            };

            VBox promotionDetailsBox = new VBox(30);
            promotionDetailsBox.setPadding(new Insets(10));

            // Ajout des boutons pour chaque promotion
            for (int i = 0; i < promotionOptions.length; i++) {
                String promotion = promotionOptions[i];
                Button promotionButton = new Button(promotion);
                promotionButton.setStyle("-fx-background-color: #6261af; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px;");

                final int promotionIndex = i + 1; // Index pour identifier la promotion
                promotionButton.setOnAction(e -> {
                    promotionDetailsBox.getChildren().clear(); // Efface les anciens détails
                    displayPromotionDetails(String.valueOf(promotionIndex));
                });

                buttonBox.getChildren().add(promotionButton);
            }
            chatBox.getChildren().addAll(buttonBox, promotionDetailsBox);
        });
    }
    private static void displayProductSearchOptions() {
        Platform.runLater(() -> {
            // Conteneur pour les boutons
            HBox buttonBox = new HBox(10); // Espacement entre les boutons
            buttonBox.setPadding(new Insets(30));
            buttonBox.setStyle("-fx-background-color: white;");

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
                optionButton.setStyle("-fx-background-color: #6261af; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px;");

                // Ajout de l'événement sur chaque bouton pour afficher les produits de la catégorie
                optionButton.setOnAction(e -> displayProductCards(searchOption, productGrid));

                buttonBox.getChildren().add(optionButton);
            }

            // Efface les anciens éléments et ajoute les nouveaux dans le chatBox
            chatBox.getChildren().addAll(buttonBox, productGrid);
        });
    }
    public static void displayMessage(String message, String sender) {
        Platform.runLater(() -> {
            HBox messageBox = new HBox(5);

            messageBox.setPadding(new Insets(10));
            messageBox.setAlignment(sender.equals("user") ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            Label bubble = new Label(message);
            bubble.setWrapText(true); // Permettre le retour à la ligne automatique

            bubble.setPadding(new Insets(10));
            bubble.setStyle(sender.equals("user")
                    ? "-fx-background-color: #6261af; -fx-text-fill: white; -fx-background-radius: 10px;"
                    : "-fx-background-color: #e0e0e0; -fx-text-fill: black; -fx-background-radius: 10px;");
            bubble.setWrapText(true);
            bubble.setMaxWidth(300);

            Label timeLabel = new Label(time);
            timeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: black;");
            VBox bubbleWithTime = new VBox(bubble, timeLabel);

            Label avatar = new Label(sender.equals("user") ? "👤" : "🤖");
            avatar.setStyle("-fx-border-color: white; -fx-border-radius: 5px;");
            if (sender.equals("user")) {

                messageBox.getChildren().addAll(bubbleWithTime, avatar);
            } else {
                bubble.setWrapText(true);
                bubble.setMaxWidth(500);

                bubble.setMaxHeight(400);


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