package com.example.eshopbackend.eshopbackend.datamodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    public Long id;

    private Double discountRate;
    private Double shippingCharge;
    private Integer deliveryDays;
    private String description;
    private Integer stockCount;
    private Double ratings;
    private Date createdDate;
    private Date lastModifyDate;

    private Long masterProductModelId;
    private Long masterProductBrandId;
    private Long masterProductSubCategoryId;
    private Long masterProductCategoryId;

    // private String name;
    // private Double actualPrice;
    // private String pictureUrl;
}
