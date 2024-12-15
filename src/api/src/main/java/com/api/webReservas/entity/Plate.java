package com.api.webReservas.entity;

import com.api.webReservas.dto.PlateDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "plates")
public class Plate {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name_plate", nullable = false)
	private String namePlate;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private Double price;

	@Column
	private String img;

	@Column
	private String category;

	// Constructor vacío
	public Plate() {
	}

	// Constructor con todos los campos
	public Plate(Long id, String namePlate, String description, Double price, String img, String category) {
		this.id = id;
		this.namePlate = namePlate;
		this.description = description;
		this.price = price;
		this.img = img;
		this.category = category;
	}

	// Constructor que toma un DTO como parámetro
	public Plate(PlateDTO plate) {
		this.id = plate.getId();
		this.namePlate = plate.getNamePlate();
		this.description = plate.getDescription();
		this.price = plate.getPrice();
		this.img = plate.getImg();
		this.category = plate.getCategory();
	}

	// Metodo estático que convierte un Plate en un PlateDTO
	public static PlateDTO toDTO(Plate plate) {
		if (plate == null) {
			return null;
		}
		return new PlateDTO(
				plate.getId(),
				plate.getNamePlate(),
				plate.getDescription(),
				plate.getPrice(),
				plate.getImg(),
				plate.getCategory()
		);
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
		return "Plate{" +
				"id=" + id +
				", namePlate='" + namePlate + '\'' +
				", description='" + description + '\'' +
				", price=" + price +
				", img='" + img + '\'' +
				", category='" + category + '\'' +
				'}';
	}
}
