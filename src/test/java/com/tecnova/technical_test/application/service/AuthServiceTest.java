package com.tecnova.technical_test.application.service;

import com.tecnova.technical_test.domain.model.dto.NewUserDto;
import com.tecnova.technical_test.infrastructure.adapter.config.jwt.JwtUtil;
import com.tecnova.technical_test.infrastructure.adapter.entity.AppUserEntity;
import com.tecnova.technical_test.infrastructure.adapter.entity.RoleEntity;
import com.tecnova.technical_test.infrastructure.adapter.enums.RoleList;
import com.tecnova.technical_test.infrastructure.adapter.repository.IRoleRepository;
import jakarta.security.auth.message.AuthException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private AppUserService appUserService;

    @Mock
    private IRoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticate_ValidCredentials_ReturnsToken() throws Exception {
        String username = "lmontoya";
        String password = "pass123";
        String jwt = "mock-jwt-token";

        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtil.generateToken(authentication)).thenReturn(jwt);

        String result = authService.authenticate(username, password);

        assertEquals(jwt, result);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken(authentication);
    }

    @Test
    void authenticate_BadCredentials_ThrowsAuthException() {
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        AuthException exception = assertThrows(AuthException.class, () ->
                authService.authenticate("invalidUser", "wrongPass"));

        assertTrue(exception.getMessage().contains("Invalid credentials"));
    }

    @Test
    void registerUser_NewValidUser_SavesUser() {
        NewUserDto newUserDto = new NewUserDto("lmontoya", "Linda123*", "user");
        RoleEntity role = new RoleEntity(1, RoleList.ROLE_USER);

        when(appUserService.existsByUserName("lmontoya")).thenReturn(false);
        when(roleRepository.findByName(RoleList.ROLE_USER)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("Linda123*")).thenReturn("encodedPassword");

        authService.registerUser(newUserDto);

        ArgumentCaptor<AppUserEntity> userCaptor = ArgumentCaptor.forClass(AppUserEntity.class);
        verify(appUserService).save(userCaptor.capture());

        AppUserEntity savedUser = userCaptor.getValue();
        assertEquals("lmontoya", savedUser.getUserName());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(role, savedUser.getRole());
    }

    @Test
    void registerUser_UsernameExists_ThrowsException() {
        when(appUserService.existsByUserName("lmontoya")).thenReturn(true);

        NewUserDto newUserDto = new NewUserDto("lmontoya", "123", "ROLE_USER");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                authService.registerUser(newUserDto));

        assertEquals("The username already exists", ex.getMessage());
    }

    @Test
    void registerUser_InvalidRole_ThrowsException() {
        NewUserDto newUserDto = new NewUserDto("newuser", "123", "INVALID_ROLE");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                authService.registerUser(newUserDto));

        assertTrue(ex.getMessage().contains("Invalid role"));
    }

    @Test
    void registerUser_RoleNotFound_ThrowsException() {
        NewUserDto newUserDto = new NewUserDto("newuser", "123", "user");

        when(appUserService.existsByUserName("newuser")).thenReturn(false);
        when(roleRepository.findByName(RoleList.ROLE_USER)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                authService.registerUser(newUserDto));

        assertEquals("Role not found", ex.getMessage());
    }
}