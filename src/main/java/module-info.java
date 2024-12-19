module com {
    requires javafx.base; // Si nécessaire pour JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires kafka.clients;
    requires com.google.gson ;

    opens com.ChatResponse.NLP to com.google.gson;
    requires org.apache.commons.csv;
    requires com.fasterxml.jackson.databind; // Pour Jackson

    requires java.desktop;

    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson; // Required for BSON operations
    requires org.mongodb.driver.core;
    requires edu.stanford.nlp.corenlp;
    requires org.json;

    opens com.GUI to javafx.fxml; // Pour le binding avec FXML
    exports com.Kafka;


    exports com.GUI;

}
