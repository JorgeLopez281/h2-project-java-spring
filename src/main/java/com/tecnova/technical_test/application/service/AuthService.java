package com.tecnova.technical_test.application.service;

import com.tecnova.technical_test.domain.model.dto.NewUserDto;
import com.tecnova.technical_test.infrastructure.adapter.config.jwt.JwtUtil;
import com.tecnova.technical_test.infrastructure.adapter.entity.AppUserEntity;
import com.tecnova.technical_test.infrastructure.adapter.entity.RoleEntity;
import com.tecnova.technical_test.infrastructure.adapter.enums.RoleList;
import com.tecnova.technical_test.infrastructure.adapter.repository.IRoleRepository;
import jakarta.security.auth.message.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserService appUserService;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    public AuthService(AppUserService appUserService, IRoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.appUserService = appUserService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    public String authenticate(String username, String password) throws AuthException {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);

            Authentication authResult = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authResult);
            return jwtUtil.generateToken(authResult);

        } catch (BadCredentialsException ex) {
            throw new AuthException("Invalid credentials. Please verify your username and password.");
        } catch (AuthenticationException ex) {
            throw new AuthException("Authentication error: " + ex.getMessage());
        }
    }

    public void registerUser(NewUserDto newUserDto){
        if (appUserService.existsByUserName(newUserDto.getUserName())) {
            throw new IllegalArgumentException("The username already exists");
        }

        RoleList roleEnum = RoleList.fromValue(newUserDto.getRole())
                .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + newUserDto.getRole()));

        RoleEntity role = roleRepository.findByName(roleEnum)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        AppUserEntity user = new AppUserEntity(
                newUserDto.getUserName(),
                passwordEncoder.encode(newUserDto.getPassword()),
                role
        );

        appUserService.save(user);
    }
}
