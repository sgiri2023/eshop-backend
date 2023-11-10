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
public class MasterProductCategoryRequest {
    private Long id;
    // private String categoryCode;
    private String displayName;
    private Date createdDate;
    private Boolean isArchive;
}
