package com.example.eshopbackend.eshopbackend.datamodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private Long id;
    private String referenceNo;
    private String description;
    private Double amount;
    private Date processingDate;
    private Date transactionDate;
    private String paymentMethod;
    private String transactionType;
    private String transactionStatus;
    private Long invoiceId;
    private Long toUserId;
}
