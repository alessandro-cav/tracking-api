package com.ms.tracking_api.configs.auth;

import com.ms.tracking_api.configs.jwt.JwtService;
import com.ms.tracking_api.configs.validations.Validator;
import com.ms.tracking_api.entities.User;
import com.ms.tracking_api.enuns.Role;
import com.ms.tracking_api.enuns.StatusUsuario;
import com.ms.tracking_api.handlers.BadRequestException;
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

    private final Validator validator;

    public AuthenticationResponseDTO register(RegisterRequestDTO requestDTO) {
        this.validator.validaEmail(requestDTO.getEmail());
        this.repository.findByEmail(requestDTO.getEmail()).ifPresent(email -> {
            throw new BadRequestException(requestDTO.getEmail() + " já cadastrado no sistema!");
        });
        var user = User.builder()
                .nome(requestDTO.getNome())
                .email(requestDTO.getEmail())
                .senha(passwordEncoder.encode(requestDTO.getSenha()))
                .role(Role.buscarRole(requestDTO.getRole()))
                .statusUsuario(StatusUsuario.ATIVO)
                .build();
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDTO.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO requestDTO) {
        this.validator.validaEmail(requestDTO.getEmail());
        var user = repository.findByEmail(requestDTO.getEmail())
                .map(usuario -> {
                    if (usuario.getStatusUsuario() == StatusUsuario.ATIVO) {
                        return usuario;
                    } else {
                        throw new BadRequestException("Usuário com status INATIVO. Por favor, entre em contato com a empresa para mais informações.");
                    }
                })
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com esse e-mail: " + requestDTO.getEmail()));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDTO.getEmail(),
                        requestDTO.getSenha()
                )
        );
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDTO.builder()
                .token(jwtToken)
                .build();
    }
}
