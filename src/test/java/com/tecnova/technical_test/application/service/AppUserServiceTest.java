package com.tecnova.technical_test.application.service;

import com.tecnova.technical_test.infrastructure.adapter.entity.AppUserEntity;
import com.tecnova.technical_test.infrastructure.adapter.entity.RoleEntity;
import com.tecnova.technical_test.infrastructure.adapter.enums.RoleList;
import com.tecnova.technical_test.infrastructure.adapter.repository.IAppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class AppUserServiceTest {

    @InjectMocks
    private AppUserService appUserService;

    @Mock
    private IAppUserRepository appUserRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        AppUserEntity user = new AppUserEntity();
        user.setUserName("lmontoya");
        user.setPassword("encodedPass");
        user.setRole(new RoleEntity(1, RoleList.ROLE_USER));

        when(appUserRepository.findByUserName("lmontoya")).thenReturn(Optional.of(user));

        UserDetails result = appUserService.loadUserByUsername("lmontoya");

        assertNotNull(result);
        assertEquals("lmontoya", result.getUsername());
        assertEquals("encodedPass", result.getPassword());
        assertTrue(result.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsException() {
        when(appUserRepository.findByUserName("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            appUserService.loadUserByUsername("nonexistent");
        });
    }

    @Test
    void existsByUserName_ReturnsTrue() {
        when(appUserRepository.existsByUserName("lmontoya")).thenReturn(true);

        boolean exists = appUserService.existsByUserName("lmontoya");

        assertTrue(exists);
    }

    @Test
    void save_CallsRepositorySave() {
        AppUserEntity user = new AppUserEntity();
        user.setUserName("lmontoya");
        user.setPassword("encodedPass");
        user.setRole(new RoleEntity(1, RoleList.ROLE_USER));

        appUserService.save(user);

        verify(appUserRepository, times(1)).save(user);
    }
}
