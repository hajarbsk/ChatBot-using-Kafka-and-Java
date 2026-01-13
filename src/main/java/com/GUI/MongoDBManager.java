package com.GUI;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoDBManager {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017"; // URL de votre serveur MongoDB
    private static final String DATABASE_NAME = "chatbot"; // Nom de votre base
    private static final String COLLECTION_NAME = "produits"; // Nom de votre collection

    // Méthode pour récupérer les produits par catégorie
    public List<Product> getProductsByCategory(String category) {
        List<Product> products = new ArrayList<>();

        try (MongoClient client = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = client.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Test de la connexion
            System.out.println("Connexion réussie à MongoDB");

            // Vérification des catégories disponibles dans la collection
            List<String> categories = new ArrayList<>();
            collection.find().forEach(doc -> {
                String cat = doc.getString("Category");
                if (cat != null && !categories.contains(cat)) {
                    categories.add(cat);
                }
            });

            System.out.println("Catégories disponibles : " + categories);

            // Requête simplifiée sans regex pour rechercher par catégorie exacte
            List<Document> documents = collection.find(new Document("Category", category)).limit(9).into(new ArrayList<>());

            // Vérifier si des produits sont trouvés dans la catégorie spécifiée
            if (documents.isEmpty()) {
                System.out.println("Aucun produit trouvé dans la catégorie : " + category);
            }

            for (Document doc : documents) {
                Product product = new Product(
                        doc.getObjectId("_id"),
                        doc.getInteger("User ID"),
                        doc.getInteger("Product ID"),
                        doc.getString("Product Name"),
                        doc.getString("Brand"),
                        doc.getString("Category"),
                        doc.getInteger("Price"),
                        doc.getDouble("Rating"),
                        doc.getString("Color"),
                        doc.getString("Size")
                );
                products.add(product);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }
}
