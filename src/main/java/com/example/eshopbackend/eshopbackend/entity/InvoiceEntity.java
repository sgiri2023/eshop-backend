package com.example.eshopbackend.eshopbackend.entity;

import com.example.eshopbackend.eshopbackend.common.enumConstant.InvoiceStateCode;
import com.example.eshopbackend.eshopbackend.entity.masterProduct.MasterProductBrandEntity;
import com.example.eshopbackend.eshopbackend.entity.masterProduct.MasterProductCategoryEntity;
import com.example.eshopbackend.eshopbackend.entity.masterProduct.MasterProductModelEntity;
import com.example.eshopbackend.eshopbackend.entity.masterProduct.MasterProductSubCategoryEntity;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "invoice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceEntity implements Serializable {

    private transient static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    private String orderId;
    private String invoieNo;

    Double unitPrice;
    Double discountRate;
    Integer quantity;
    Double taxRate;
    Double shippingCharge;

    @Column(length = 40)
    @Enumerated(value = EnumType.STRING)
    @NotNull
    InvoiceStateCode invoiceState;

    String paymentMethod;

    Boolean isInvoiceSettle;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    UserEntity buyerEntity;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    UserEntity sellerEntity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressEntity addressEntity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    @ManyToOne
    @JoinColumn(name = "master_product_model_id")
    MasterProductModelEntity masterProductModelEntity;

    @ManyToOne
    @JoinColumn(name = "master_product_brand_id")
    MasterProductBrandEntity masterProductBrandEntity;

    @ManyToOne
    @JoinColumn(name = "master_product_sub_catergory_id")
    MasterProductSubCategoryEntity masterProductSubCategoryEntity;

    @ManyToOne
    @JoinColumn(name = "master_product_catergory_id")
    MasterProductCategoryEntity masterProductCategoryEntity;

    @OneToMany(mappedBy = "invoiceEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<AuditTrailEntity> invoiceAuditTrailEntity;

    @OneToMany(mappedBy = "invoiceEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<TransactionEntity> toTransactionEntity;

    @Column(columnDefinition = "boolean default false")
    private Boolean isArchive;

    @CreatedDate
    @Column(name = "purchase_date")
    private Date purchaseDate;

    @CreatedDate
    @Column(name = "delivered_date")
    private Date deliveredDate;

    @CreatedDate
    @Column(name = "delivery_date")
    private Date deliveryDate;

    @CreatedDate
    @Column(name = "created_date")
    private Date createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;

}
