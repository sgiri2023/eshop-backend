package com.example.eshopbackend.eshopbackend.repository.masterProduct;

import com.example.eshopbackend.eshopbackend.entity.masterProduct.MasterProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterProductCategoryRepository extends JpaRepository<MasterProductCategoryEntity, Long> {
}
