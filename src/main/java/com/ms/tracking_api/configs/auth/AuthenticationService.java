package com.ms.tracking_api.configs.auth;


import com.ms.tracking_api.configs.jwt.JwtService;
import com.ms.tracking_api.entities.User;
import com.ms.tracking_api.enuns.Role;
import com.ms.tracking_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDTO register(RegisterRequestDTO requestDTO) {
        var user = User.builder()
                .nome(requestDTO.getNome())
                .email(requestDTO.getEmail())
                .senha(passwordEncoder.encode(requestDTO.getSenha()))
                .role(Role.ADMIN)
                .build();
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
    return  AuthenticationResponseDTO.builder()
            .token(jwtToken)
            .build();
    }

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO requestDTO) {

        //validação

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                      requestDTO.getEmail(),
                        requestDTO.getSenha()
                )
        );
        var user = repository.findByEmail(requestDTO.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado"));
        var jwtToken = jwtService.generateToken(user);
        return  AuthenticationResponseDTO.builder()
                .token(jwtToken)
                .build();
    }
}
