package com.LeftMenu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Localisation{
    public VBox getContent() {
        // 1. Ajouter le texte de localisation
        Label locationLabel = new Label("Our store is located next to Sidi Bouafif School in Boukidan, Al Hoceima.");
        locationLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #6261af; -fx-font-family: 'Serif';");

// 2. Ajouter les images sous forme de HBox défilant horizontalement
        Image image1 = new Image("file:src/main/resources/store1.jpg");
        Image image2 = new Image("file:src/main/resources/store2.jpg");
        Image image3 = new Image("file:src/main/resources/store3.jpg");

        ImageView imageView1 = new ImageView(image1);
        imageView1.setFitWidth(300);
        imageView1.setFitHeight(300);
        ImageView imageView2 = new ImageView(image2);
        imageView2.setFitWidth(300);
        imageView2.setFitHeight(300);
        ImageView imageView3 = new ImageView(image3);
        imageView3.setFitWidth(300);
        imageView3.setFitHeight(300);

// Créer un HBox pour afficher les images horizontalement
        HBox imagesBox = new HBox(10, imageView1, imageView2, imageView3);
        imagesBox.setAlignment(Pos.CENTER);
        imagesBox.setPadding(new Insets(10));

// 3. Ajouter un ScrollPane pour permettre le défilement horizontal des images
        ScrollPane imagesScrollPane = new ScrollPane(imagesBox);
        imagesScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Toujours afficher la barre de défilement horizontale
        imagesScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Ne pas afficher la barre de défilement verticale
        imagesScrollPane.setFitToWidth(true); // Ajuste la taille des images

// 4. Ajouter ces éléments à votre interface
        VBox locationContent = new VBox(10, locationLabel, imagesScrollPane);
        locationContent.setPadding(new Insets(20));
        locationContent.setAlignment(Pos.CENTER);

        return locationContent;}
}

