package com.tecnova.technical_test.infrastructure.adapter.config.jwt;

import com.tecnova.technical_test.application.service.AppUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AppUserService appUserService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private UserDetails userDetails;
    private final String TEST_USERNAME = "testuser";
    private final String VALID_JWT = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTYxMjM0NTY3OCwiZXhwIjoxNjEyMzQ5Mjc4fQ.signature";
    private final String INVALID_JWT = "Bearer invalid.jwt.token";


    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        userDetails = new User(TEST_USERNAME, "password", new ArrayList<>()); // Usamos la clase User de Spring Security
    }


    @Test
    void doFilterInternal_whenValidTokenAndUserAuthenticated_shouldSetAuthenticationInContext() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(VALID_JWT);
        when(jwtUtil.extractUserName(anyString())).thenReturn(TEST_USERNAME);
        when(appUserService.loadUserByUsername(TEST_USERNAME)).thenReturn(userDetails);
        when(jwtUtil.validateToken(anyString(), eq(userDetails))).thenReturn(true); // Token es válido para el UserDetails

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication(), "La autenticación no debería ser nula");
        assertEquals(TEST_USERNAME, SecurityContextHolder.getContext().getAuthentication().getName(), "El nombre de usuario autenticado debe coincidir");

        verify(filterChain).doFilter(request, response);

        verify(request).getHeader("Authorization");
        verify(jwtUtil).extractUserName(anyString());
        verify(appUserService).loadUserByUsername(TEST_USERNAME);
        verify(jwtUtil).validateToken(anyString(), eq(userDetails));
    }

    @Test
    void doFilterInternal_whenNoAuthorizationHeader_shouldNotSetAuthentication() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null); // No hay encabezado de autorización

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication(), "La autenticación debería ser nula");

        verify(filterChain).doFilter(request, response);

        verify(request).getHeader("Authorization");
        verifyNoInteractions(jwtUtil, appUserService); // Asegurarse de que JwtUtil y AppUserService no fueron llamados
    }

    @Test
    void doFilterInternal_whenInvalidTokenFormat_shouldNotSetAuthentication() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("InvalidTokenWithoutBearerPrefix"); // Formato incorrecto

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication(), "La autenticación debería ser nula");
        verify(filterChain).doFilter(request, response);
        verify(request).getHeader("Authorization");
        verifyNoInteractions(jwtUtil, appUserService);
    }

    @Test
    void doFilterInternal_whenTokenDoesNotStartWithBearer_shouldNotSetAuthentication() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("CustomToken " + VALID_JWT.substring(7)); // Prefijo incorrecto

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication(), "La autenticación debería ser nula");
        verify(filterChain).doFilter(request, response);
        verify(request).getHeader("Authorization");
        verifyNoInteractions(jwtUtil, appUserService);
    }

    @Test
    void doFilterInternal_whenUserNameIsNullFromToken_shouldNotSetAuthentication() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(VALID_JWT);
        when(jwtUtil.extractUserName(anyString())).thenReturn(null); // UserName no puede ser extraído

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication(), "La autenticación debería ser nula");
        verify(filterChain).doFilter(request, response);
        verify(request).getHeader("Authorization");
        verify(jwtUtil).extractUserName(anyString());
        verifyNoInteractions(appUserService); // AppUserService no debería ser llamado
    }

    @Test
    void doFilterInternal_whenUserAlreadyAuthenticated_shouldNotOverrideAuthentication() throws ServletException, IOException {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("alreadyAuthenticatedUser", null));

        when(request.getHeader("Authorization")).thenReturn(VALID_JWT); // Puede haber un token, pero no debería cambiar nada
        when(jwtUtil.extractUserName(anyString())).thenReturn(TEST_USERNAME); // Estos mocks podrían llamarse, pero la autenticación no cambiará

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication(), "La autenticación no debería ser nula");
        assertEquals("alreadyAuthenticatedUser", SecurityContextHolder.getContext().getAuthentication().getName(), "La autenticación existente no debería ser sobrescrita");

        verify(filterChain).doFilter(request, response);

        verify(request).getHeader("Authorization");
        verify(jwtUtil).extractUserName(anyString()); // extractUserName sí se llama
        verifyNoInteractions(appUserService); // No debería intentar cargar el usuario si ya está autenticado
        verify(jwtUtil, never()).validateToken(anyString(), any(UserDetails.class)); // No debería validar el token
    }

    @Test
    void doFilterInternal_whenTokenIsInvalid_shouldNotSetAuthentication() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(VALID_JWT);
        when(jwtUtil.extractUserName(anyString())).thenReturn(TEST_USERNAME);
        when(appUserService.loadUserByUsername(TEST_USERNAME)).thenReturn(userDetails);
        when(jwtUtil.validateToken(anyString(), eq(userDetails))).thenReturn(false); // Token inválido

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication(), "La autenticación debería ser nula");
        verify(filterChain).doFilter(request, response);

        verify(request).getHeader("Authorization");
        verify(jwtUtil).extractUserName(anyString());
        verify(appUserService).loadUserByUsername(TEST_USERNAME);
        verify(jwtUtil).validateToken(anyString(), eq(userDetails));
    }
}
