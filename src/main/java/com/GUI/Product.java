package com.GUI;

public class Product {
    private String namePr;
    private String[] colors;
    private String[] sizes;
    private Double pricePr;
    private int quantite;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    private String gender;

    public Product(String namePr ,String[] colors, String[] sizes,Double pricePr, int quantite) {
        this.namePr = namePr;
        this.colors=colors;
        this.sizes=sizes;
        this.pricePr=pricePr;
        this.quantite=quantite;
    }

    public String getNamePr() {
        return namePr;
    }

    public void setNamePr(String namePr) {
        this.namePr = namePr;
    }

    public String[] getSizes() {
        return sizes;
    }

    public String[] getColors() {
        return colors;
    }

    public void setColors(String[] colors) {
        this.colors = colors;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public void setSizes(String[] sizes) {
        this.sizes = sizes;
    }

    public Double getPricePr() {
        return pricePr;
    }

    public void setPricePr(Double pricePr) {
        this.pricePr = pricePr;
    }
    public void displayDetails() {
        System.out.println("Product: " + namePr);
        System.out.println("Stock: " + quantite);
        System.out.println("Available colors: " + String.join(", ", colors));
        System.out.println("Available sizes: " + String.join(", ", sizes));
        System.out.println("Price: $" + pricePr);
        System.out.println("Gender: " + gender);
    }

}