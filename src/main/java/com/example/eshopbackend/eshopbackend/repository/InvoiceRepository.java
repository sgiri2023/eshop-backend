package com.example.eshopbackend.eshopbackend.repository;

import com.example.eshopbackend.eshopbackend.entity.AddressEntity;
import com.example.eshopbackend.eshopbackend.entity.InvoiceEntity;
import com.example.eshopbackend.eshopbackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {
    List<InvoiceEntity> findByBuyerEntity(UserEntity buyerEntity);
    List<InvoiceEntity> findBySellerEntity(UserEntity sellerEntity);
    Optional<InvoiceEntity> findByIdAndSellerEntity(Long invoiceId, UserEntity sellerEntity);

//    @Query(value = "SELECT COUNT(i.id) FROM tbl_invoices AS i WHERE i.is_archive = false AND i.invoice_type = :type AND "
//            + " i.business_id = :businessId AND i.invoice_state IN :state", nativeQuery = true)
//    Long countByInvoiceTypeAndBusinessEntityAndInvoiceStateIs(@Param("type") String type,
//                                                              @Param("businessId") Long businessId, @Param("state") List<String> state);
//@Query(value = "from EntityClassTable t where yourDate BETWEEN :startDate AND :endDate")
//public List<EntityClassTable> getAllBetweenDates(@Param("startDate")Date startDate,@Param("endDate")Date endDate);

    @Query(value = "SELECT i.* from invoice AS i WHERE i.created_date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<InvoiceEntity> getAllInvoiceBetweenDates(@Param("startDate") Date startDate, @Param("endDate")Date endDate);

}
