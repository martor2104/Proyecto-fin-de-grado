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
	private String img;
	private String category;

	public PlateDTO() {
	}
	

	public PlateDTO(Long id, String namePlate, String description, Double price, String img, String category) {

		this.id = id;
		this.namePlate = namePlate;
		this.description = description;
		this.price = price;
		this.img = img;
		this.category = category;
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
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
}
