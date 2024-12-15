package com.api.webReservas.dto;

public class SuccessDTO {
    private String message;
    private Object data; // Opcional: para incluir datos adicionales en la respuesta

    // Constructor con solo mensaje
    public SuccessDTO(String message) {
        this.message = message;
    }

    // Constructor con mensaje y datos adicionales
    public SuccessDTO(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    // Getters y setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

