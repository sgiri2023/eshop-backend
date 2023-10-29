package com.example.eshopbackend.eshopbackend.modelconverter;

import com.example.eshopbackend.eshopbackend.datamodel.AddressRequest;
import com.example.eshopbackend.eshopbackend.datamodel.WalletBankResponse;
import com.example.eshopbackend.eshopbackend.entity.AddressEntity;
import com.example.eshopbackend.eshopbackend.entity.UserEntity;
import com.example.eshopbackend.eshopbackend.entity.WallerBankEntity;

public class WalletBankModelConverter {

    public static WalletBankResponse entityToRequest(WallerBankEntity walletBankEntity) {

        WalletBankResponse walletBankResponse = new WalletBankResponse();

        if (walletBankEntity.getId() != null) {
            walletBankResponse.setId(walletBankEntity.getId());
        }

        walletBankResponse.setWalletId(walletBankEntity.getWalletId());
        walletBankResponse.setBalance(walletBankEntity.getBalance());
        walletBankResponse.setName(walletBankEntity.getName());
        walletBankResponse.setIsArchive(walletBankEntity.getIsArchive());
        walletBankResponse.setCreatedDate(walletBankEntity.getCreatedDate());
        walletBankResponse.setLastModifiedDate(walletBankEntity.getLastModifiedDate());

        return walletBankResponse;
    }
}
