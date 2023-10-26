package com.example.eshopbackend.eshopbackend.service;

import com.example.eshopbackend.eshopbackend.datamodel.TransactionRequest;
import com.example.eshopbackend.eshopbackend.datamodel.TransactionResponse;

import java.util.List;

public interface TransactionService {
    public void createTransaction(TransactionRequest transactionRequest);
    public List<TransactionResponse> getTransactionDetails(Long userId);
}
