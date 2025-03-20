package com.example.gk_ltdd.model;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String image;
    private boolean isActivated;

    public User(int id, String username, String email, String password, String image, boolean isActivated) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.image = image;
        this.isActivated = isActivated;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getImage() { return image; }
    public boolean isActivated() { return isActivated; }
}
