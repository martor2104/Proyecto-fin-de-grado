package com.api.webReservas.dto;

import com.api.webReservas.entity.Role;

public class UserDTO {

	private Long id;
	private String name;
	private String email;
	private String password;
	private Role userRol;
	private String perfil;

	// Constructor con todos los campos
	public UserDTO(Long id, String name, String email, String password, Role userRol, String perfil) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.userRol = userRol;
		this.perfil = perfil;
	}

	// Constructor vacío
	public UserDTO() {
	}

	// Getters y Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getUserRol() {
		return userRol;
	}

	public void setUserRol(Role userRol) {
		this.userRol = userRol;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	@Override
	public String toString() {
		return "UserDTO{" +
				"id=" + id +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", userRol=" + userRol +
				", perfil='" + perfil + '\'' +
				'}';
	}
}
