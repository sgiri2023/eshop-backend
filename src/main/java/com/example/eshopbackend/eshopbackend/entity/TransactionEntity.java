package com.example.eshopbackend.eshopbackend.entity;

import com.example.eshopbackend.eshopbackend.common.enumConstant.PaymentMethodCode;
import com.example.eshopbackend.eshopbackend.common.enumConstant.TransactionProcessingStatusCode;
import com.example.eshopbackend.eshopbackend.common.enumConstant.TransactionTypeCode;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "transaction_record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEntity implements Serializable {

    private transient static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    private String referenceNo;

    private String description;

    private Double amount;

    @Temporal(TemporalType.DATE)
    private Date processingDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;

    @Enumerated(value = EnumType.STRING)
    PaymentMethodCode paymentMethod;

    @Enumerated(value = EnumType.STRING)
    TransactionTypeCode transactionType;

    @Column(length = 25)
    @Enumerated(value = EnumType.STRING)
    TransactionProcessingStatusCode transactionStatus;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "invoice_id")
    InvoiceEntity invoiceEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "from_bank_id")
    WallerBankEntity fromWalletBankEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "to_bank_id")
    WallerBankEntity toWalletBankEntity;
}
