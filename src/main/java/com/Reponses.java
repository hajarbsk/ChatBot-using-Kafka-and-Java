package com;
import java.util.Optional;
public class Reponses {

    private static final String KNOWLEDGE_BASE_PATH = Reponses.class.getClassLoader().getResource("knowledge_base.json").getPath();
        private KnowledgeBase knowledgeBase;

        public Reponses() {
            this.knowledgeBase = new KnowledgeBase(KNOWLEDGE_BASE_PATH);
        }

        public String respond(String userInput) {
            // Recherche de la question dans la base
            Optional<QuestionAnswer> possibleAnswer = knowledgeBase.findByQuestion(userInput);
            if (possibleAnswer.isPresent()) {
                return possibleAnswer.get().getResponse();
            }

            // Si la question n'existe pas, générer une réponse et l'enregistrer
            String generatedResponse = generateResponse(userInput);
            saveNewQuestion(userInput, generatedResponse);
            return generatedResponse;
        }

        private String generateResponse(String userInput) {
            // Exemple de génération simplifiée
            return "Désolé, je n'ai pas trouvé de réponse. Je vais transmettre cette question.";
        }

        private void saveNewQuestion(String question, String response) {
            // Fonctionnalité d'ajout dans le fichier JSON
            System.out.println("Sauvegarde de la nouvelle question : " + question);
            // Pour une version avancée, implémentez la mise à jour du fichier JSON.
        }
    }
