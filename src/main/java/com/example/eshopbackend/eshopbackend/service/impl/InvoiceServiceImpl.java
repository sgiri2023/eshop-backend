package com.example.eshopbackend.eshopbackend.service.impl;

import com.example.eshopbackend.eshopbackend.common.enumConstant.InvoiceStateCode;
import com.example.eshopbackend.eshopbackend.common.utils.Utils;
import com.example.eshopbackend.eshopbackend.datamodel.InvoiceRequest;
import com.example.eshopbackend.eshopbackend.datamodel.InvoiceResponse;
import com.example.eshopbackend.eshopbackend.datamodel.OrderRequest;
import com.example.eshopbackend.eshopbackend.datamodel.TransactionRequest;
import com.example.eshopbackend.eshopbackend.entity.*;
import com.example.eshopbackend.eshopbackend.modelconverter.AuditTrailModelConverter;
import com.example.eshopbackend.eshopbackend.modelconverter.InvoiceModelConverter;
import com.example.eshopbackend.eshopbackend.modelconverter.TransactionModelConverter;
import com.example.eshopbackend.eshopbackend.repository.*;
import com.example.eshopbackend.eshopbackend.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    WalletBankRepository walletBankRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    Utils utils;

    @Value("${admin.processsingWalletBankId}")
    private Long adminProcessingBankId;

    public String createBulkInvoice(OrderRequest orderRequest, Long buyerId){
       List<InvoiceRequest> invoiceRequestList = orderRequest.getInvoiceRequestList();
        Optional<UserEntity> optionalBuyerEntity = userRepository.findById(buyerId);


        if(optionalBuyerEntity.isPresent()) {
            // get buyer bank Details
            List<WallerBankEntity> buyerBankEntityList = walletBankRepository.findByUserEntity(optionalBuyerEntity.get());
            Optional<WallerBankEntity> optionalAdminTopupWalletEntity = walletBankRepository.findById(adminProcessingBankId);

            if(buyerBankEntityList.isEmpty() && optionalAdminTopupWalletEntity.isPresent()){
                return "Bank Account Not Found";
            }
            WallerBankEntity buyerBankEntity = buyerBankEntityList.get(0);
            WallerBankEntity adminProcessingBankEntity = optionalAdminTopupWalletEntity.get();

            Double buyerBankBalance = buyerBankEntity.getBalance();
            Double adminProcessingBankbalance = adminProcessingBankEntity.getBalance();

            if (!invoiceRequestList.isEmpty()) {
                for (InvoiceRequest invoiceRequest : invoiceRequestList) {

                    Double unitPrice = invoiceRequest.getUnitPrice();
                    Double discountRate = invoiceRequest.getDiscountRate();
                    Integer quantity = invoiceRequest.getQuantity();
                    Double discountedAmount = unitPrice*(1-discountRate/100)* quantity;
                    Double taxRate = invoiceRequest.getTaxRate();
                    Double shippingCharge = invoiceRequest.getShippingCharge();
                    Double FinalInvoiceAmount = shippingCharge + discountedAmount*(100 + taxRate)/100;


                    if(buyerBankBalance > FinalInvoiceAmount) {
                        System.out.println("Buyer Bank Balance -and- Invoice Amount: " + buyerBankBalance + " -and- " + FinalInvoiceAmount);
                        System.out.println("Admin Processing Bank Balance: " + adminProcessingBankbalance);

                        Optional<UserEntity> optionalSellerEntity = userRepository.findById(invoiceRequest.getSellerId());
                        Optional<AddressEntity> optionalAddressEntity = addressRepository.findById(invoiceRequest.getAddressId());
                        Optional<ProductEntity> optionalProductEntity = productRepository.findById(invoiceRequest.getProductId());
                        InvoiceEntity invoiceEntity = new InvoiceEntity();
                        InvoiceEntity savedInvoiceEntity = new InvoiceEntity();

                        if (optionalSellerEntity.isPresent()) {
                            System.out.println("Seller Found");
                        }
                        if (optionalBuyerEntity.isPresent()) {
                            System.out.println("Byuer Found");
                        }
                        if (optionalAddressEntity.isPresent()) {
                            System.out.println("Address Found");
                        }
                        if (optionalProductEntity.isPresent()) {
                            System.out.println("Product Found");
                        }
                        if (optionalSellerEntity.isPresent() && optionalBuyerEntity.isPresent() && optionalAddressEntity.isPresent()) {
                            System.out.println("All Found");

                            invoiceRequest.setOrderId(utils.generateOrderId());
                            invoiceRequest.setInvoiceState("ORDER_PLACED");
                            invoiceRequest.setIsArchive(false);
                            invoiceRequest.setPurchaseDate(new Date());

                            // Save Invoice
                            invoiceEntity = InvoiceModelConverter.requestToEntity(invoiceRequest, optionalSellerEntity.get(), optionalBuyerEntity.get(), optionalProductEntity.isPresent() ? optionalProductEntity.get() : null, optionalAddressEntity.isPresent() ? optionalAddressEntity.get() : null);
                            savedInvoiceEntity = invoiceRepository.save(invoiceEntity);
                            System.out.println("Invoice Created");

                            // Add Audit Trail
                            AuditTrailEntity auditTrailEntity = new AuditTrailEntity();
                            auditTrailEntity = AuditTrailModelConverter.requestToEntity(savedInvoiceEntity, savedInvoiceEntity.getInvoiceState());
                            auditTrailRepository.save(auditTrailEntity);
                            System.out.println("Audit Trail Added");

                            System.out.println("Invoice Created Successfully");

                            TransactionRequest transactionRequest = new TransactionRequest();
                            TransactionEntity transactionEntity = new TransactionEntity();
                            String transactionRefNo = utils.generateTransactionId();

                            System.out.println("Initiating Funding Debit from Buyer account");
                            transactionRequest.setReferenceNo(transactionRefNo);
                            transactionRequest.setAmount(FinalInvoiceAmount);
                            transactionRequest.setTransactionType("WALLET_DEBIT");
                            transactionRequest.setTransactionStatus("COMPLETED");
                            transactionRequest.setDescription("Invoice Purchase");
                            transactionRequest.setPaymentMethod(invoiceRequest.getPaymentMethod());
                            transactionRequest.setInvoiceId(savedInvoiceEntity.getId());
                            transactionRequest.setToUserId(adminProcessingBankEntity.getUserEntity().getId());

                            transactionEntity = TransactionModelConverter.requestToEntity(transactionRequest, optionalBuyerEntity.get(), buyerBankEntity, adminProcessingBankEntity, savedInvoiceEntity);
                            transactionRepository.save(transactionEntity);
                            // deduct invoice amount from buyer account
                            buyerBankEntity.setBalance(buyerBankBalance-FinalInvoiceAmount);
                            walletBankRepository.save(buyerBankEntity);
                            System.out.println("Funding Debit Completed from Buyer Account");

                            System.out.println("Initiating Funding Credit");
                            transactionRequest.setTransactionType("WALLET_CREDIT");
                            transactionRequest.setTransactionStatus("COMPLETED");

                            transactionEntity = TransactionModelConverter.requestToEntity(transactionRequest, adminProcessingBankEntity.getUserEntity(), buyerBankEntity, adminProcessingBankEntity, savedInvoiceEntity);
                            transactionRepository.save(transactionEntity);
                            // credit invoice amount to admin
                            adminProcessingBankEntity.setBalance(adminProcessingBankbalance + FinalInvoiceAmount);
                            walletBankRepository.save(adminProcessingBankEntity);
                            System.out.println("Funding Credit Completed for Admin account");

                        } else {
                            System.out.println("Not Found");
                        }
                    } else {
                        System.out.println("Low Balance");
                        return "Low Balance";
                    }
                }
            }
        }
       return "Invoice Created Successfully";
    }

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
        if(optionalSellerEntity.isPresent() && optionalBuyerEntity.isPresent() && optionalAddressEntity.isPresent()){
            System.out.println("All Found");
            invoiceRequest.setOrderId(utils.generateOrderId());
            invoiceRequest.setInvoiceState("ORDER_PLACED");

            invoiceEntity = InvoiceModelConverter.requestToEntity(invoiceRequest, optionalSellerEntity.get(), optionalBuyerEntity.get(),optionalProductEntity.isPresent() ? optionalProductEntity.get(): null, optionalAddressEntity.isPresent() ? optionalAddressEntity.get() : null);
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
        Optional<UserEntity> optionalSellerEntity = userRepository.findById(userId);
        List<InvoiceEntity> invoiceEntityList = invoiceRepository.findBySellerEntity(optionalSellerEntity.get());

        List<InvoiceResponse> invoiceListResponse = new ArrayList<>();
        if(!invoiceEntityList.isEmpty()){
            for(InvoiceEntity invoice : invoiceEntityList){
                InvoiceResponse invoiceResponse = InvoiceModelConverter.entityToResponse(invoice);
                invoiceListResponse.add(invoiceResponse);
            }
        }
        return invoiceListResponse;
    }

    public InvoiceResponse updateInvoiceState(Long sellerId, Long invoiceId, InvoiceRequest invoiceRequest){
        Optional<UserEntity> optionalSellerEntity = userRepository.findById(sellerId);
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        if(optionalSellerEntity.isPresent()){
            Optional<InvoiceEntity> optionalInvocieEntity = invoiceRepository.findByIdAndSellerEntity(invoiceId, optionalSellerEntity.get());
            invoiceEntity = optionalInvocieEntity.get();
            invoiceEntity.setInvoiceState(InvoiceStateCode.valueOf(invoiceRequest.getInvoiceState().toUpperCase().trim()));
            invoiceRepository.save(invoiceEntity);
            System.out.println("Invoice Updated");

            // Add Audit trail
            AuditTrailEntity auditTrailEntity = new AuditTrailEntity();
            auditTrailEntity = AuditTrailModelConverter.requestToEntity(invoiceEntity, InvoiceStateCode.valueOf(invoiceRequest.getInvoiceState().toUpperCase().trim()));
            auditTrailRepository.save(auditTrailEntity);
            System.out.println("Audit Trail Added");
        }

        return InvoiceModelConverter.entityToResponse(invoiceEntity);
    }
}
