package com.example.eshopbackend.eshopbackend.modelconverter;

import com.example.eshopbackend.eshopbackend.common.utills.InvoiceStateCode;
import com.example.eshopbackend.eshopbackend.datamodel.*;
import com.example.eshopbackend.eshopbackend.entity.AddressEntity;
import com.example.eshopbackend.eshopbackend.entity.InvoiceEntity;
import com.example.eshopbackend.eshopbackend.entity.ProductEntity;
import com.example.eshopbackend.eshopbackend.entity.UserEntity;

import java.util.Date;

public class InvoiceModelConverter {

    public static InvoiceEntity requestToEntity(InvoiceRequest request, UserEntity sellerEntity, UserEntity buyerEntity, ProductEntity productEntity, AddressEntity addressEntity){
        InvoiceEntity invoiceEntity = new InvoiceEntity();

        if (request.getId() != null) {
            invoiceEntity.setId(request.getId());
        }
        if (sellerEntity != null) {
            invoiceEntity.setSellerEntity(sellerEntity);
        }
        if(buyerEntity != null){
            invoiceEntity.setBuyerEntity(buyerEntity);
        }
        if(productEntity != null){
            invoiceEntity.setProductEntity(productEntity);
        }
        if(addressEntity != null){
            invoiceEntity.setAddressEntity(addressEntity);
        }

        invoiceEntity.setOrderId(request.getOrderId());

        invoiceEntity.setPaymentMethod(request.getPaymentMethod());
        invoiceEntity.setInvoiceState(InvoiceStateCode.valueOf(request.getInvoiceState().toUpperCase().trim()));
        invoiceEntity.setQuantity(request.getQuantity());
        invoiceEntity.setUnitPrice(request.getUnitPrice());
        invoiceEntity.setTotalAmount(request.getTotalAmount());
        invoiceEntity.setDiscountAmount(request.getDiscountAmount());
        invoiceEntity.setTax(request.getTax());
        invoiceEntity.setFinalAmount(request.getFinalAmount());
        invoiceEntity.setShippingCharge(request.getShippingCharge());
        invoiceEntity.setIsArchive(false);
        invoiceEntity.setPurchaseDate(new Date());
        invoiceEntity.setCreatedDate(new Date());
        invoiceEntity.setLastModifiedDate(new Date());
        return invoiceEntity;
    }

    public static InvoiceResponse entityToResponse(InvoiceEntity entity){
        InvoiceResponse invoiceResponse = new InvoiceResponse();
        if (entity.getId() != null) {
            invoiceResponse.setId(entity.getId());
        }
        invoiceResponse.setOrderId(entity.getOrderId());
        invoiceResponse.setQuantity(entity.getQuantity());
        invoiceResponse.setUnitPrice(entity.getUnitPrice());
        invoiceResponse.setShippingCharge(entity.getShippingCharge());
        invoiceResponse.setDiscountAmount(entity.getDiscountAmount());
        invoiceResponse.setTotalAmount(entity.getTotalAmount());
        invoiceResponse.setTax(entity.getTax());
        invoiceResponse.setFinalAmount(entity.getFinalAmount());
        invoiceResponse.setPaymentMethod(entity.getPaymentMethod());
        invoiceResponse.setInvoiceState(entity.getInvoiceState().getValue());
        invoiceResponse.setIsArchive(entity.getIsArchive());
        invoiceResponse.setPurchaseDate(entity.getPurchaseDate());
        invoiceResponse.setDeliveryDate(entity.getDeliveryDate());

        AddressRequest addressRequest = new AddressRequest();
        addressRequest = AddressModelConverter.entityToRequest(entity.getAddressEntity());
        invoiceResponse.setAddressRequest(addressRequest);

        ProductResponse productResponse = new ProductResponse();
        productResponse = ProductModelConverter.entityToResponse(entity.getProductEntity());
        invoiceResponse.setProductResponse(productResponse);

        return invoiceResponse;
    }
}
