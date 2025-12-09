package com.noosyn.onboarding.service;

import com.noosyn.onboarding.entity.User;
import com.noosyn.onboarding.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository repo;

    @InjectMocks
    private CustomUserDetailsService service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------------- SUCCESS CASE ----------------
    @Test
    void testLoadUserByUsername_Success() {
        User mockUser = User.builder()
                .username("aaryan")
                .password("encoded-pass")
                .role("USER")
                .build();

        when(repo.findByUsername("aaryan"))
                .thenReturn(Optional.of(mockUser));

        UserDetails details = service.loadUserByUsername("aaryan");

        assertNotNull(details);
        assertEquals("aaryan", details.getUsername());
        assertEquals("encoded-pass", details.getPassword());
        assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

        verify(repo).findByUsername("aaryan");
    }

    // ---------------- NOT FOUND CASE ----------------
    @Test
    void testLoadUserByUsername_NotFound() {
        when(repo.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> service.loadUserByUsername("unknown")
        );

        verify(repo).findByUsername("unknown");
    }
}
