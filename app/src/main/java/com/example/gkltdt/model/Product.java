package com.example.gkltdt.model;

public class Product {
    private int id;
    private String name;
    private double price;
    private byte[] image;
    private int categoryId;

    public Product(int id, String name, double price, byte[] image, int categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.categoryId = categoryId;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public byte[] getImage() {
        return image;
    }

    public int getCategoryId() {
        return categoryId;
    }
}
