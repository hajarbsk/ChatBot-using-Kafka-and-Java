package com.chatbot.chatbotapp.server;

import java.io.IOException;

public class ServerManager {

    private static Process zookeeperProcess;
    private static Process kafkaProcess;

    public static void startZookeeper() throws IOException {
        System.out.println("Démarrage de Zookeeper dans un terminal séparé...");

        String command = "cmd.exe /c start cmd.exe /k C:\\kafka\\bin\\windows\\zookeeper-server-start.bat src\\main\\resources\\zookeeper.properties";
        zookeeperProcess = Runtime.getRuntime().exec(command);
        System.out.println("Zookeeper démarré dans une nouvelle fenêtre de terminal.");
    }

    public static void startKafka() throws IOException {
        System.out.println("Démarrage de Kafka dans un terminal séparé...");
        String command = "cmd.exe /c start cmd.exe /k C:\\kafka\\bin\\windows\\kafka-server-start.bat src\\main\\resources\\server.properties";
        kafkaProcess = Runtime.getRuntime().exec(command);
        System.out.println("Kafka démarré dans une nouvelle fenêtre de terminal.");

    }



    public void startServer() {        try {
            startZookeeper(); // Lance Zookeeper dans un terminal
            Thread.sleep(5000); // Attendre un peu que Zookeeper soit prêt
            startKafka(); // Lance Kafka dans un terminal
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}