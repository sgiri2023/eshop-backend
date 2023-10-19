package com.example.eshopbackend.eshopbackend.controller;

import com.example.eshopbackend.eshopbackend.datamodel.*;
import com.example.eshopbackend.eshopbackend.service.impl.InvoiceServiceImpl;
import com.example.eshopbackend.eshopbackend.session.SessionDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.eshopbackend.eshopbackend.session.SessionStore.SESSION_TRACKER;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {

    @Autowired
    InvoiceServiceImpl invoiceService;
    @Autowired
    private SessionDataManager sessionDataManager;

    // http://localhost:8090/api/invoice/create
    @PostMapping("/create")
    public ResponseEntity<?> createInvoice(@RequestHeader String Authorization, @RequestBody InvoiceRequest request){
        System.out.println("Add Product request: " + request);
        SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
        if (sessionData == null) {
            return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(invoiceService.createInvoice(request, sessionData.getUserId()), HttpStatus.OK);
    }

    // Get Address
    // http://localhost:8090/api/invoice/get-invoice-list
    @GetMapping("/get-invoice-list")
    public ResponseEntity<?> getInvoiceList(@RequestHeader String Authorization){
        System.out.println("Get user address List: " + Authorization);
        SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
        if (sessionData == null) {
            return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
        }
        List<InvoiceResponse> invoiceResponsesList = invoiceService.getAllBuyerInvoice(sessionData.getUserId());
        return new ResponseEntity<>(invoiceResponsesList, HttpStatus.OK);
    }
}
