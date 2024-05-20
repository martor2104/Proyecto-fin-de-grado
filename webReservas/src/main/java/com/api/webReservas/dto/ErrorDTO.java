package com.api.webReservas.dto;

import lombok.Value;

@Value
public class ErrorDTO {
	
    public ErrorDTO(String string) {
		this.error = string;
	}

	String error;
}

