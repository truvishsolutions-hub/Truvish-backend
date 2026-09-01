package com.truvish.truvishbackend.corporateLogin.security;

import com.truvish.truvishbackend.corporateLogin.ClientLogin;
import com.truvish.truvishbackend.corporateLogin.ClientLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final ClientLoginRepository clientLoginRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        ClientLogin client =
                clientLoginRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "User not found with email : " + email
                                )
                        );

        if (!Boolean.TRUE.equals(client.getActive())) {

            throw new DisabledException(
                    "Account is inactive"
            );
        }

        List<GrantedAuthority> authorities =
                List.of(
                        new SimpleGrantedAuthority("ROLE_CLIENT")
                );

        return User.builder()
                .username(client.getEmail())
                .password(client.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!client.getActive())
                .build();
    }
}
