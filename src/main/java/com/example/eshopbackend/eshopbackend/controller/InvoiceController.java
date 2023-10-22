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
    // http://localhost:8090/api/invoice/buyer/get-invoice-list
    @GetMapping("/buyer/get-invoice-list")
    public ResponseEntity<?> getBuyerInvoiceList(@RequestHeader String Authorization){
        System.out.println("Get buyer invoice List: " + Authorization);
        SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
        if (sessionData == null) {
            return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
        }
        List<InvoiceResponse> invoiceResponsesList = invoiceService.getAllBuyerInvoice(sessionData.getUserId());
        return new ResponseEntity<>(invoiceResponsesList, HttpStatus.OK);
    }

    // Get Address
    // http://localhost:8090/api/invoice/seller/get-invoice-list
    @GetMapping("/seller/get-invoice-list")
    public ResponseEntity<?> getSellerInvoiceList(@RequestHeader String Authorization){
        System.out.println("Get seller invoice List: " + Authorization);
        SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
        if (sessionData == null) {
            return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
        }
        List<InvoiceResponse> invoiceResponsesList = invoiceService.getAllSellerInvoice(sessionData.getUserId());
        return new ResponseEntity<>(invoiceResponsesList, HttpStatus.OK);
    }

    // http://localhost:8090/api/invoice/update-state/{invoiceId}
    @PutMapping("/update-state/{invoiceId}")
    public ResponseEntity<?> updateInvoiceState(@RequestHeader String Authorization, @PathVariable(value = "invoiceId") Long invoiceId, @RequestBody InvoiceRequest request){
        System.out.println("Get user address List: " + Authorization);
        SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
        if (sessionData == null) {
            return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
        }
        InvoiceResponse updateInvoice = invoiceService.updateInvoiceState(sessionData.getUserId(), invoiceId, request);
        return new ResponseEntity<>(updateInvoice, HttpStatus.OK);
    }
}
