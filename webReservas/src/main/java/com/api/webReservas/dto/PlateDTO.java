package com.api.webReservas.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlateDTO {

	private Long id;
	private String namePlate;
	private String description;
	private Double price;
	
	public PlateDTO(Long id, String namePlate, String description, Double price) {
		this.id = id;
		this.namePlate = namePlate;
		this.description = description;
		this.price = price;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNamePlate() {
		return namePlate;
	}
	public void setNamePlate(String namePlate) {
		this.namePlate = namePlate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
	
}
