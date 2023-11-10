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
    String invoiceNo;

    Double unitPrice;
    Double discountRate;
    Integer quantity;
    // Double discountedAmount;
    Double taxRate;
    Double shippingCharge;
    // Double FinalAmount;

    Long productId;
    String paymentMethod;
    String invoiceState;
    Boolean isInvoiceSettle;
    Long sellerId;
    Long addressId;
    Boolean isArchive;
    Date purchaseDate;
    Date deliveryDate;
    Date createdDate;
    Date lastModifyDate;
}
