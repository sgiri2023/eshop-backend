package com.example.eshopbackend.eshopbackend.modelconverter;

import com.example.eshopbackend.eshopbackend.datamodel.AddressRequest;
import com.example.eshopbackend.eshopbackend.datamodel.ProductRequest;
import com.example.eshopbackend.eshopbackend.datamodel.ProductResponse;
import com.example.eshopbackend.eshopbackend.entity.AddressEntity;
import com.example.eshopbackend.eshopbackend.entity.ProductEntity;
import com.example.eshopbackend.eshopbackend.entity.UserEntity;

import java.util.Date;

public class ProductModelConverter {

    public static ProductEntity requestToEntity(ProductRequest request, UserEntity userEntity){

        ProductEntity productEntity = new ProductEntity();

        if (request.getId() != null) {
            productEntity.setId(request.getId());
        }
        if (userEntity != null) {
            productEntity.setSellerEntity(userEntity);
        }

        productEntity.setName(request.getName());
        productEntity.setDescription(request.getName());
        productEntity.setActualPrice(request.getActualPrice());
        productEntity.setDiscountRate(request.getDiscountRate());

        double actualPrice = request.getActualPrice();
        double discountRate = request.getDiscountRate();
        double discountedPrice = actualPrice - actualPrice*discountRate/100;
        // productEntity.setDiscountedPrice(discountedPrice);

        productEntity.setStockCount(request.getStockCount());
        productEntity.setRatings(request.getRatings());
        productEntity.setPictureUrl(request.getPictureUrl());
        productEntity.setCreatedDate(new Date());
        productEntity.setLastModifiedDate(new Date());

        return productEntity;
    }

    public static ProductResponse entityToResponse(ProductEntity entity){
        ProductResponse response = new ProductResponse();
        if(entity.getId() != null){
            response.setId(entity.getId());
        }
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setActualPrice(entity.getActualPrice());
        response.setDiscountRate(entity.getDiscountRate());

        double actualPrice = entity.getActualPrice();
        double discountRate = entity.getDiscountRate();
        double discountedPrice = actualPrice - actualPrice*discountRate/100;
        response.setDiscountedPrice(discountedPrice);

        response.setStockCount(entity.getStockCount());
        response.setRatings(entity.getRatings());
        response.setSellerId(entity.getSellerEntity().getId());
        response.setSellerName(entity.getSellerEntity().getFirstName() + " " + entity.getSellerEntity().getLastName());
        response.setPictureUrl(entity.getPictureUrl());
        response.setCreatedDate(new Date());

        return response;
    }
}
