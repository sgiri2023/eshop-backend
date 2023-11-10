package com.example.eshopbackend.eshopbackend.datamodel.MasterProduct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterProductModelRequest {
    private Long id;
    private String modelName;
    private String productImageUrl;
    private String variant;
    private String description;
    private Double marketRatePrice;
    private Integer warranty;
    private Date createdDate;
    private Date lastModifiedDate;
    private Boolean isArchive;
    private String categoryName;
    private String subCategoryName;
    private String brandName;
    private Long masterProductCategoryId;
    private Long masterProductSubCategoryId;
    private Long masterProductBrandId;
}
