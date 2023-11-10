package com.example.eshopbackend.eshopbackend.entity;

import com.example.eshopbackend.eshopbackend.entity.masterProduct.MasterProductBrandEntity;
import com.example.eshopbackend.eshopbackend.entity.masterProduct.MasterProductCategoryEntity;
import com.example.eshopbackend.eshopbackend.entity.masterProduct.MasterProductModelEntity;
import com.example.eshopbackend.eshopbackend.entity.masterProduct.MasterProductSubCategoryEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

    @OneToMany(mappedBy = "productEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<InvoiceEntity> invoiceEntityList;

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
