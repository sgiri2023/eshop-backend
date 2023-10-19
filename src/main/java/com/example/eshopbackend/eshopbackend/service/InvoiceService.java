package com.example.eshopbackend.eshopbackend.service;

import com.example.eshopbackend.eshopbackend.datamodel.InvoiceRequest;
import com.example.eshopbackend.eshopbackend.datamodel.InvoiceResponse;

import java.util.List;

public interface InvoiceService {
    public InvoiceResponse createInvoice(InvoiceRequest invoiceRequest, Long buyerId);
    public List<InvoiceResponse> getAllBuyerInvoice(Long userId);
}
