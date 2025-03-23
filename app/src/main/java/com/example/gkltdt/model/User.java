package com.example.gkltdt.model;

public class User {
    private String username;
    private String email;
    private String password;
    private byte[] image;

    // Constructor
    public User(String username, String email, String password, byte[] image) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.image = image;
    }

    // Getter và Setter cho username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter và Setter cho email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter và Setter cho password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter và Setter cho image (mảng byte)
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

