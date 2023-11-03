package com.example.eshopbackend.eshopbackend.service;

import com.example.eshopbackend.eshopbackend.datamodel.InvoiceRequest;
import com.example.eshopbackend.eshopbackend.datamodel.InvoiceResponse;
import com.example.eshopbackend.eshopbackend.datamodel.OrderRequest;

import java.util.List;

public interface InvoiceService {
    public InvoiceResponse createInvoice(InvoiceRequest invoiceRequest, Long buyerId);
    public List<InvoiceResponse> getAllBuyerInvoice(Long buyerId);
    public List<InvoiceResponse> getAllSellerInvoice(Long sellerId);
    public List<InvoiceResponse> getAllInvoices();
    public String createBulkInvoice(OrderRequest orderRequest, Long buyerId);
    public InvoiceResponse updateInvoiceState(Long userId, Long invoiceId, InvoiceRequest invoiceRequest);
    public String processInvoicePaymentToSeller(Long invoiceId, InvoiceRequest invoiceRequest);
}
