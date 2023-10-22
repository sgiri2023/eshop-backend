package com.example.eshopbackend.eshopbackend.service.impl;

import com.example.eshopbackend.eshopbackend.common.utills.InvoiceStateCode;
import com.example.eshopbackend.eshopbackend.datamodel.InvoiceRequest;
import com.example.eshopbackend.eshopbackend.datamodel.InvoiceResponse;
import com.example.eshopbackend.eshopbackend.entity.*;
import com.example.eshopbackend.eshopbackend.modelconverter.AddressModelConverter;
import com.example.eshopbackend.eshopbackend.modelconverter.AuditTrailModelConverter;
import com.example.eshopbackend.eshopbackend.modelconverter.InvoiceModelConverter;
import com.example.eshopbackend.eshopbackend.repository.*;
import com.example.eshopbackend.eshopbackend.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;
    @Autowired
    AuditTrailRepository auditTrailRepository;

    public InvoiceResponse createInvoice(InvoiceRequest invoiceRequest, Long buyerId){

        Optional<UserEntity> optionalBuyerEntity = userRepository.findById(buyerId);
        Optional<UserEntity> optionalSellerEntity = userRepository.findById(invoiceRequest.getSellerId());
        Optional<AddressEntity> optionalAddressEntity = addressRepository.findById(invoiceRequest.getAddressId());
        Optional<ProductEntity> optionalProductEntity = productRepository.findById(invoiceRequest.getProductId());

        InvoiceEntity invoiceEntity = new InvoiceEntity();
        InvoiceEntity savedInvoiceEntity = new InvoiceEntity();
        if(optionalSellerEntity.isPresent()){
            System.out.println("Seller Found");
        }
        if(optionalBuyerEntity.isPresent()){
            System.out.println("Byuer Found");
        }
        if(optionalAddressEntity.isPresent()){
            System.out.println("Address Found");
        }
        if(optionalProductEntity.isPresent()){
            System.out.println("Product Found");
        }
        if(optionalSellerEntity.isPresent() && optionalBuyerEntity.isPresent() && optionalAddressEntity.isPresent() && optionalProductEntity.isPresent()){
            System.out.println("All Found");
            invoiceEntity = InvoiceModelConverter.requestToEntity(invoiceRequest, optionalSellerEntity.get(), optionalBuyerEntity.get(),optionalProductEntity.get(), optionalAddressEntity.get());
            savedInvoiceEntity = invoiceRepository.save(invoiceEntity);
            System.out.println("Invoice Created");
            // add Audit Trail
            AuditTrailEntity auditTrailEntity = new AuditTrailEntity();
            auditTrailEntity = AuditTrailModelConverter.requestToEntity(savedInvoiceEntity, savedInvoiceEntity.getInvoiceState());
            auditTrailRepository.save(auditTrailEntity);
            System.out.println("Audit Trail Added");
        } else {
            System.out.println("Not Found");
        }

        return InvoiceModelConverter.entityToResponse(savedInvoiceEntity);
    }

    @Override
    public List<InvoiceResponse> getAllBuyerInvoice(Long userId) {
        Optional<UserEntity> optionalBuyerEntity = userRepository.findById(userId);
        List<InvoiceEntity> invoiceEntityList = invoiceRepository.findByBuyerEntity(optionalBuyerEntity.get());

        List<InvoiceResponse> invoiceListResponse = new ArrayList<>();
        if(!invoiceEntityList.isEmpty()){
            for(InvoiceEntity invoice : invoiceEntityList){
                InvoiceResponse invoiceResponse = InvoiceModelConverter.entityToResponse(invoice);
                invoiceListResponse.add(invoiceResponse);
            }
        }
        return invoiceListResponse;
    }

    @Override
    public List<InvoiceResponse> getAllSellerInvoice(Long userId) {
        Optional<UserEntity> optionalBuyerEntity = userRepository.findById(userId);
        List<InvoiceEntity> invoiceEntityList = invoiceRepository.findBySellerEntity(optionalBuyerEntity.get());

        List<InvoiceResponse> invoiceListResponse = new ArrayList<>();
        if(!invoiceEntityList.isEmpty()){
            for(InvoiceEntity invoice : invoiceEntityList){
                InvoiceResponse invoiceResponse = InvoiceModelConverter.entityToResponse(invoice);
                invoiceListResponse.add(invoiceResponse);
            }
        }
        return invoiceListResponse;
    }
}
