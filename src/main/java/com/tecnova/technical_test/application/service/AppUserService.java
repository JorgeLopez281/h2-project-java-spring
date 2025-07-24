package com.tecnova.technical_test.application.service;

import com.tecnova.technical_test.infrastructure.adapter.entity.AppUserEntity;
import com.tecnova.technical_test.infrastructure.adapter.repository.IAppUserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@NoArgsConstructor
@Service
public class AppUserService implements UserDetailsService {

    @Autowired
    private IAppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUserEntity appUserEntity =
                appUserRepository.findByUserName(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(appUserEntity.getRole().getName().toString());

        return new org.springframework.security.core.userdetails.User(
                appUserEntity.getUserName(),
                appUserEntity.getPassword(),
                Collections.singleton(authority)
        );
    }

    public boolean existsByUserName(String username){
        return appUserRepository.existsByUserName(username);
    }

    public void save(AppUserEntity appUserEntity){
        appUserRepository.save(appUserEntity);
    }
}
