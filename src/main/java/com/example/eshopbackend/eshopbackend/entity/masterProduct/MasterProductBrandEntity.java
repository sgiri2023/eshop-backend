package com.example.eshopbackend.eshopbackend.entity.masterProduct;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "maste_product_brand")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MasterProductBrandEntity implements Serializable {
    private transient static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    // private String brandCode;

    private String brnadName;

    private String manufacturerDetails;

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

    @OneToMany(mappedBy = "masterProductBrandEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<MasterProductModelEntity> masterProductModelEntityList;
}
