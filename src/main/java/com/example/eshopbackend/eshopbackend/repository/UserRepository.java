package com.example.eshopbackend.eshopbackend.repository;

import com.example.eshopbackend.eshopbackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findByIsAdmin(Boolean isAdmin);
    List<UserEntity> finByIsAdminIsCustomer(Boolean isAdmin, Boolean isCustomer);
}