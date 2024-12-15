package com.api.webReservas.entity;

import java.util.Collection;
import java.util.List;

import com.api.webReservas.auth.RegisterRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import com.api.webReservas.dto.UserDTO;

@Entity(name = "users")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column
	private String perfil;

	// Constructor sin parámetros
	public User() {
	}

	// Constructor con parámetros
	public User(Long id, String name, String email, String password, Role role, String perfil) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.perfil = perfil;
	}

	public User(RegisterRequest request) {
		this.name = request.getName();
		this.email = request.getEmail();
		this.password = request.getPassword();
		this.role = Role.valueOf(request.getRole().toUpperCase());
	}


	// Convertir un objeto User en un UserDTO
	public static UserDTO toDTO(User user) {
		return new UserDTO(
				user.getId(),
				user.getName(),
				user.getEmail(),
				user.getPassword(),
				user.getRole(),
				user.getPerfil()
		);
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	// Métodos de UserDetails (equivalentes a la interfaz)

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public String getUsername() {
		return name;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", role=" + role +
				", perfil='" + perfil + '\'' +
				'}';
	}
}
