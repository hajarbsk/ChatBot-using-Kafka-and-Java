package com.LeftMenu;

import com.GUI.Product;
import com.server.ServerManager;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AboutUs  extends Application {

    private static VBox aboutUs;
    @Override
    public void start(Stage primaryStage) {

        // Zone de chat (VBox)
        aboutUs = new VBox(10);
        aboutUs.setPadding(new Insets(10));
        aboutUs.setStyle("-fx-background-color: #fbfbfb; -fx-font-size: 13px;");
        aboutUs.setPrefHeight(430);
        aboutUs.setPrefWidth(500);


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
            aboutUs.getChildren().clear();
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


        ScrollPane scrollPane = new ScrollPane();
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
        aboutUs.setStyle(isDarkMode ? lightModeChatBoxStyle : darkModeChatBoxStyle);
        headerBox.setStyle(isDarkMode ? lightModeStyle : darkModeStyle);
    }






    public static void main(String[] args) {
        launch(args);
    }
}

