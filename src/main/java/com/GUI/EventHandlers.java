package com.GUI;

import com.Kafka.KafkaProducerExample;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public abstract class EventHandlers {

    public static EventHandler<ActionEvent> onMessageSend() {
        return event -> sendMessage();
    }

    public static EventHandler<KeyEvent> onEnterPressed() {
        return event -> {
            if (event.getCode() == KeyCode.ENTER)
                sendMessage();
        };
    }

    private static void sendMessage() {
        String message = ChatBot.input.getText();

        if (!message.trim().equals("")) {
            // Afficher le message de l'utilisateur dans la zone de discussion
            ChatBot.chat.appendText("\n\nYou: " + message + "\n");

            // Envoyer le message au producteur Kafka
            KafkaProducerExample.sendMessage(message);

            // Effacer la zone de saisie
            ChatBot.input.clear();
        }
    }
}
