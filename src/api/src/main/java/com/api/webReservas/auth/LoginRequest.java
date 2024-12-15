package com.api.webReservas.auth;

public class LoginRequest {

    private String name;
    private String password;

    // Constructor sin parámetros
    public LoginRequest() {
    }

    // Constructor con parámetros
    public LoginRequest(String name, String password) {
        this.name = name;
        this.password = password;
    }

    // Getters y Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
