package com.GUI;

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

            ChatBot.chat.appendText("\n\nYou: " + message + "\n");

            String botReply = getBotReply(message);
            ChatBot.chat.appendText("Bot: " + botReply + "\n\n");

            // Efface le texte de la zone d'entrée
            ChatBot.input.clear();
        }

    }



    // Méthode fictive pour simuler une réponse du bot en mode hors ligne
    private static String getBotReply(String userMessage) {
        return "  noha w rajaa l97ibat .";
    }
}
