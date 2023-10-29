package com.example.eshopbackend.eshopbackend.datamodel;

import com.example.eshopbackend.eshopbackend.common.enumConstant.PaymentMethodCode;
import com.example.eshopbackend.eshopbackend.common.enumConstant.TransactionTypeCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
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
