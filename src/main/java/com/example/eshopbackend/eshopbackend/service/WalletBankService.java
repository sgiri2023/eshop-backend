package com.example.eshopbackend.eshopbackend.service;

import com.example.eshopbackend.eshopbackend.datamodel.WalletBankResponse;

import java.util.List;

public interface WalletBankService {

    public void createWallet(Long userId);
    public List<WalletBankResponse> getWalletBankDetails(Long userId);
}
