package com.LeftMenu;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Help {
    public VBox getContent() {
        VBox content = new VBox();
        content.setStyle("-fx-padding: 20; -fx-background-color: #e3f2fd;");
        content.getChildren().add(new Button("Contenu pour Help"));
        return content;
    }
}


