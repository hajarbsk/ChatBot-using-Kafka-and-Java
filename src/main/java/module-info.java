module com.chatbot.chatbotapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires kafka.clients;


    opens com.chatbot.chatbotapp to javafx.fxml;
    exports com.chatbot.chatbotapp;
    exports com.chatbot.chatbotapp.consumer;
    opens com.chatbot.chatbotapp.consumer to javafx.fxml;
    exports com.chatbot.chatbotapp.producer;
    opens com.chatbot.chatbotapp.producer to javafx.fxml;
}