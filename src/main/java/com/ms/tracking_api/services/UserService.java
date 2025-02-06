package com.ms.tracking_api.services;

import com.ms.tracking_api.entities.User;
import com.ms.tracking_api.handlers.ObjetoNotFoundException;
import com.ms.tracking_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User findByEmail(String email) {
        return this.repository.findByEmail(email).orElseThrow(() -> new ObjetoNotFoundException("Usúario não encontrado!"));
    }

    @Transactional
    public void salvarNovoStatus(User user) {
        this.repository.save(user);
    }
}
