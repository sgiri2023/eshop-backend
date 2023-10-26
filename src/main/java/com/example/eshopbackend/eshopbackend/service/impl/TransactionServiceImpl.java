package com.example.eshopbackend.eshopbackend.service.impl;

import com.example.eshopbackend.eshopbackend.datamodel.TransactionRequest;
import com.example.eshopbackend.eshopbackend.datamodel.TransactionResponse;
import com.example.eshopbackend.eshopbackend.entity.TransactionEntity;
import com.example.eshopbackend.eshopbackend.entity.UserEntity;
import com.example.eshopbackend.eshopbackend.modelconverter.TransactionModelConverter;
import com.example.eshopbackend.eshopbackend.repository.TransactionRepository;
import com.example.eshopbackend.eshopbackend.repository.UserRepository;
import com.example.eshopbackend.eshopbackend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public void createTransaction(TransactionRequest transactionRequest) {

    }

    @Override
    public List<TransactionResponse> getTransactionDetails(Long userId) {
        System.out.println("Transaction Details For :" + userId);
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        List<TransactionEntity> transactionEntityList = new ArrayList<>();
        List<TransactionResponse> transactionResponseList = new ArrayList<>();

        if(optionalUserEntity.isPresent()) {
            transactionEntityList = transactionRepository.findByUserEntity(optionalUserEntity.get());

            if(!transactionEntityList.isEmpty()){
                for(TransactionEntity transaction: transactionEntityList){
                    TransactionResponse tempTransactionResponse = TransactionModelConverter.entityToResponse(transaction);
                    transactionResponseList.add(tempTransactionResponse);
                }
            }
        }

        return transactionResponseList;
    }
}
