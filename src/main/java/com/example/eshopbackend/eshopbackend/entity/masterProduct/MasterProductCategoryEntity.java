package com.example.eshopbackend.eshopbackend.entity.masterProduct;

import com.example.eshopbackend.eshopbackend.entity.ProductEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "maste_product_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MasterProductCategoryEntity implements Serializable {
    private transient static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    // private String categoryCode;

    private String displayName;

    @CreatedDate
    @Column(name = "created_date")
    private Date createdDate;

    @Column(columnDefinition = "boolean default false")
    private Boolean isArchive;

    @OneToMany(mappedBy = "masterProductCategoryEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<MasterProductSubCategoryEntity> masterProductSubCategoryEntityList;

    @OneToMany(mappedBy = "masterProductCategoryEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<MasterProductBrandEntity> masterProductBrandEntityList;

    @OneToMany(mappedBy = "masterProductCategoryEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<MasterProductModelEntity> masterProductModelEntityList;

    @OneToMany(mappedBy = "masterProductCategoryEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<ProductEntity> productEntityList;
}
