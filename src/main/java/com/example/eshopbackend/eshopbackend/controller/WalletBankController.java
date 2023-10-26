package com.example.eshopbackend.eshopbackend.controller;

import com.example.eshopbackend.eshopbackend.datamodel.InvoiceRequest;
import com.example.eshopbackend.eshopbackend.datamodel.SessionDataModel;
import com.example.eshopbackend.eshopbackend.service.impl.WalletServiceImpl;
import com.example.eshopbackend.eshopbackend.session.SessionDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.eshopbackend.eshopbackend.session.SessionStore.SESSION_TRACKER;

@RestController
@RequestMapping("/api/wallet")
public class WalletBankController {
    @Autowired
    private SessionDataManager sessionDataManager;

    @Autowired
    private WalletServiceImpl walletService;

    // http://localhost:8090/api/wallet/create
    @PostMapping("/create")
    public ResponseEntity<?> createWallet(@RequestHeader String Authorization){
        // System.out.println("Add Product request: " + request);
        SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
        if (sessionData == null) {
            return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
        }
        walletService.createWallet(sessionData.getUserId());
        return new ResponseEntity<>("Wallet Created Successfully", HttpStatus.OK);
    }

    // http://localhost:8090/api/wallet/details
    @GetMapping("/details")
    public ResponseEntity<?> getWalletBankDetails(@RequestHeader String Authorization){
        SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
        if (sessionData == null) {
            return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(walletService.getWalletBankDetails(sessionData.getUserId()), HttpStatus.OK);
    }
}
