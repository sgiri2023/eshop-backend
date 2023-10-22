package com.example.eshopbackend.eshopbackend.datamodel;

import com.example.eshopbackend.eshopbackend.entity.AddressEntity;
import com.example.eshopbackend.eshopbackend.entity.ProductEntity;
import com.example.eshopbackend.eshopbackend.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponse {
    public Long id;
    String orderId;
    Integer quantity;
    Double unitPrice;
    Double shippingCharge;
    Double discountAmount;
    Double totalAmount;
    Double tax;
    Double finalAmount;
    String paymentMethod;
    String invoiceState;
//    UserResponse buyerId;
//    UserResponse sellerId;
    ProductResponse productResponse;
    AddressRequest addressRequest;
    Boolean isArchive;
    Date purchaseDate;
    Date deliveryDate;
}
