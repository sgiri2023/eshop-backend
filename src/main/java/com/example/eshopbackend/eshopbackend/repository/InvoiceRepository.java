package com.example.eshopbackend.eshopbackend.repository;

import com.example.eshopbackend.eshopbackend.entity.AddressEntity;
import com.example.eshopbackend.eshopbackend.entity.InvoiceEntity;
import com.example.eshopbackend.eshopbackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long>, JpaSpecificationExecutor<InvoiceEntity> {
    List<InvoiceEntity> findByBuyerEntity(UserEntity buyerEntity);
    List<InvoiceEntity> findBySellerEntity(UserEntity sellerEntity);
    Optional<InvoiceEntity> findByIdAndSellerEntity(Long invoiceId, UserEntity sellerEntity);

    @Query(value = "SELECT i.* from invoice AS i WHERE i.created_date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<InvoiceEntity> getAllInvoiceBetweenDates(@Param("startDate") Date startDate, @Param("endDate")Date endDate);

    @Query(value = "SELECT i.* from invoice AS i WHERE i.buyer_id = :buyerId AND i.created_date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<InvoiceEntity> getBuyerInvoiceBetweenDates(@Param("buyerId") Long buyerId, @Param("startDate") Date startDate, @Param("endDate")Date endDate);

    @Query(value = "SELECT i.* from invoice AS i WHERE i.seller_id = :sellerId AND i.created_date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<InvoiceEntity> getSellerInvoiceBetweenDates(@Param("sellerId") Long sellerId, @Param("startDate") Date startDate, @Param("endDate")Date endDate);

    @Query(value = "SELECT SUM(i.quantity) from invoice AS i WHERE i.product_id = :productId AND i.seller_id = :sellerId AND i.created_date BETWEEN :startDate AND :endDate", nativeQuery = true)
    Long countQuantityByProductIdAndSellerIdAndInvoicedateBetweenDates(@Param("productId") Long productId, @Param("sellerId") Long sellerId, @Param("startDate") Date startDate, @Param("endDate")Date endDate);
}
