package com.example.demo.users.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.users.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	
	/*
	Since I extended JpaRepository<User, UUID>, I already have:
		save(User user)	-> Insert or update a user
		findById(UUID id) -> Retrieve a user by ID
		findAll() -> List all users
		deleteById(UUID id) -> Delete a user by ID
		existsById(UUID id)	-> Check if a user exists
		count()	-> Count users
		
	*/
	
	// Find a user by email (e.g., for login or validation)
    Optional<User> findByEmail(String email);

    // Check if a user already exists with a given email
    boolean existsByEmail(String email);
}
