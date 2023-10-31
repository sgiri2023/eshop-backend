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
public class MasterProductBrandRequest {
    private Long id;
//    private String brandCode;
    private String brnadName;
    private String manufacturerDetails;
    private Date createdDate;
    private Date lastModifiedDate;
    private Boolean isArchive;
    private String categoryName;
    private String subCategoryName;
    private Long masterProductCategoryId;
    private Long masterProductSubCategoryId;
}
