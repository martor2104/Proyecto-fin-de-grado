package com.api.webReservas.entity;

import java.util.Collection;
import java.util.List;

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
import lombok.Builder;
import lombok.Data;

import com.api.webReservas.auth.RegisterRequest;
import com.api.webReservas.dto.UserDTO;

@Entity(name = "users")
@Builder
@Data
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
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Role userRol;
	
    public User(Long id, String name, String email, String password, Role userRol) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRol = userRol;
    }
	
	public static UserDTO toDTO(User user) {
		return new UserDTO(
			    user.getId(),
			    user.getName(),
			    user.getEmail(),
			    user.getPassword(),
			    user.getUserRol()
			);
	}


	public User(RegisterRequest user) {
	    this.name = user.getName();
	    this.email = user.getEmail();
	    this.password = user.getPassword();
	    this.userRol = user.getUserRol();
	}

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

	public Role getUserRol() {
		return userRol;
	}

	public void setUserRol(Role userRol) {
		this.userRol = userRol;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority((userRol.name())));
	}

	@Override
	public String getPassword() {
		return password;
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



}
