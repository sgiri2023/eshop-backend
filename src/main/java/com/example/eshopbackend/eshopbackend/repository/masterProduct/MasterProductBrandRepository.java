package com.example.eshopbackend.eshopbackend.repository.masterProduct;

import com.example.eshopbackend.eshopbackend.entity.masterProduct.MasterProductBrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterProductBrandRepository extends JpaRepository<MasterProductBrandEntity, Long> {
}
