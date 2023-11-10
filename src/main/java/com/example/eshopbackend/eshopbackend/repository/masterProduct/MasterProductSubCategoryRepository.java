package com.example.eshopbackend.eshopbackend.repository.masterProduct;

import com.example.eshopbackend.eshopbackend.entity.masterProduct.MasterProductSubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterProductSubCategoryRepository extends JpaRepository<MasterProductSubCategoryEntity, Long> {
}
