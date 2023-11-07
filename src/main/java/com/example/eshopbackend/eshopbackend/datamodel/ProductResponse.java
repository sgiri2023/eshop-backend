package com.example.eshopbackend.eshopbackend.datamodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    public Long id;
    private String modelName;
    private String variant;
    private String description;
    private Double ratings;

    private Double marketRatePrice;
    private Double discountRate;
    private Double priceAfterDiscount;
    private Double shippingCharges;

    private Integer deliveryDays;

    private Integer stockCount;
    private String productImageUrl;

    private Long sellerId;

    private String categoryName;
    private String subCategoryName;
    private String brandName;
    private Long masterProductCategoryId;
    private Long masterProductSubCategoryId;
    private Long masterProductBrandId;
    private Long masterProductModelId;

    private String sellerName;
    private Date createdDate;
    private Date lastModifyDate;
}
