package com.Traitement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ExcelToSQL {
    public static void main(String[] args) {
        // Chemin vers le fichier CSV
        String csvFilePath = "C:\\Users\\faty\\Desktop\\JAVA\\fashion_products.csv";

        // URL de connexion à votre base de données
        String jdbcURL = "jdbc:mysql://localhost:3306/chatbot"; // Nom de la base de données
        String dbUser = "root"; // Votre utilisateur MySQL
        String dbPassword = ""; // Votre mot de passe MySQL

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            // Connexion à la base de données
            Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);

            // Requête SQL pour insérer les données
            String insertQuery = "INSERT INTO product (user_id, product_id, product_name, brand, category, price, rating, color, size) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                // Ignorer la première ligne (les en-têtes)
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // Diviser la ligne par le séparateur
                String[] values = line.split(",");

                // Assurez-vous que toutes les colonnes sont présentes
                if (values.length >= 9) {
                    preparedStatement.setInt(1, Integer.parseInt(values[0].trim())); // User ID
                    preparedStatement.setInt(2, Integer.parseInt(values[1].trim())); // Product ID
                    preparedStatement.setString(3, values[2].trim());               // Product Name
                    preparedStatement.setString(4, values[3].trim());               // Brand
                    preparedStatement.setString(5, values[4].trim());               // Category
                    preparedStatement.setDouble(6, Double.parseDouble(values[5].trim())); // Price
                    preparedStatement.setDouble(7, Double.parseDouble(values[6].trim())); // Rating
                    preparedStatement.setString(8, values[7].trim());               // Color
                    preparedStatement.setString(9, values[8].trim());               // Size

                    // Exécuter la requête
                    preparedStatement.executeUpdate();
                }
            }

            System.out.println("Données insérées dans la table SQL avec succès !");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}