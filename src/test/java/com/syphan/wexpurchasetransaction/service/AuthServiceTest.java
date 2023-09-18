package com.syphan.wexpurchasetransaction.service;

import com.syphan.wexpurchasetransaction.model.entity.User;
import com.syphan.wexpurchasetransaction.repository.UserRepository;
import com.syphan.wexpurchasetransaction.service.auth.AuthService;
import com.syphan.wexpurchasetransaction.util.UserBuilder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void loadUserByEmailSuccess() {
        User user  = UserBuilder.generateUserEntity();
        when(this.userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> {
            UserDetails userDetails = this.authService.loadUserByUsername(user.getEmail());
            assertEquals(user.getEmail(), userDetails.getUsername());
            assertEquals(user.getPassword(), userDetails.getPassword());
        });
    }

    @Test
    public void loadUserByEmaiFacilUserNotFound() {
        when(this.userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            this.authService.loadUserByUsername("gabriel@syphan.com");
        });
    }

    @Test
    public void createUserSuccess() {
        User user  = UserBuilder.generateUserEntity();
        when(this.userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        when(this.userRepository.save(Mockito.any(User.class))).thenReturn(user);
        assertDoesNotThrow(() -> {
            this.authService.createUser(UserBuilder.generateUserDto());
        });
    }

    @Test
    public void createUserEmailAlreadyExists() {
        User user  = UserBuilder.generateUserEntity();
        when(this.userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
        when(this.passwordEncoder.encode(Mockito.anyString())).thenReturn("ag5a1fa6w452302a3");
        assertThrows(Exception.class, () -> {
            this.authService.createUser(UserBuilder.generateUserDto());
        });
    }
}
