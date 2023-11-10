package com.example.eshopbackend.eshopbackend.repository.masterProduct;

import com.example.eshopbackend.eshopbackend.entity.masterProduct.MasterProductModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterProductModelRepository extends JpaRepository<MasterProductModelEntity, Long> {
}
