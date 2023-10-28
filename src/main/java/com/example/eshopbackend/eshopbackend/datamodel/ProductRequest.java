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
    private String name;
    private String description;
    private Integer stockCount;
    private Double actualPrice;
    private Double discountRate;
    private Double ratings;
    private String pictureUrl;
    private Date createdDate;
    private Date lastModifyDate;
    private Integer deliveryDays;
}
