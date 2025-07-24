package com.tecnova.technical_test.infrastructure.adapter.config.jwt;

import com.tecnova.technical_test.infrastructure.adapter.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.MalformedJwtException; // Importa esta excepción
import io.jsonwebtoken.security.SignatureException; // Importa esta excepción

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig; // Para usar @SpringJUnitConfig

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
class JwtUtilTest {

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private JwtUtil jwtUtil;

    private final String TEST_SECRET_KEY = "thisisatestsecretkeythatisatleast256bitslongforsigningjwt";
    private final long TEST_EXPIRATION_MS = 3600000L;

    @BeforeEach
    void setUp() {
        when(jwtProperties.getSecretKey()).thenReturn(TEST_SECRET_KEY);
        when(jwtProperties.getExpiration()).thenReturn(TEST_EXPIRATION_MS);
    }

    @Test
    void generateToken_shouldReturnValidJwtToken() {
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        String username = "testuser";
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);

        String token = jwtUtil.generateToken(authentication);

        assertNotNull(token, "El token no debería ser nulo");
        assertFalse(token.isEmpty(), "El token no debería estar vacío");

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(TEST_SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(username, claims.getSubject(), "El subject del token debería ser el nombre de usuario");
        assertNotNull(claims.getIssuedAt(), "El token debería tener una fecha de emisión");
        assertNotNull(claims.getExpiration(), "El token debería tener una fecha de expiración");
        assertTrue(claims.getExpiration().getTime() > claims.getIssuedAt().getTime(), "La fecha de expiración debe ser posterior a la de emisión");
    }

    @Test
    void validateToken_whenTokenIsValidAndUserMatches_shouldReturnTrue() {
        String username = "validuser";
        SecretKey key = Keys.hmacShaKeyFor(TEST_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        String validToken = Jwts.builder().setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TEST_EXPIRATION_MS)) // Token válido por 1 hora
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);

        Boolean isValid = jwtUtil.validateToken(validToken, userDetails);

        assertTrue(isValid, "El token válido debería ser validado correctamente");
    }

    @Test
    void validateToken_whenTokenIsExpired_shouldReturnFalse() {
        String username = "expireduser";
        SecretKey key = Keys.hmacShaKeyFor(TEST_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        String expiredToken = Jwts.builder().setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis() - TEST_EXPIRATION_MS - 1000L)) // Expirado hace 1 hora y 1 segundo
                .setExpiration(new Date(System.currentTimeMillis() - 1000L)) // Expirado hace 1 segundo
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username); // El nombre de usuario aún coincide

        Boolean isValid = jwtUtil.validateToken(expiredToken, userDetails);

        assertFalse(isValid, "El token expirado no debería ser validado");
    }

    @Test
    void validateToken_whenUsernameDoesNotMatch_shouldReturnFalse() {
        String tokenUsername = "tokenuser";
        String userDetailsUsername = "mismatcheduser";
        SecretKey key = Keys.hmacShaKeyFor(TEST_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder().setSubject(tokenUsername)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TEST_EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(userDetailsUsername); // Nombre de usuario no coincide

        Boolean isValid = jwtUtil.validateToken(token, userDetails);

        assertFalse(isValid, "El token no debería ser validado si el nombre de usuario no coincide");
    }

    @Test
    void extractUserName_shouldReturnCorrectUsername() {
        String username = "johndoe";
        SecretKey key = Keys.hmacShaKeyFor(TEST_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder().setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TEST_EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String extractedUsername = jwtUtil.extractUserName(token);

        assertEquals(username, extractedUsername, "El nombre de usuario extraído debería coincidir");
    }

    @Test
    void extractExpiration_shouldReturnCorrectExpirationDate() {
        String username = "testuser";
        long futureTime = System.currentTimeMillis() + TEST_EXPIRATION_MS;
        Date expectedExpiration = new Date(futureTime);
        SecretKey key = Keys.hmacShaKeyFor(TEST_SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        String token = Jwts.builder().setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expectedExpiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        Date extractedExpiration = jwtUtil.extractExpiration(token);

        assertNotNull(extractedExpiration, "La fecha de expiración no debería ser nula");

        long expectedSeconds = expectedExpiration.getTime() / 1000;
        long actualSeconds = extractedExpiration.getTime() / 1000;

        assertEquals(expectedSeconds, actualSeconds, "La fecha de expiración extraída debería coincidir");
    }

    @Test
    void isTokenExpired_whenTokenIsNotExpired_shouldReturnFalse() {
        String username = "activeuser";
        SecretKey key = Keys.hmacShaKeyFor(TEST_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder().setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TEST_EXPIRATION_MS)) // Token aún válido
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        Boolean isExpired = jwtUtil.isTokenExpired(token);

        assertFalse(isExpired, "El token no expirado debería reportarse como no expirado");
    }

    @Test
    void isTokenExpired_whenTokenIsExpired_shouldReturnTrue() {
        String username = "inactiveuser";
        SecretKey key = Keys.hmacShaKeyFor(TEST_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder().setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis() - TEST_EXPIRATION_MS - 1000L)) // Expirado hace tiempo
                .setExpiration(new Date(System.currentTimeMillis() - 1000L)) // Expirado hace 1 segundo
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        Boolean isExpired = jwtUtil.isTokenExpired(token);

        assertTrue(isExpired, "El token expirado debería reportarse como expirado");
    }

    @Test
    void extractAllClaims_whenTokenIsValid_shouldReturnClaims() {
        String username = "claimtest";
        SecretKey key = Keys.hmacShaKeyFor(TEST_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder().setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TEST_EXPIRATION_MS))
                .claim("role", "ADMIN") // Añadir una claim personalizada para probar
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        Claims claims = jwtUtil.extractAllClaims(token);

        assertNotNull(claims, "Los claims no deberían ser nulos");
        assertEquals(username, claims.getSubject());
        assertEquals("ADMIN", claims.get("role", String.class));
    }

    @Test
    void extractAllClaims_whenTokenHasInvalidSignature_shouldThrowSignatureException() {
        String username = "invalidSig";
        SecretKey correctKey = Keys.hmacShaKeyFor(TEST_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        SecretKey wrongKey = Keys.hmacShaKeyFor("aDifferentSecretKeyThatIsAlsoLongEnough".getBytes(StandardCharsets.UTF_8));

        String tokenWithCorrectSig = Jwts.builder().setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TEST_EXPIRATION_MS))
                .signWith(correctKey, SignatureAlgorithm.HS256)
                .compact();

        when(jwtProperties.getSecretKey()).thenReturn("aDifferentSecretKeyThatIsAlsoLongEnough");

        assertThrows(SignatureException.class, () -> jwtUtil.extractAllClaims(tokenWithCorrectSig),
                "Debería lanzar SignatureException para un token con firma inválida");
    }

    @Test
    void extractAllClaims_whenTokenIsMalformed_shouldThrowMalformedJwtException() {
        String malformedToken = "not.a.valid.jwt"; // Un token que no sigue la estructura JWT

        assertThrows(MalformedJwtException.class, () -> jwtUtil.extractAllClaims(malformedToken),
                "Debería lanzar MalformedJwtException para un token malformado");
    }
}
