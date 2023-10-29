package com.example.eshopbackend.eshopbackend.service.impl;

import com.example.eshopbackend.eshopbackend.common.utils.Utils;
import com.example.eshopbackend.eshopbackend.datamodel.TransactionRequest;
import com.example.eshopbackend.eshopbackend.datamodel.WalletBankResponse;
import com.example.eshopbackend.eshopbackend.entity.TransactionEntity;
import com.example.eshopbackend.eshopbackend.entity.UserEntity;
import com.example.eshopbackend.eshopbackend.entity.WallerBankEntity;
import com.example.eshopbackend.eshopbackend.modelconverter.TransactionModelConverter;
import com.example.eshopbackend.eshopbackend.modelconverter.WalletBankModelConverter;
import com.example.eshopbackend.eshopbackend.repository.InvoiceRepository;
import com.example.eshopbackend.eshopbackend.repository.TransactionRepository;
import com.example.eshopbackend.eshopbackend.repository.UserRepository;
import com.example.eshopbackend.eshopbackend.repository.WalletBankRepository;
import com.example.eshopbackend.eshopbackend.service.WalletBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletBankService {

    @Autowired
    WalletBankRepository walletBankRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    Utils utils;

    @Value("${admin.topupWalletId}")
    private Long topUpAccountId;

    @Override
    public void createWallet(Long userId) {
        System.out.println("Waleet Create for : " + userId);
        Optional<UserEntity> optionaluserEntity = userRepository.findById(userId);

        if(optionaluserEntity.isPresent()){

            System.out.println("User Present");
            WallerBankEntity walletBankEntity = new WallerBankEntity();

            String walletId= utils.generateWalletId();
            Double defaultWalletbalance = 1000.00;

            walletBankEntity.setName(optionaluserEntity.get().getFirstName() + " " + optionaluserEntity.get().getLastName());
            walletBankEntity.setWalletId(walletId);
            walletBankEntity.setBalance(defaultWalletbalance);
            walletBankEntity.setUserEntity(optionaluserEntity.get());
            walletBankEntity.setIsArchive(false);
            walletBankEntity.setCreatedDate(new Date());
            walletBankEntity.setLastModifiedDate(new Date());

            walletBankRepository.save(walletBankEntity);
            System.out.println("Wallet bank Saved");
        }
    }

    @Override
    public List<WalletBankResponse> getWalletBankDetails(Long userId) {
        List<WallerBankEntity> walletBankEntityList = new ArrayList<>();
        List<WalletBankResponse> walletBankResponseList = new ArrayList<>();

        Optional<UserEntity> optionaluserEntity = userRepository.findById(userId);
        if(optionaluserEntity.isPresent()){
            walletBankEntityList = walletBankRepository.findByUserEntity(optionaluserEntity.get());
            if(!walletBankEntityList.isEmpty()){
                for(WallerBankEntity walletBankEntity : walletBankEntityList){
                    WalletBankResponse tempWalletBankResponse = new WalletBankResponse();
                    tempWalletBankResponse = WalletBankModelConverter.entityToRequest(walletBankEntity);
                    walletBankResponseList.add(tempWalletBankResponse);
                }
            }
        }
        return walletBankResponseList;
    }

    @Override
    public void rechargeWallet(Long userId, TransactionRequest transactionRequest) {

        System.out.println(".......Admin topup bank id: " + topUpAccountId );
        TransactionEntity transactionEntity = new TransactionEntity();

        Optional<UserEntity> optionalAdminuserEntity = userRepository.findById(userId);
        Optional<UserEntity> optionalToUserEntity = userRepository.findById(transactionRequest.getToUserId());

        Optional<WallerBankEntity> optionalAdminTopupWalletEntity = walletBankRepository.findById(topUpAccountId);
        List<WallerBankEntity> userWalletBankList = new ArrayList<>();

        if(optionalAdminuserEntity.isPresent() && optionalAdminuserEntity.get().getIsAdmin() == true && optionalToUserEntity.isPresent()){

            userWalletBankList = walletBankRepository.findByUserEntity(optionalToUserEntity.get());

            if(optionalAdminTopupWalletEntity.isPresent() && !userWalletBankList.isEmpty()){
                // get first index user bank
                WallerBankEntity adminTopUpBank = optionalAdminTopupWalletEntity.get();
                WallerBankEntity firstUserBank = userWalletBankList.get(0);
                Double AdminBankBalance = adminTopUpBank.getBalance();
                Double rechargeAmount = transactionRequest.getAmount();
                Double UserBalance =firstUserBank.getBalance();

                if(AdminBankBalance> rechargeAmount){
                    String transactionRefNo = utils.generateTransactionId();
                    transactionRequest.setReferenceNo(transactionRefNo);

                    System.out.println("Initiating Funding Debit");
                    transactionRequest.setTransactionType("TOP_UP_DEBIT");
                    transactionRequest.setTransactionStatus("COMPLETED");
                    transactionEntity = TransactionModelConverter.requestToEntity(transactionRequest, optionalAdminuserEntity.get(), optionalAdminTopupWalletEntity.get(), firstUserBank, null);
                    transactionRepository.save(transactionEntity);
                    adminTopUpBank.setBalance(AdminBankBalance-rechargeAmount);
                    walletBankRepository.save(adminTopUpBank);
                    System.out.println("Funding Debit Completed");

                    System.out.println("Initiating Funding Credit");
                    transactionRequest.setTransactionType("TOP_UP_CREDIT");
                    transactionRequest.setTransactionStatus("COMPLETED");
                    transactionEntity = TransactionModelConverter.requestToEntity(transactionRequest, optionalToUserEntity.get(), optionalAdminTopupWalletEntity.get(), firstUserBank, null);
                    transactionRepository.save(transactionEntity);
                    firstUserBank.setBalance(UserBalance+rechargeAmount);
                    walletBankRepository.save(firstUserBank);
                    System.out.println("Funding Credit Completed");

                } else {
                    System.out.println("Admin -> Insufficient Balance");
                }
            }
        }
    }
}
