package com.truvish.truvishbackend.corporateLogin.service;

import com.truvish.truvishbackend.corporateLogin.ClientLogin;
import com.truvish.truvishbackend.corporateLogin.ClientLoginRepository;
import com.truvish.truvishbackend.corporateLogin.dto.LoginRequest;
import com.truvish.truvishbackend.corporateLogin.dto.LoginResponse;
import com.truvish.truvishbackend.corporateLogin.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ClientLoginService {

    private final ClientLoginRepository clientLoginRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        ClientLogin login = clientLoginRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Invalid Email or Password"
                        ));

        if (!Boolean.TRUE.equals(login.getActive())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Account is inactive"
            );
        }

        boolean passwordMatched = passwordEncoder.matches(
                request.getPassword(),
                login.getPassword()
        );

        if (!passwordMatched) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid Email or Password"
            );
        }

        String token = jwtService.generateToken(login.getEmail());

        return LoginResponse.builder()
                .success(true)
                .message("Login Successful")
                .clientId(login.getClientId())
                .email(login.getEmail())
                .token(token)
                .build();
    }
}