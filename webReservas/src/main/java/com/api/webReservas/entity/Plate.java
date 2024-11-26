package com.api.webReservas.entity;

import com.api.webReservas.dto.PlateDTO;
import com.api.webReservas.dto.UserDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "plates")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
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
  
	public Plate(PlateDTO plate) {
		this.id = plate.getId();
		this.namePlate = plate.getNamePlate();
		this.description = plate.getDescription();
		this.price = plate.getPrice();
	}

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
