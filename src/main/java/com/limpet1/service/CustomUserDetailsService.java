package com.limpet1.service;

import com.limpet1.model.XUser;
import com.limpet1.repository.UserRepositoryJPA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepositoryJPA userRepositoryJPA;

    public CustomUserDetailsService(UserRepositoryJPA userRepositoryJPA) {
        this.userRepositoryJPA = userRepositoryJPA;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        XUser xUser = userRepositoryJPA.findByEmail(email);

        if (xUser == null) {
            throw new UsernameNotFoundException("Unknown user: " + email);
        }

        UserDetails user;
        user = User.builder()
                .username(xUser.getEmail())
                .password(xUser.getPassword())
                .roles(xUser.getRole())
                .build();
        return user;
    }
}
