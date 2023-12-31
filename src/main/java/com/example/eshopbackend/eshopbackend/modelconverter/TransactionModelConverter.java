package com.example.eshopbackend.eshopbackend.modelconverter;

import com.example.eshopbackend.eshopbackend.common.enumConstant.PaymentMethodCode;
import com.example.eshopbackend.eshopbackend.common.enumConstant.TransactionProcessingStatusCode;
import com.example.eshopbackend.eshopbackend.common.enumConstant.TransactionTypeCode;
import com.example.eshopbackend.eshopbackend.datamodel.TransactionRequest;
import com.example.eshopbackend.eshopbackend.datamodel.TransactionResponse;
import com.example.eshopbackend.eshopbackend.datamodel.WalletBankResponse;
import com.example.eshopbackend.eshopbackend.entity.InvoiceEntity;
import com.example.eshopbackend.eshopbackend.entity.TransactionEntity;
import com.example.eshopbackend.eshopbackend.entity.UserEntity;
import com.example.eshopbackend.eshopbackend.entity.WallerBankEntity;

import java.util.Date;

public class TransactionModelConverter {
    public static TransactionEntity requestToEntity(TransactionRequest request, UserEntity userEntity, WallerBankEntity fromBankEntity, WallerBankEntity toBankEntity, InvoiceEntity invoiceEntity) {

        TransactionEntity transactionEntity = new TransactionEntity();

        if (request.getId() != null) {
            transactionEntity.setId(request.getId());
        }
        if(invoiceEntity != null){
            transactionEntity.setInvoiceEntity(invoiceEntity);
        }
        if(userEntity !=null){
            transactionEntity.setUserEntity(userEntity);
        }
        if(fromBankEntity != null){
            transactionEntity.setFromWalletBankEntity(fromBankEntity);
        }
        if(toBankEntity != null){
            transactionEntity.setToWalletBankEntity(toBankEntity);
        }
        transactionEntity.setDescription(request.getDescription());
        transactionEntity.setAmount(request.getAmount());
        transactionEntity.setReferenceNo(request.getReferenceNo());
        transactionEntity.setPaymentMethod(PaymentMethodCode.valueOf(request.getPaymentMethod()));
        transactionEntity.setTransactionType(TransactionTypeCode.valueOf(request.getTransactionType()));
        transactionEntity.setTransactionStatus(TransactionProcessingStatusCode.valueOf(request.getTransactionStatus()));
        transactionEntity.setProcessingDate(new Date());
        transactionEntity.setTransactionDate(new Date());

        return transactionEntity;
    }

    public static TransactionResponse entityToResponse(TransactionEntity transactionEntity){
        TransactionResponse transactionResponse = new TransactionResponse();

        if(transactionEntity.getId() != null){
            transactionResponse.setId(transactionEntity.getId());
        }
        transactionResponse.setReferenceNo(transactionEntity.getReferenceNo());
        transactionResponse.setAmount(transactionEntity.getAmount());
        transactionResponse.setDescription(transactionEntity.getDescription());
        transactionResponse.setTransactionDate(transactionEntity.getTransactionDate());
        transactionResponse.setProcessingDate(transactionEntity.getProcessingDate());
        transactionResponse.setTransactionType(transactionEntity.getTransactionType().getValue());
        transactionResponse.setTransactionStatus(transactionEntity.getTransactionStatus().getValue());
        transactionResponse.setPaymentMethod(transactionEntity.getPaymentMethod().getValue());
        if(transactionEntity.getInvoiceEntity() != null){
            transactionResponse.setInvoiceId(transactionEntity.getInvoiceEntity().getId());
        }

        // transactionResponse.setUserId(transactionEntity.getUserEntity().getId());
        transactionResponse.setFromAccountName(transactionEntity.getFromWalletBankEntity().getName());
        transactionResponse.setToAccountName(transactionEntity.getToWalletBankEntity().getName());

        return transactionResponse;
    }
}
