package com.chatbot.chatbotapp.Traitement;

public class chatbot {
    public String respond(String question) {
        // Exemple basique : répondre en fonction de mots-clés
        if (question.contains("promotion")) {
            return "Les produits A et B sont en promotion.";
        } else if (question.contains("horaires")) {
            return "Le magasin est ouvert de 9h à 18h.";
        } else {
            return "Je suis désolé, je ne connais pas la réponse à cette question.";
        }
    }
}
