package com.example.eshopbackend.eshopbackend.repository;

import com.example.eshopbackend.eshopbackend.entity.AddressEntity;
import com.example.eshopbackend.eshopbackend.entity.UserEntity;
import com.example.eshopbackend.eshopbackend.entity.WallerBankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletBankRepository extends JpaRepository<WallerBankEntity, Long> {
    List<WallerBankEntity> findByUserEntity(UserEntity userEntity);
}
