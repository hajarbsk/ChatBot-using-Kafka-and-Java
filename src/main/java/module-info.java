module com.chatbot.chatbotapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires kafka.clients;


    opens com.chatbot.chatbotapp to javafx.fxml;
    exports com.chatbot.chatbotapp;
}