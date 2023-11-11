package com.example.eshopbackend.eshopbackend.controller;

import com.example.eshopbackend.eshopbackend.datamodel.*;
import com.example.eshopbackend.eshopbackend.datamodel.Dataset.SellerProjectionDataset;
import com.example.eshopbackend.eshopbackend.datamodel.Dataset.TotalOrder;
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

    // http://localhost:8090/api/invoice/create-bulk
    @PostMapping("/create-bulk")
    public ResponseEntity<?> createBulkInvoice(@RequestHeader String Authorization, @RequestBody OrderRequest request){
        System.out.println("Add Product request: " + request);
        SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
        if (sessionData == null) {
            return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(invoiceService.createBulkInvoice(request, sessionData.getUserId()), HttpStatus.OK);
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

    // http://localhost:8090/api/invoice/admin/get-invoice-list
    @GetMapping("/admin/get-invoice-list")
    public ResponseEntity<?> getAdminInvoiceList(@RequestHeader String Authorization){
        System.out.println("Get Admin All invoice List: " + Authorization);
        SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
        if (sessionData == null) {
            return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
        }
        List<InvoiceResponse> invoiceResponsesList = invoiceService.getAllInvoices();
        return new ResponseEntity<>(invoiceResponsesList, HttpStatus.OK);
    }

    // http://localhost:8090/api/invoice/list?userType=ADMIN&key=
    @GetMapping("/list")
    public ResponseEntity<?> getInvoiceList(
            @RequestHeader String Authorization,
            @RequestParam(value = "userType") String userType,
            @RequestParam(value = "key", required = false) String searchKey){

        System.out.println("Filter Invoice: " + userType + " - " + searchKey);
        SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
        if (sessionData == null) {
            return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
        }
        List<InvoiceResponse> invoiceResponsesList = invoiceService.filterInvoice(sessionData.getUserId(), userType, searchKey);
        return new ResponseEntity<>(invoiceResponsesList, HttpStatus.OK);
    }

    // http://localhost:8090/api/invoice/betweenDates/get-invoice-list/{month}
    @GetMapping("/betweenDates/get-invoice-list/{month}")
    public ResponseEntity<?> getAllInvoiceBetweenDates(@RequestHeader String Authorization, @PathVariable(value = "month") Integer month){
        System.out.println("Get Admin All invoice List: " + Authorization);
        SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
        if (sessionData == null) {
            return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
        }
        Integer invoiceResponsesList = 0; //invoiceService.getAllInvoiceBetweenDates(month, 2023);
        return new ResponseEntity<>(invoiceResponsesList, HttpStatus.OK);
    }

    // http://localhost:8090/api/invoice/dataset/monthwise
    @GetMapping("/dataset/monthwise")
    public ResponseEntity<?> datasetgetMonthWiseTotalOrder(@RequestHeader String Authorization){
        System.out.println("Get Admin All invoice List: " + Authorization);
        SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
        if (sessionData == null) {
            return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
        }
        TotalOrder datasetInvoiceResponsesList = invoiceService.datasetGetMonthwiseTotalOrder(sessionData.getUserId());
        return new ResponseEntity<>(datasetInvoiceResponsesList, HttpStatus.OK);
    }

    // http://localhost:8090/api/invoice/dataset/seller/monthwise
    @GetMapping("/dataset/seller/monthwise")
    public ResponseEntity<?> datasetgetMonthWiseSellerProjection(@RequestHeader String Authorization){
        System.out.println("Get Admin All invoice List: " + Authorization);
        SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
        if (sessionData == null) {
            return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
        }
        SellerProjectionDataset dataset= invoiceService.getAllSellerOrderProjection(sessionData.getUserId());
        return new ResponseEntity<>(dataset, HttpStatus.OK);
    }

    // http://localhost:8090/api/invoice/dataset/buyer/monthwise
    @GetMapping("/dataset/buyer/monthwise")
    public ResponseEntity<?> datasetgetMonthWiseBuyerProjection(@RequestHeader String Authorization){
        System.out.println("Get Admin All invoice List: " + Authorization);
        SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
        if (sessionData == null) {
            return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
        }
        SellerProjectionDataset dataset= invoiceService.getAllBuyerOrderProjection(sessionData.getUserId());
        return new ResponseEntity<>(dataset, HttpStatus.OK);
    }

    // http://localhost:8090/api/invoice/update-state/{invoiceId}
    @PutMapping("/update-state/{invoiceId}")
    public ResponseEntity<?> updateInvoiceState(@RequestHeader String Authorization, @PathVariable(value = "invoiceId") Long invoiceId, @RequestBody InvoiceRequest request){
        System.out.println("Update Invoice State: " + request);
        SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
        if (sessionData == null) {
            return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
        }
        InvoiceResponse updateInvoice = invoiceService.updateInvoiceState(sessionData.getUserId(), invoiceId, request);
        return new ResponseEntity<>(updateInvoice, HttpStatus.OK);
    }

    // http://localhost:8090/api/invoice/make-payment/{invoiceId}
    @PutMapping("/make-payment/{invoiceId}")
    public ResponseEntity<?> settleInvoicePayment(@RequestHeader String Authorization, @PathVariable(value = "invoiceId") Long invoiceId, @RequestBody InvoiceRequest request){
        System.out.println("Make Invocie Payment: " + request);
        SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
        if (sessionData == null) {
            return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
        }
        String updateInvoice = invoiceService.processInvoicePaymentToSeller(invoiceId, request);
        return new ResponseEntity<>(updateInvoice, HttpStatus.OK);
    }

    // http://localhost:8090/api/invoice/refund/{invoiceId}
    @PutMapping("/refund/{invoiceId}")
    public ResponseEntity<?> refundInvoicePayment(@RequestHeader String Authorization, @PathVariable(value = "invoiceId") Long invoiceId, @RequestBody InvoiceRequest request){
        System.out.println("Refund Invocie Payment: " + request);
        SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
        if (sessionData == null) {
            return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
        }
        String updateInvoice = invoiceService.processRefundInvoicePaymentToUser(invoiceId, request);
        return new ResponseEntity<>(updateInvoice, HttpStatus.OK);
    }
}
