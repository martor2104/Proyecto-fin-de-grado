package com.api.webReservas.dto;

public class PlateDTO {

	private Long id;
	private String namePlate;
	private String description;
	private Double price;
	private String img;
	private String category;

	// Constructor vacío
	public PlateDTO() {
	}

	// Constructor con todos los campos
	public PlateDTO(Long id, String namePlate, String description, Double price, String img, String category) {
		this.id = id;
		this.namePlate = namePlate;
		this.description = description;
		this.price = price;
		this.img = img;
		this.category = category;
	}

	// Getters y Setters

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

	@Override
	public String toString() {
		return "PlateDTO{" +
				"id=" + id +
				", namePlate='" + namePlate + '\'' +
				", description='" + description + '\'' +
				", price=" + price +
				", img='" + img + '\'' +
				", category='" + category + '\'' +
				'}';
	}
}
