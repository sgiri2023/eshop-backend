package com.example.eshopbackend.eshopbackend.service.impl;

import com.example.eshopbackend.eshopbackend.datamodel.WalletBankResponse;
import com.example.eshopbackend.eshopbackend.entity.UserEntity;
import com.example.eshopbackend.eshopbackend.entity.WallerBankEntity;
import com.example.eshopbackend.eshopbackend.modelconverter.WalletBankModelConverter;
import com.example.eshopbackend.eshopbackend.repository.UserRepository;
import com.example.eshopbackend.eshopbackend.repository.WalletBankRepository;
import com.example.eshopbackend.eshopbackend.service.WalletBankService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public void createWallet(Long userId) {
        System.out.println("Waleet Create for : " + userId);
        Optional<UserEntity> optionaluserEntity = userRepository.findById(userId);

        if(optionaluserEntity.isPresent() && optionaluserEntity.get().getWalletBankEntity() != null){

            System.out.println("User Present");
            WallerBankEntity walletBankEntity = new WallerBankEntity();

            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
            String datetime = ft.format(dNow);
            String walletId = "ESHOPWALLET" + datetime;
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
    public WalletBankResponse getWalletBankDetails(Long userId) {
        List<WallerBankEntity> walletBankEntityList = new ArrayList<>();
        WalletBankResponse walletBankResponse = new WalletBankResponse();

        Optional<UserEntity> optionaluserEntity = userRepository.findById(userId);
        if(optionaluserEntity.isPresent()){
            walletBankEntityList = walletBankRepository.findByUserEntity(optionaluserEntity.get());
            if(!walletBankEntityList.isEmpty()){
                walletBankResponse = WalletBankModelConverter.entityToRequest(walletBankEntityList.get(0));
            }
        }
        return walletBankResponse;
    }
}
