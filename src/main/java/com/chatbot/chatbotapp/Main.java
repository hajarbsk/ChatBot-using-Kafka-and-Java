package com.chatbot.chatbotapp;

import com.chatbot.chatbotapp.consumer.KafkaConsumerExample;
import com.chatbot.chatbotapp.producer.KafkaProducerExample;
import com.chatbot.chatbotapp.server.ServerManager;

public class Main {
    public static void main(String[] args) {
        //Thread pour demarrer kafka et producer
        new Thread(() -> {
            ServerManager serverManager = new ServerManager();
            serverManager.main(); //
        }).start();
        // Thread pour démarrer le Producer
        new Thread(() -> {
            KafkaProducerExample producer = new KafkaProducerExample();
            producer.runProducerLogic();  // Appelle une méthode dédiée à la logique du producer
        }).start();

        // Thread pour démarrer le Consumer
        new Thread(() -> {
            KafkaConsumerExample consumer = new KafkaConsumerExample();
            consumer.runConsumerLogic();  // Appelle une méthode dédiée à la logique du consumer
        }).start();
    }
}
