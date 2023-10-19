package com.example.eshopbackend.eshopbackend.repository;

import com.example.eshopbackend.eshopbackend.entity.AddressEntity;
import com.example.eshopbackend.eshopbackend.entity.InvoiceEntity;
import com.example.eshopbackend.eshopbackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

    List<InvoiceEntity> findByBuyerEntity(UserEntity buyerentity);
}
