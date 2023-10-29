package com.example.eshopbackend.eshopbackend.datamodel;

import com.example.eshopbackend.eshopbackend.entity.AddressEntity;
import com.example.eshopbackend.eshopbackend.entity.ProductEntity;
import com.example.eshopbackend.eshopbackend.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponse {
    public Long id;
    String orderId;

    Double unitPrice;
    Double discountRate;
    Integer quantity;
    Double discountedAmount;
    Double taxRate;
    Double shippingCharge;
    Double FinalAmount;

    String paymentMethod;
    String invoiceState;
//    UserResponse buyerId;
//    UserResponse sellerId;
    ProductResponse productResponse;
    AddressRequest addressRequest;
    List<AuditResponse> auditResponseList;
    Boolean isArchive;
    Date purchaseDate;
    Date deliveryDate;
}
