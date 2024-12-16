package com.GUI;

import org.bson.types.ObjectId;

public class Product {
    private ObjectId id;
    private Integer userId;
    private Integer productId;
    private String productName;
    private String brand;
    private String category;
    private Integer price;
    private double rating;
    private String color;
    private String size;

    // Constructeur
    public Product(ObjectId id, Integer userId, Integer productId, String productName, String brand,
                   String category, Integer price, double rating, String color, String size) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.brand = brand;
        this.category = category;
        this.price = price;
        this.rating = rating;
        this.color = color;
        this.size = size;
    }

    // Getters
    public ObjectId getId() { return id; }
    public Integer getUserId() { return userId; }
    public Integer getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getBrand() { return brand; }
    public String getCategory() { return category; }
    public Integer getPrice() { return price; }
    public double getRating() { return rating; }
    public String getColor() { return color; }
    public String getSize() { return size; }
}
