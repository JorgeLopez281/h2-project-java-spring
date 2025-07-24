package com.tecnova.technical_test.infrastructure.adapter.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JwtPropertiesTest {

    @Test
    void testJwtPropertiesGettersAndSetters() {
        JwtProperties jwtProperties = new JwtProperties();
        String expectedSecretKey = "aVerySecretKeyThatIsLongEnoughForSigningJWTs";
        long expectedExpiration = 3600000L; // 1 hora en milisegundos

        jwtProperties.setSecretKey(expectedSecretKey);
        jwtProperties.setExpiration(expectedExpiration);

        assertNotNull(jwtProperties.getSecretKey(), "La secretKey no debería ser nula");
        assertEquals(expectedSecretKey, jwtProperties.getSecretKey(), "La secretKey debería coincidir con la establecida");

        assertEquals(expectedExpiration, jwtProperties.getExpiration(), "La expiración debería coincidir con la establecida");
    }

    @Test
    void testJwtPropertiesDefaultConstructor() {
        JwtProperties jwtProperties = new JwtProperties();

        assertNotNull(jwtProperties); // Solo verificamos que se puede instanciar
        assertEquals(0L, jwtProperties.getExpiration());
    }
}
