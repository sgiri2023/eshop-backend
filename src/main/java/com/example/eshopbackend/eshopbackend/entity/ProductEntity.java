package com.example.eshopbackend.eshopbackend.entity;

import com.example.eshopbackend.eshopbackend.entity.masterProduct.MasterProductModelEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity implements Serializable {

    private transient static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    // private String name;

    // private Double actualPrice;

    // private Double discountedPrice;

    // private String pictureUrl;

    private Double discountRate;
    private Double shippingCharge;
    private Integer deliveryDays;
    private String description;
    private Integer stockCount;
    private Double ratings;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    UserEntity sellerEntity;

    @OneToOne(mappedBy = "productEntity")
    private InvoiceEntity invoiceEntity;

    @ManyToOne
    @JoinColumn(name = "master_product_model_id")
    MasterProductModelEntity masterProductModelEntity;

    @CreatedDate
    @Column(name = "created_date")
    private Date createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;

    @PrePersist
    void onPrePersist() {
        if (ratings == null) {
            ratings = 0.0;
        }
        if (stockCount == null) {
            stockCount = 0;
        }
        if(discountRate == null){
            discountRate=0.0;
        }
        if(shippingCharge == null){
            shippingCharge = 0.0;
        }
    }
}
