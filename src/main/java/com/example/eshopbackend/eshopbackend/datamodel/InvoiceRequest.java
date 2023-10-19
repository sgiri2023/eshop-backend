package com.example.eshopbackend.eshopbackend.datamodel;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequest {
    public Long id;
    String orderId;
    Integer quantity;
    Double unitPrice;
    Double shippingCharge;
    Double discountAmount;
    Double totalAmount;
    Double tax;
    Double finalAmount;
    Long productId;
    String paymentMethod;
    Long sellerId;
    Long addressId;
    Boolean isArchive;
    Date purchaseDate;
    Date deliveryDate;
}
