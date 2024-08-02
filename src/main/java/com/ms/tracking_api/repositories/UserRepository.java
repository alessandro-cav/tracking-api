package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;


public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
