package com.example.eshopbackend.eshopbackend.entity;

import com.example.eshopbackend.eshopbackend.common.enumConstant.InvoiceStateCode;
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

    Double unitPrice;
    Double discountRate;
    Integer quantity;
    Double taxRate;
    Double shippingCharge;

    @Column(length = 20)
    @Enumerated(value = EnumType.STRING)
    @NotNull
    InvoiceStateCode invoiceState;

    String paymentMethod;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    UserEntity buyerEntity;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    UserEntity sellerEntity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressEntity addressEntity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

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
    @Column(name = "delivery_date")
    private Date deliveryDate;

    @CreatedDate
    @Column(name = "created_date")
    private Date createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;

}
