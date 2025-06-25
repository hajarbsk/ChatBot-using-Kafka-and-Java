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
        Label storeName = new Label("Fashion Products");
        storeName.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Contact info
        Text contactInfo = new Text("If you have any questions, feel free to reach out to us:");
        contactInfo.setStyle("-fx-font-size: 16px; -fx-text-fill: #444;");

        ImageView whatplogo = new ImageView(new Image("file:src/main/resources/whtp.png"));
        whatplogo.setFitWidth(20);
        whatplogo.setFitHeight(20);

        Text phoneNumber = new Text(" Phone: +212 123 456 789");
        phoneNumber.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;-fx-border");
        HBox whtBox = new HBox(10, whatplogo, phoneNumber);
        whtBox.setAlignment(Pos.CENTER);

        // Message input field
        TextField messageField = new TextField();
        TextField emailField = new TextField();


        messageField.setPromptText("Write your message here...");
        messageField.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

        emailField.setPromptText("Your email...");
        emailField.setStyle("-fx-font-size: 8px; -fx-padding: 10px;");

        Button sendButton = new Button("Send");
        sendButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-font-size: 14px;");

        // Action for send button
        sendButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message Sent");
            alert.setHeaderText(null);
            alert.setContentText("Your message has been received and will be processed by our store server.");
            alert.showAndWait();
            System.out.println("Message sent to manager!!!");
        });

        HBox messageBox = new HBox(10,messageField, emailField, sendButton);
        messageBox.setAlignment(Pos.CENTER);

        // Social media logos and labels
        ImageView facebookLogo = new ImageView(new Image("file:src/main/resources/faceb.png"));
        facebookLogo.setFitWidth(40);
        facebookLogo.setFitHeight(40);


        ImageView telegramLogo = new ImageView(new Image("file:src/main/resources/twiter.png"));
        telegramLogo.setFitWidth(40);
        telegramLogo.setFitHeight(40);

        ImageView instalogo = new ImageView(new Image("file:src/main/resources/insta.png"));
        instalogo.setFitWidth(40);
        instalogo.setFitHeight(40);

        Label facebookLabel = new Label("Fushion_Products");
        facebookLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

        Label instagramLabel = new Label("Fushion_Products");
        instagramLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

        Label instalabel = new Label("Fushion_Products");
        instalabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

        HBox socialBox1 = new HBox(10, facebookLogo, facebookLabel);
        HBox sociaBox2=new HBox(10,instalogo,instalabel);
        HBox socialBox3 = new HBox(10, telegramLogo, instagramLabel);

        socialBox1.setAlignment(Pos.CENTER);
        sociaBox2.setAlignment(Pos.CENTER);
        socialBox3.setAlignment(Pos.CENTER);

        VBox socialBox = new VBox(10, socialBox1,sociaBox2, socialBox3);
        socialBox.setAlignment(Pos.CENTER);

        // Layout
        VBox contactUsBox = new VBox(20, storeName, contactInfo, whtBox, messageBox, socialBox);
        contactUsBox.setAlignment(Pos.CENTER);
        contactUsBox.setStyle("-fx-padding: 20px;");

        return contactUsBox;
    }


}


