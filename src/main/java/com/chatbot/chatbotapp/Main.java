package com.chatbot.chatbotapp;

public class Main {
    public static void main(String[] args) {
        // Lancer le consommateur dans un thread séparé
        new Thread(KafkaConsumerExample::listenForMessages).start();

        // Lancer l'application JavaFX
        ChatbotApp.main(args);
    }
}
