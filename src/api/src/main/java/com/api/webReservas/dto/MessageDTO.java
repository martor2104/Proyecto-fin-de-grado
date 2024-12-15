package com.api.webReservas.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
public class MessageDTO {
	
    public MessageDTO(String message) {
    	this.message = message;
	}

	String message;
}
