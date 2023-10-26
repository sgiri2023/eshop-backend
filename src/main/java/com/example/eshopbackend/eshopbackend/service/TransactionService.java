package com.example.eshopbackend.eshopbackend.service;

import com.example.eshopbackend.eshopbackend.datamodel.TransactionRequest;

public interface TransactionService {
    public void createTransaction(TransactionRequest transactionRequest);
}
