package com.GUI;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HybridSearch {

    private MongoClient mongoClient;
    private MongoCollection<Document> productCollection;
    private List<Map<String, String>> knowledgeBase = new ArrayList<>();

    // 1. Connect to MongoDB
    public void connectToMongoDB(String uri, String dbName, String collectionName) {
        try {
            mongoClient = MongoClients.create(uri);
            MongoDatabase database = mongoClient.getDatabase(dbName);
            productCollection = database.getCollection(collectionName);
            System.out.println("Connected to MongoDB.");
        } catch (Exception e) {
            System.err.println("Failed to connect to MongoDB: " + e.getMessage());
        }
    }

    // 2. Load knowledge.json
    public void loadKnowledgeBase(String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            BufferedReader br = new BufferedReader(reader);
            StringBuilder jsonContent = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                jsonContent.append(line);
            }

            // Parse JSON
            JSONArray jsonArray = new JSONArray(jsonContent.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Map<String, String> entry = new HashMap<>();
                entry.put("question", obj.getString("question"));
                entry.put("response", obj.getString("response"));
                knowledgeBase.add(entry);
            }
            System.out.println("Knowledge Base loaded successfully.");
        } catch (IOException e) {
            System.err.println("Failed to load knowledge base: " + e.getMessage());
        }
    }

    // 3. Normalize text
    private String normalizeText(String input) {
        if (input == null || input.isEmpty()) return "";
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "") // Remove accents
                .replaceAll("\\s+", " ")        // Reduce multiple spaces
                .trim()
                .toLowerCase();
    }
    private String normalizeText2(String text) {
        // Liste des stopwords communs
        Set<String> stopwords = new HashSet<>(Arrays.asList(
                "what", "do", "you", "we", "is", "the", "a", "an", "to", "of", "in",
                "and", "for", "on", "at", "this", "that", "it", "with", "can", "i", "be", "are"
        ));

        // Mise en minuscule et suppression de la ponctuation
        String normalizedText = text.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "");

        // Suppression des stopwords
        return Arrays.stream(normalizedText.split(" "))
                .filter(word -> !stopwords.contains(word) && word.length() > 1)
                .collect(Collectors.joining(" "));
    }


    public String findSimilarResponse(String userQuestion) {
        // Normaliser la question de l'utilisateur
        String normalizedUserQuestion = normalizeText2(userQuestion);
        List<String> userKeywords = Arrays.asList(normalizedUserQuestion.split(" "));

        int maxCommonWords = 0;
        String bestResponse = null;

        // Parcourir la base de connaissances
        for (Map<String, String> entry : knowledgeBase) {
            // Normaliser chaque question de la base de connaissances
            String normalizedQuestion = normalizeText2(entry.get("question"));
            List<String> questionKeywords = Arrays.asList(normalizedQuestion.split(" "));

            // Trouver les mots-clés communs
            long commonWords = userKeywords.stream()
                    .filter(questionKeywords::contains)
                    .count();

            // Vérifier si cette question est la meilleure correspondance
            if (commonWords > maxCommonWords) {
                maxCommonWords = (int) commonWords;
                bestResponse = entry.get("response");
            }
        }

        // Retourner la réponse si des mots communs sont trouvés
        return (maxCommonWords > 0) ? bestResponse : "I'm sorry, I couldn't find a matching response.";
    }

    public Map<String, String> extractKeywords(String userQuestion) {
        // Normaliser la question de l'utilisateur
        String normalizedUserQuestion = normalizeText(userQuestion).toLowerCase();

        // Listes de mots-clés
        String[] colors = {"red", "blue", "green", "black", "white", "yellow"};
        String[] sizes = {"xl", "s", "m", "l"};
        String[] categories = {"women", "men", "kids"};
        String[] productNames = {"jeans", "sweater","t-shirt", "dress", "shoes"};

        // Initialisation d'une map pour stocker les mots-clés extraits
        Map<String, String> keywords = new HashMap<>();

        // Extraire la couleur
        String color = extractKeyword(normalizedUserQuestion, colors);
        if (color != null) keywords.put("color", color);

        // Extraire la taille
        String size = extractKeyword(normalizedUserQuestion, sizes);
        if (size != null) keywords.put("size", size);

        // Extraire la catégorie
        String category = extractKeyword(normalizedUserQuestion, categories);
        if (category != null) keywords.put("category", category);

        // Extraire le nom du produit
        String productName = extractKeyword(normalizedUserQuestion, productNames);
        if (productName != null) keywords.put("productName", productName);

        return keywords;
    }

    // Helper function to extract a keyword from the user question using regular expression
    private String extractKeyword(String normalizedUserQuestion, String[] keywordList) {
        for (String keyword : keywordList) {
            Pattern pattern = createPattern(keyword);
            Matcher matcher = pattern.matcher(normalizedUserQuestion);
            if (matcher.find()) {
                return keyword;
            }
        }
        return null;
    }

    // Helper function to create a Pattern for matching the keyword
    private Pattern createPattern(String keyword) {
        return Pattern.compile("\\b" + keyword + "\\b", Pattern.CASE_INSENSITIVE);
    }
    // Search for products based on the extracted keywords
    public List<Document> searchProducts(Map<String, String> keywords) {
        List<Bson> filters = new ArrayList<>();

        // Ajouter les filtres pour les différents mots-clés extraits
        if (keywords.containsKey("color")) {
            filters.add(Filters.regex("Color", ".*" + keywords.get("color") + ".*", "i"));
        }
        if (keywords.containsKey("size")) {
            filters.add(Filters.regex("Size", ".*" + keywords.get("size") + ".*", "i"));
        }
        if (keywords.containsKey("category")) {
            filters.add(Filters.regex("Category", ".*" + keywords.get("category") + ".*", "i"));
        }
        if (keywords.containsKey("productName")) {
            filters.add(Filters.regex("Product Name", ".*" + keywords.get("productName") + ".*", "i"));
        }

        // Exécuter la requête si des filtres existent
        List<Document> results = new ArrayList<>();
        if (!filters.isEmpty()) {
            Bson query = Filters.and(filters);
            results = productCollection.find(query).limit(10).into(new ArrayList<>());
        }

        return results;
    }

    // Fonction pour normaliser le texte (enlever les accents et les espaces multiples)

    public boolean isGreeting(String userQuestion) {
        if (userQuestion == null || userQuestion.isEmpty()) return false;

        // Liste des salutations communes
        List<String> greetings = Arrays.asList("hello", "hi", "hey", "good morning", "good evening","hey there","can you help me");

        // Transformez la question en minuscules pour la comparer
        String normalizedQuestion = userQuestion.toLowerCase().trim();

        // Vérifiez si la question contient exactement un mot de salutation
        return greetings.stream().anyMatch(normalizedQuestion::startsWith);
    }



    public String getResponse(String userQuestion) {
        connectToMongoDB("mongodb://localhost:27017", "chatbot", "produits");
        loadKnowledgeBase("src/main/resources/knowledge_base.json");

        // Vérifier si la question de l'utilisateur est une salutation
        if (isGreeting(userQuestion)) {
            return "Hello! How can I assist you?";  // Retourner une salutation et sortir de la méthode
        }

        // Étape 1 : Extraire les mots-clés de la question de l'utilisateur
        Map<String, String> keywords = extractKeywords(userQuestion);

        // Étape 2 : Recherche de produits dans MongoDB selon les mots-clés extraits
        List<Document> products = searchProducts(keywords);

        // Si des produits sont trouvés, les convertir en objets Product et formater l'affichage
        if (!products.isEmpty()) {
            StringBuilder response = new StringBuilder("Similar products found :\n");
            for (Document product : products) {
                // Récupérer les champs du produit
                String productName = product.getString("Product Name");
                String brand = product.getString("Brand");
                String category = product.getString("Category");
                Double price = Double.valueOf(product.getInteger("Price"));
                Double rating = product.getDouble("Rating");
                String color = product.getString("Color");
                String size = product.getString("Size");

                // Vérifier si les champs sont non null
                if (productName != null && brand != null && category != null && price != null) {
                    response.append("- Product: ").append(productName)
                            .append(" | Brand: ").append(brand)
                            .append(" | Category: ").append(category)
                            .append(" | Price: ").append(price).append(" €")
                            .append(" | Rating: ").append(String.format("%.2f", rating))
                            .append(" | Color: ").append(color)
                            .append(" | Size: ").append(size)
                            .append("\n");
                } else {
                    response.append("-No Product in stock !!!\n");
                }
            }

            return response.toString();
        } else {
            // Étape 3 : Si aucun produit n'est trouvé, chercher une réponse similaire dans la base de connaissances
            String response = findSimilarResponse(userQuestion);
            if (response != null) {
                return response;  // Retourner la réponse similaire trouvée
            } else {
                return "Désolé, je ne comprends pas votre question. Pouvez-vous être plus précis ?";
            }
        }
    }
    /*public static void main(String[] args) {
        HybridSearch hybridSearch = new HybridSearch();

        // Connect to MongoDB
        hybridSearch.connectToMongoDB("mongodb://localhost:27017", "chatbot", "produits");

        // Load knowledge base (optional, only if needed)
        hybridSearch.loadKnowledgeBase("src/main/resources/knowledge_base.json");

        // User interaction
        Scanner scanner = new Scanner(System.in);
        System.out.println("Posez votre question : ");
        String userQuestion = scanner.nextLine();

        // Check if the user input is a greeting
        if (hybridSearch.isGreeting(userQuestion)) {
            System.out.println("Hello! How can I assist you?");
            // Continue processing instead of returning
        }
        // Step 1: Extract keywords from user question
        Map<String, String> keywords = hybridSearch.extractKeywords(userQuestion);

        // Step 2: Search products in MongoDB based on extracted keywords
        List<Document> products = hybridSearch.searchProducts(keywords);

        if (!products.isEmpty()) {
            System.out.println("Produits similaires trouvés :");
            for (Document product : products) {
                System.out.println(product.toJson());
            }
        } else {
            // Step 3: If no products are found, try finding a similar response in the knowledge base
            String response = hybridSearch.findSimilarResponse(userQuestion);
            if (response != null) {
                System.out.println( response);
            } else {
                System.out.println("Désolé, je ne comprends pas votre question. Pouvez-vous être plus précis ?");
            }
        }
    }*/

}
