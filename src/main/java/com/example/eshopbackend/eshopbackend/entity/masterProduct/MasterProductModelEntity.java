package com.example.eshopbackend.eshopbackend.entity.masterProduct;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "maste_product_model")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MasterProductModelEntity implements Serializable {
    private transient static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    private String modelName;

    private String productImageUrl;

    private String variant;

    private String description;

    private Double marketRatePrice;

    private Integer warranty;

    @CreatedDate
    @Column(name = "created_date")
    private Date createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;

    @Column(columnDefinition = "boolean default false")
    private Boolean isArchive;

    @ManyToOne
    @JoinColumn(name = "product_catergory_id")
    MasterProductCategoryEntity masterProductCategoryEntity;

    @ManyToOne
    @JoinColumn(name = "product_sub_catergory_id")
    MasterProductSubCategoryEntity masterProductSubCategoryEntity;

    @ManyToOne
    @JoinColumn(name = "product_brand_id")
    MasterProductBrandEntity masterProductBrandEntity;
}
