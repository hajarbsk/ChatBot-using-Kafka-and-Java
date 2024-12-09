module com {
    requires javafx.base; // Si nécessaire pour JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires kafka.clients;
    requires com.google.gson ;
    requires org.apache.commons.csv;
    requires com.fasterxml.jackson.databind; // Pour Jackson

    requires java.desktop;


    opens com.GUI to javafx.fxml; // Pour le binding avec FXML
    exports com.Kafka;

    exports com.Traitement;
    exports com.GUI;
    exports com;
}
