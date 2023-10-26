package com.example.eshopbackend.eshopbackend.controller;

import com.example.eshopbackend.eshopbackend.datamodel.SessionDataModel;
import com.example.eshopbackend.eshopbackend.service.impl.TransactionServiceImpl;
import com.example.eshopbackend.eshopbackend.session.SessionDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.eshopbackend.eshopbackend.session.SessionStore.SESSION_TRACKER;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    private SessionDataManager sessionDataManager;

    @Autowired
    private TransactionServiceImpl transactionService;

    // http://localhost:8090/api/transaction/details
    @GetMapping("/details")
    public ResponseEntity<?> getTransactionDetails(@RequestHeader String Authorization){
        SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
        if (sessionData == null) {
            return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(transactionService.getTransactionDetails(sessionData.getUserId()), HttpStatus.OK);
    }
}
