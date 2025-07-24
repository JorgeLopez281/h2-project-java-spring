package com.tecnova.technical_test.infrastructure.adapter.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
class JwtEntryPointTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter printWriter;

    @InjectMocks
    private JwtEntryPoint jwtEntryPoint;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws IOException {
        when(response.getWriter()).thenReturn(printWriter);
        objectMapper = new ObjectMapper();
    }

    @Test
    void commence_shouldSetUnauthorizedResponse() throws IOException, ServletException {
        AuthenticationException authException = new BadCredentialsException("Invalid credentials");
        String requestUri = "/api/secure/resource";
        when(request.getRequestURI()).thenReturn(requestUri);

        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);

        jwtEntryPoint.commence(request, response, authException);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(printWriter).write(jsonCaptor.capture());

        String actualJson = jsonCaptor.getValue();

        Map<String, String> actualErrorResponse = objectMapper.readValue(actualJson, Map.class);

        assertEquals("Unauthorized", actualErrorResponse.get("error"), "El campo 'error' debe ser 'Unauthorized'");
        assertEquals("You must provide a valid token to access this resource", actualErrorResponse.get("message"), "El mensaje debe coincidir");
        assertEquals(requestUri, actualErrorResponse.get("path"), "El campo 'path' debe coincidir con la URI de la solicitud");
    }

    @Test
    void commence_shouldThrowIOExceptionWhenWriterFails() throws IOException, ServletException {
        AuthenticationException authException = new BadCredentialsException("Authentication failed");
        when(request.getRequestURI()).thenReturn("/some/path");

        doThrow(new IOException("Simulated write error")).when(printWriter).write(anyString());

        IOException thrown = assertThrows(IOException.class, () -> {
            jwtEntryPoint.commence(request, response, authException);
        }, "Debería lanzar una IOException cuando la escritura falla.");

        assertEquals("Simulated write error", thrown.getMessage());

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
