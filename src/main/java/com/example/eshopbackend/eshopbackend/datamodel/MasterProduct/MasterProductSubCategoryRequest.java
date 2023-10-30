package com.example.eshopbackend.eshopbackend.datamodel.MasterProduct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterProductSubCategoryRequest {
    private Long id;
    // private String subCategoryCode;
    private String displayName;
    private Date createdDate;
    private Long masterProductCategoryId;
    private Boolean isArchive;
}
