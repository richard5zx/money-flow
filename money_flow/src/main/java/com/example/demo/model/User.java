package com.example.demo.model;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="users")
public class User {
	@Id
	@Column(columnDefinition = "uuid", updatable = false, nullable = false, name = "id")
	private UUID userId;
	
	@Column(nullable = false, name = "email")
	private String email;
	
	@Column(nullable = false, name = "password_hash")
	private String passwordHash;
	
	@Column(name = "full_name")
	private String fullName;
	
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	
	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false, updatable = false)
	private Instant updatedAt;
	

}
