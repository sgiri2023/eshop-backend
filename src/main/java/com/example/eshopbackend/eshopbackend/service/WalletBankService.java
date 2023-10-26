package com.example.eshopbackend.eshopbackend.service;

import com.example.eshopbackend.eshopbackend.datamodel.WalletBankResponse;

public interface WalletBankService {

    public void createWallet(Long userId);
    public WalletBankResponse getWalletBankDetails(Long userId);
}
