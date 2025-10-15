package com.example.demo.users.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.users.model.User;

public interface UserService {
	User create(String email, String rawPassword, String fullName);
    User getById(UUID id);
    Page<User> list(Pageable pageable);
    User updateUser(UUID id, String email, String fullName);
    void deleteUser(UUID id);
    void changePassword(UUID id, String newRawPassword);
}
