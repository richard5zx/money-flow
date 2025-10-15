package com.example.demo.users.service;

import com.example.demo.users.model.User;
import com.example.demo.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock private UserRepository repo;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private UserServiceImpl service; // the class under test

    private UUID userId;
    private User existing;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();
        existing = User.builder()
                .userId(userId)
                .email("old@example.com")
                .passwordHash("$2a$10$hash")
                .fullName("Old Name")
                .build();
    }

    // ---------- create ----------
    /*
    @Test
    void create_success() {
        when(repo.existsByEmail("a@ex.com")).thenReturn(false);
        when(passwordEncoder.encode("pw")).thenReturn("ENCODED");
        // return saved entity (id could be set by DB, but we simulate)
        when(repo.save(any(User.class))).thenAnswer(inv -> {
            User user = inv.getArgument(0);
            user.setUserId(UUID.randomUUID());
            return user;
        });

        User created = service.create("a@ex.com", "pw", "Alice");

        assertNotNull(created.getUserId());
        assertEquals("a@ex.com", created.getEmail());
        assertEquals("Alice", created.getFullName());
        assertEquals("ENCODED", created.getPasswordHash());
        verify(repo).existsByEmail("a@ex.com");
        verify(passwordEncoder).encode("pw");
        verify(repo).save(any(User.class));
    }

    @Test
    void create_throwsWhenEmailTaken() {
        when(repo.existsByEmail("taken@ex.com")).thenReturn(true);
        assertThrows(EmailAlreadyUsedException.class,
                () -> service.create("taken@ex.com", "pw", "Name"));
        verify(repo, never()).save(any());
    }

    // ---------- getById ----------

    @Test
    void getById_success() {
        when(repo.findById(userId)).thenReturn(Optional.of(existing));
        User user = service.getById(userId);
        assertEquals(userId, user.getUserId());
        verify(repo).findById(userId);
    }

    @Test
    void getById_notFound() {
        when(repo.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> service.getById(userId));
    }

    // ---------- list (pagination) ----------

    @Test
    void list_returnsPage() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("email").ascending());
        List<User> content = List.of(existing);
        Page<User> page = new PageImpl<>(content, pageable, 1);
        when(repo.findAll(pageable)).thenReturn(page);

        Page<User> result = service.list(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(existing.getEmail(), result.getContent().get(0).getEmail());
        verify(repo).findAll(pageable);
    }

    // ---------- update ----------

    @Test
    void update_changesEmailAndName_whenEmailFree() {
        when(repo.findById(userId)).thenReturn(Optional.of(existing));
        when(repo.existsByEmail("new@ex.com")).thenReturn(false);
        when(repo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User updated = service.updateUser(userId, "new@ex.com", "New Name");

        assertEquals("new@ex.com", updated.getEmail());
        assertEquals("New Name", updated.getFullName());
        verify(repo).existsByEmail("new@ex.com");
        verify(repo).save(any(User.class));
    }

    @Test
    void update_sameEmail_doesNotCheckExists() {
        when(repo.findById(userId)).thenReturn(Optional.of(existing));
        when(repo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User updated = service.updateUser(userId, "old@example.com", "New Name");
        verify(repo, never()).existsByEmail(anyString()); // no duplicate check for unchanged email
        assertEquals("New Name", updated.getFullName());
    }

    @Test
    void update_throwsWhenNewEmailTaken() {
        when(repo.findById(userId)).thenReturn(Optional.of(existing));
        when(repo.existsByEmail("taken@ex.com")).thenReturn(true);
        assertThrows(EmailAlreadyUsedException.class,
                () -> service.updateUser(userId, "taken@ex.com", "New Name"));
        verify(repo, never()).save(any(User.class));
    }

    // ---------- delete ----------

    @Test
    void delete_success() {
        when(repo.existsById(userId)).thenReturn(true);
        service.deleteUser(userId);
        verify(repo).deleteById(userId);
    }

    @Test
    void delete_notFound_throws() {
        when(repo.existsById(userId)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> service.deleteUser(userId));
        verify(repo, never()).deleteById(any());
    }

    // ---------- change password ----------

    @Test
    void changePassword_encodesAndSaves() {
        when(repo.findById(userId)).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("newPw")).thenReturn("NEW_HASH");
        when(repo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        service.changePassword(userId, "newPw");

        assertEquals("NEW_HASH", existing.getPasswordHash());
        verify(passwordEncoder).encode("newPw");
        verify(repo).save(existing);
    }
    */
}
