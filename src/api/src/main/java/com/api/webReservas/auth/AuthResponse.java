package com.api.webReservas.auth;

public class AuthResponse {

    private String token;
    private String profileImageUrl;

    // Constructor sin parámetros
    public AuthResponse() {
    }

    // Constructor con parámetros
    public AuthResponse(String token, String profileImageUrl) {
        this.token = token;
        this.profileImageUrl = profileImageUrl;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "token='" + token + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                '}';
    }
}
