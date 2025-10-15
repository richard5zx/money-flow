package com.example.demo.users.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.users.model.User;
import com.example.demo.users.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository ur;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	@Transactional
	public User create(String email, String rawPassword, String fullName) {
		if (email == null || email.isBlank()) throw new IllegalArgumentException("email is required");
        if (rawPassword == null || rawPassword.isBlank()) throw new IllegalArgumentException("password is required");
        if (ur.existsByEmail(email)) throw new EmailAlreadyUsedException(email);

        User user = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .fullName(fullName)
                .build();

        return ur.save(user);
	}

	@Override
	public User getById(UUID id) {
		return ur.findById(id).orElseThrow(() -> new UserNotFoundException(id));
	}

	@Override
	public Page<User> list(Pageable pageable) {
		return ur.findAll(pageable);
	}

	@Override
	@Transactional
	public User updateUser(UUID id, String email, String fullName) {
		
		User user = ur.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		
		if (email != null && !email.isBlank()) {
			// Check if email already exists
			if (!email.equalsIgnoreCase(user.getEmail()) && ur.existsByEmail(email)) {
				throw new EmailAlreadyUsedException(email);
			}
			
			user.setEmail(email);
			
		}
		if (fullName != null) user.setFullName(fullName);
		
		return ur.save(user);
	}

	@Override
	@Transactional
	public void deleteUser(UUID id) {
		if (!ur.existsById(id)) throw new UserNotFoundException(id);
		ur.deleteById(id);
	}

	@Override
	@Transactional
	public void changePassword(UUID id, String newRawPassword) {
		if (newRawPassword == null || newRawPassword.isBlank())
			throw new IllegalArgumentException("Password is required");
		
		User user = ur.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		
		user.setPasswordHash(passwordEncoder.encode(newRawPassword));
		
		ur.save(user);
		
	}

}
