package com.example.eshopbackend.eshopbackend.modelconverter;

import com.example.eshopbackend.eshopbackend.common.enumConstant.InvoiceStateCode;
import com.example.eshopbackend.eshopbackend.datamodel.*;
import com.example.eshopbackend.eshopbackend.entity.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, productEntity.getDeliveryDays().intValue());
            invoiceEntity.setDeliveryDate(calendar.getTime());
        }
        if(addressEntity != null){
            invoiceEntity.setAddressEntity(addressEntity);
        }

        invoiceEntity.setOrderId(request.getOrderId());

        invoiceEntity.setPaymentMethod(request.getPaymentMethod());
        invoiceEntity.setInvoiceState(InvoiceStateCode.valueOf(request.getInvoiceState().toUpperCase().trim()));

        invoiceEntity.setQuantity(request.getQuantity());
        invoiceEntity.setUnitPrice(request.getUnitPrice());
        invoiceEntity.setDiscountRate(request.getDiscountRate());
        invoiceEntity.setTaxRate(request.getTaxRate());
        invoiceEntity.setShippingCharge(request.getShippingCharge());

        invoiceEntity.setIsArchive(request.getIsArchive());
        invoiceEntity.setPurchaseDate(request.getPurchaseDate());
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

        Double unitPrice = entity.getUnitPrice();
        Double discountRate = entity.getDiscountRate();
        Integer quantity = entity.getQuantity();
        Double discountedAmount = unitPrice*(1-discountRate/100)* quantity;
        Double taxRate = entity.getTaxRate();
        Double shippingCharge = entity.getShippingCharge();
        Double finalAmount = shippingCharge + discountedAmount*(100 + taxRate)/100;

        invoiceResponse.setUnitPrice(unitPrice);
        invoiceResponse.setDiscountRate(discountRate);
        invoiceResponse.setQuantity(quantity);
        invoiceResponse.setDiscountedAmount(discountedAmount);
        invoiceResponse.setTaxRate(taxRate);
        invoiceResponse.setShippingCharge(shippingCharge);
        invoiceResponse.setFinalAmount(finalAmount);

        invoiceResponse.setPaymentMethod(entity.getPaymentMethod());
        invoiceResponse.setInvoiceState(entity.getInvoiceState().getValue());
        invoiceResponse.setIsArchive(entity.getIsArchive());
        invoiceResponse.setPurchaseDate(entity.getPurchaseDate());
        invoiceResponse.setDeliveryDate(entity.getDeliveryDate());

        if(entity.getAddressEntity() != null){
            AddressRequest addressRequest = new AddressRequest();
            addressRequest = AddressModelConverter.entityToRequest(entity.getAddressEntity());
            invoiceResponse.setAddressRequest(addressRequest);
        }

        if(entity.getProductEntity() != null){
            ProductResponse productResponse = new ProductResponse();
            productResponse = ProductModelConverter.entityToResponse(entity.getProductEntity());
            invoiceResponse.setProductResponse(productResponse);
        }

        // audit Response List
        List<AuditResponse> auditResponseList = new ArrayList<>();
        if(entity.getInvoiceAuditTrailEntity() != null && !entity.getInvoiceAuditTrailEntity().isEmpty()){
            for(AuditTrailEntity invoiceAudit: entity.getInvoiceAuditTrailEntity()){
                AuditResponse tempAuditResponse = new AuditResponse();
                tempAuditResponse = AuditTrailModelConverter.entityToResponse(invoiceAudit);
                auditResponseList.add(tempAuditResponse);
            }
            invoiceResponse.setAuditResponseList(auditResponseList);
        }


        return invoiceResponse;
    }
}
