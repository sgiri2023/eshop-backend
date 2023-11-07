package com.example.eshopbackend.eshopbackend.modelconverter;

import com.example.eshopbackend.eshopbackend.datamodel.AddressRequest;
import com.example.eshopbackend.eshopbackend.datamodel.ProductRequest;
import com.example.eshopbackend.eshopbackend.datamodel.ProductResponse;
import com.example.eshopbackend.eshopbackend.entity.AddressEntity;
import com.example.eshopbackend.eshopbackend.entity.ProductEntity;
import com.example.eshopbackend.eshopbackend.entity.UserEntity;
import com.example.eshopbackend.eshopbackend.entity.masterProduct.MasterProductModelEntity;

import java.util.Date;

public class ProductModelConverter {

    public static ProductEntity requestToEntity(ProductRequest request, UserEntity userEntity, MasterProductModelEntity masterProductModelEntity){

        ProductEntity productEntity = new ProductEntity();

        if (request.getId() != null) {
            productEntity.setId(request.getId());
        }
        if (userEntity != null) {
            productEntity.setSellerEntity(userEntity);
        }
        if (masterProductModelEntity != null) {
            productEntity.setMasterProductModelEntity(masterProductModelEntity);
        }

        productEntity.setDiscountRate(request.getDiscountRate());
        productEntity.setDeliveryDays(request.getDeliveryDays());
        productEntity.setShippingCharge(request.getShippingCharge());
        productEntity.setDescription(request.getDescription());
        productEntity.setStockCount(request.getStockCount());
        productEntity.setRatings(request.getRatings());
        productEntity.setCreatedDate(request.getCreatedDate());
        productEntity.setLastModifiedDate(request.getLastModifyDate());

        return productEntity;
    }

    public static ProductResponse entityToResponse(ProductEntity entity){
        ProductResponse response = new ProductResponse();
        if(entity.getId() != null){
            response.setId(entity.getId());
        }
        if(entity.getSellerEntity() != null){
            response.setSellerId(entity.getSellerEntity().getId());
            response.setSellerName(entity.getSellerEntity().getFirstName() + " " + entity.getSellerEntity().getLastName());
        }

        Double marketRatePrice = 0.0;

        if(entity.getMasterProductModelEntity() != null){
            MasterProductModelEntity masterProductModelEntity = entity.getMasterProductModelEntity();

            response.setMasterProductModelId(masterProductModelEntity.getId());
            response.setModelName(masterProductModelEntity.getModelName());
            response.setVariant(masterProductModelEntity.getVariant());
            response.setProductImageUrl(masterProductModelEntity.getProductImageUrl());
            response.setCategoryName(masterProductModelEntity.getMasterProductCategoryEntity().getDisplayName());
            response.setSubCategoryName(masterProductModelEntity.getMasterProductSubCategoryEntity().getDisplayName());
            response.setBrandName(masterProductModelEntity.getMasterProductBrandEntity().getBrnadName());
            response.setMasterProductCategoryId(masterProductModelEntity.getMasterProductCategoryEntity().getId());
            response.setMasterProductSubCategoryId(masterProductModelEntity.getMasterProductSubCategoryEntity().getId());
            response.setMasterProductBrandId(masterProductModelEntity.getMasterProductBrandEntity().getId());

            marketRatePrice = masterProductModelEntity.getMarketRatePrice();
        }

        response.setDescription(entity.getDescription());
        response.setRatings(entity.getRatings());
        Double discountRate = entity.getDiscountRate();
        Double discountedPrice = marketRatePrice - marketRatePrice*discountRate/100;
        response.setMarketRatePrice(marketRatePrice);
        response.setDiscountRate(discountRate);
        response.setPriceAfterDiscount(discountedPrice);
        response.setShippingCharges(entity.getShippingCharge());

        response.setDeliveryDays(entity.getDeliveryDays());
        response.setStockCount(entity.getStockCount());

        response.setSellerId(entity.getSellerEntity().getId());
        response.setCreatedDate(entity.getCreatedDate());
        response.setLastModifyDate(entity.getLastModifiedDate());

        return response;
    }
}
