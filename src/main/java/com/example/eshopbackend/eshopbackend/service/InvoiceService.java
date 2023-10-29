package com.example.eshopbackend.eshopbackend.service;

import com.example.eshopbackend.eshopbackend.datamodel.InvoiceRequest;
import com.example.eshopbackend.eshopbackend.datamodel.InvoiceResponse;
import com.example.eshopbackend.eshopbackend.datamodel.OrderRequest;

import java.util.List;

public interface InvoiceService {
    public InvoiceResponse createInvoice(InvoiceRequest invoiceRequest, Long buyerId);
    public List<InvoiceResponse> getAllBuyerInvoice(Long userId);
    public List<InvoiceResponse> getAllSellerInvoice(Long userId);
    public String createBulkInvoice(OrderRequest orderRequest, Long buyerId);
}
