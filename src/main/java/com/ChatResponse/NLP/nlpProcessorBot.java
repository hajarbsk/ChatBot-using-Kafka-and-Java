package com.ChatResponse.NLP;

import com.google.gson.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.CoreLabel;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class nlpProcessorBot {

    // Structure pour stocker les intents
    static class Intent {
        String tag;
        List<String> patterns;
        List<String> responses;
    }

    // Méthode pour charger les intents de manière asynchrone
    private static void loadIntentsAsync(String intentsFile, List<Intent> intents, StanfordCoreNLP pipeline, CountDownLatch latch) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                String json = new String(Files.readAllBytes(Paths.get(intentsFile))); // Lire le fichier JSON des intents
                JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

                for (JsonElement element : jsonObject.getAsJsonArray("intents")) {
                    JsonObject intentJson = element.getAsJsonObject();
                    Intent intent = new Intent();
                    intent.tag = intentJson.get("tag").getAsString();
                    intent.patterns = jsonArrayToList(intentJson.getAsJsonArray("patterns"));
                    intent.responses = jsonArrayToList(intentJson.getAsJsonArray("responses"));

                    // Prétraitement des patterns pour optimisation
                    intent.patterns = intent.patterns.stream()
                            .map(pattern -> tokenize(pattern, pipeline)) // Tokenisation des patterns
                            .map(tokens -> String.join(" ", tokens)) // Recréer le texte tokenisé
                            .collect(Collectors.toList());

                    synchronized (intents) {
                        intents.add(intent);
                    }
                }
            } catch (Exception e) {
                System.err.println("Failed to load intents: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });
        executor.shutdown(); // Assurez-vous de fermer le service après son utilisation
    }

    // Répondre à la demande de l'utilisateur
    public String respond(String userInput) {
        List<Intent> intents = new ArrayList<>();
        StanfordCoreNLP pipeline = null;
        CountDownLatch latch = new CountDownLatch(1); // Compteur pour attendre le chargement des intents

        try {
            // Charger StanfordCoreNLP avec ses propriétés
            InputStream is = getClass().getClassLoader().getResourceAsStream("StanfordCoreNLP.properties");
            if (is == null) {
                throw new RuntimeException("Fichier de propriétés StanfordCoreNLP.properties introuvable.");
            }
            Properties props = new Properties();
            props.load(is);
            pipeline = new StanfordCoreNLP(props);

            // Charger les intents de manière asynchrone
            loadIntentsAsync("src/main/resources/intents.json", intents, pipeline, latch);

            // Attendre que les intents soient chargés
            latch.await(); // Attente que le latch soit décrémenté (lorsque les intents sont chargés)

        } catch (Exception e) {
            e.printStackTrace();
            return "Désolé, une erreur est survenue lors du traitement de votre message.";
        }

        // Obtenir la meilleure réponse à partir de l'input de l'utilisateur
        String bestResponse = getBestResponse(userInput, intents, pipeline);
        return bestResponse;
    }

    // Trouver la meilleure réponse en fonction de la similarité
    private static String getBestResponse(String userInput, List<Intent> intents, StanfordCoreNLP pipeline) {
        String bestMatch = "Désolé, je n'ai pas compris votre demande.";
        double highestScore = 0.0;

        List<String> userTokens = tokenize(userInput, pipeline); // Tokeniser la question utilisateur

        for (Intent intent : intents) {
            for (String pattern : intent.patterns) {
                List<String> patternTokens = tokenize(pattern, pipeline); // Tokeniser le pattern

                double score = calculateSimilarity(userTokens, patternTokens); // Calculer la similarité entre les tokens

                if (score > highestScore) {
                    highestScore = score;
                    bestMatch = intent.responses.get(new Random().nextInt(intent.responses.size())); // Sélectionner une réponse aléatoire
                }
            }
        }

        return bestMatch;
    }

    // Tokenisation avec Stanford CoreNLP
    private static List<String> tokenize(String text, StanfordCoreNLP pipeline) {
        CoreDocument document = new CoreDocument(text.toLowerCase());
        pipeline.annotate(document);
        return document.tokens().stream().map(CoreLabel::word).collect(Collectors.toList());
    }

    // Calcul de la similarité entre les tokens
    private static double calculateSimilarity(List<String> tokens1, List<String> tokens2) {
        Set<String> allTokens = new HashSet<>(tokens1);
        allTokens.addAll(tokens2);

        int[] vector1 = vectorize(tokens1, allTokens);
        int[] vector2 = vectorize(tokens2, allTokens);

        return cosineSimilarity(vector1, vector2); // Calcul de la similarité cosinus
    }

    // Convertir les tokens en vecteurs
    private static int[] vectorize(List<String> tokens, Set<String> allTokens) {
        int[] vector = new int[allTokens.size()];
        int i = 0;
        for (String token : allTokens) {
            vector[i++] = Collections.frequency(tokens, token);
        }
        return vector;
    }

    // Calcul de la similarité cosinus
    private static double cosineSimilarity(int[] vector1, int[] vector2) {
        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;

        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
            magnitude1 += Math.pow(vector1[i], 2);
            magnitude2 += Math.pow(vector2[i], 2);
        }
        return dotProduct / (Math.sqrt(magnitude1) * Math.sqrt(magnitude2));
    }

    // Convertir JSONArray en List
    private static List<String> jsonArrayToList(JsonArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            list.add(element.getAsString());
        }
        return list;
    }
}
