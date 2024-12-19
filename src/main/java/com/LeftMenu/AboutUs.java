package com.LeftMenu;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class AboutUs {
    public VBox getContent() {
        // Titre du magasin
        Label storeName = new Label("About Our Store ");
        storeName.setAlignment(Pos.CENTER_RIGHT);
        storeName.setStyle("-fx-font-size: 18px; -fx-background-color: #6261af; -fx-font-weight: bold; -fx-text-fill: white;");

        // Paragraphes descriptifs
        Text para1 = new Text("Fushion Products is your one-stop shop for high-quality, innovative, and affordable items.\n "
                + "We specialize in providing a diverse range of products, including sweaters, dresses, and more,\n "
                + "all carefully curated to meet your needs.\n");

        Text para2 = new Text("What makes us stand out?\n"
                + "- Unmatched quality: Our products are sourced from trusted suppliers.\n"
                + "- Exceptional customer service: We value your satisfaction above all.\n"
                + "- Competitive prices: Premium quality without breaking the bank.\n");

        Text para3 = new Text("Choose Fusion Products for a seamless shopping experience. Your happiness is our priority!");

        // Création du VBox avec espacement entre les éléments
        VBox vbox = new VBox(10); // 10px d'espacement entre les paragraphes
        vbox.getChildren().addAll(storeName, para1, para2, para3);
        vbox.setAlignment(Pos.TOP_LEFT);  // Aligner tout le contenu à gauche

        // Définir la bordure pour le VBox
        BorderStroke borderStroke = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT);
        Border border = new Border(borderStroke);
        vbox.setBorder(border);

        // Appliquer un fond clair pour le VBox et ajuster la largeur du texte
        vbox.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 20px; -fx-spacing: 10px;");

        return vbox;
    }
}
