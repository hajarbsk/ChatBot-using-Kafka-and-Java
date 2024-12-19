package com.LeftMenu;


import javafx.application.HostServices;
import javafx.geometry.Pos;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class ContactUs{
    public VBox getContent() {
        // Header with store name
        Label storeName = new Label("Fusion Products");
        storeName.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Contact info
        Text contactInfo = new Text("If you have any questions, feel free to reach out to us:");
        contactInfo.setStyle("-fx-font-size: 16px; -fx-text-fill: #444;");

        Text phoneNumber = new Text("📞 Phone: +212 123 456 789");
        phoneNumber.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;-fx-border");

        // Message input field
        TextField messageField = new TextField();
        messageField.setPromptText("Write your message here...");
        messageField.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

        Button sendButton = new Button("Send");
        sendButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-font-size: 14px;");

        // Action for send button
        sendButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message Sent");
            alert.setHeaderText(null);
            alert.setContentText("Your message has been received and will be processed by our store server.");
            alert.showAndWait();
        });

        HBox messageBox = new HBox(10, messageField, sendButton);
        messageBox.setAlignment(Pos.CENTER);

        // Social media logos and labels
        ImageView facebookLogo = new ImageView(new Image("file:src/main/resources/faceb.png"));
        facebookLogo.setFitWidth(40);
        facebookLogo.setFitHeight(40);


        ImageView telegramLogo = new ImageView(new Image("file:src/main/resources/twiter.png"));
        telegramLogo.setFitWidth(40);
        telegramLogo.setFitHeight(40);

        Label facebookLabel = new Label("Fushion_Products_Officiel");
        facebookLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

        Label instagramLabel = new Label("Fushion_Products_Officiel");
        instagramLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

        Label telegramLabel = new Label("Fushion_Products_Officiel");
        telegramLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

        HBox socialBox1 = new HBox(10, facebookLogo, facebookLabel);
        HBox socialBox3 = new HBox(10, telegramLogo, telegramLabel);

        socialBox1.setAlignment(Pos.CENTER);
        socialBox3.setAlignment(Pos.CENTER);

        VBox socialBox = new VBox(10, socialBox1, socialBox3);
        socialBox.setAlignment(Pos.CENTER);

        // Layout
        VBox contactUsBox = new VBox(20, storeName, contactInfo, phoneNumber, messageBox, socialBox);
        contactUsBox.setAlignment(Pos.CENTER);
        contactUsBox.setStyle("-fx-padding: 20px;");

        return contactUsBox;
    }


}


