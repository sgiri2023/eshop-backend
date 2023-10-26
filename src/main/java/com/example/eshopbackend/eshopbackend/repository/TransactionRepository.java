package com.example.eshopbackend.eshopbackend.repository;

import com.example.eshopbackend.eshopbackend.entity.TransactionEntity;
import com.example.eshopbackend.eshopbackend.entity.UserEntity;
import com.example.eshopbackend.eshopbackend.entity.WallerBankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByUserEntity(UserEntity userEntity);
}
