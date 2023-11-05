package com.example.eshopbackend.eshopbackend.service.impl;

import com.example.eshopbackend.eshopbackend.common.enumConstant.InvoiceStateCode;
import com.example.eshopbackend.eshopbackend.common.utils.Utils;
import com.example.eshopbackend.eshopbackend.datamodel.Dataset.SellerDataSet;
import com.example.eshopbackend.eshopbackend.datamodel.Dataset.SellerProjectionDataset;
import com.example.eshopbackend.eshopbackend.datamodel.Dataset.TotalOrder;
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

import java.text.SimpleDateFormat;
import java.util.*;

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

    @Override
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

                    if(buyerBankBalance >= FinalInvoiceAmount) {
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
                            invoiceRequest.setInvoiceNo(utils.generateInvoiceNo());
                            invoiceRequest.setInvoiceState("ORDER_PLACED");
                            invoiceRequest.setIsArchive(false);
                            invoiceRequest.setPurchaseDate(new Date());
                            invoiceRequest.setCreatedDate(new Date());
                            invoiceRequest.setLastModifyDate(new Date());
                            invoiceRequest.setIsInvoiceSettle(false);

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
                            transactionRequest.setDescription(invoiceRequest.getOrderId() +" " + "Order Purchase");
                            transactionRequest.setPaymentMethod(invoiceRequest.getPaymentMethod());
                            transactionRequest.setInvoiceId(savedInvoiceEntity.getId());
                            transactionRequest.setToUserId(adminProcessingBankEntity.getUserEntity().getId());

                            transactionEntity = TransactionModelConverter.requestToEntity(transactionRequest, optionalBuyerEntity.get(), buyerBankEntity, adminProcessingBankEntity, savedInvoiceEntity);
                            transactionRepository.save(transactionEntity);
                            // deduct invoice amount from buyer account
                            buyerBankEntity.setBalance(buyerBankBalance-FinalInvoiceAmount);
                            buyerBankEntity.setLastModifiedDate(new Date());
                            walletBankRepository.save(buyerBankEntity);
                            System.out.println("Funding Debit Completed from Buyer Account");

                            System.out.println("Initiating Funding Credit");
                            transactionRequest.setTransactionType("WALLET_CREDIT");
                            transactionRequest.setTransactionStatus("COMPLETED");

                            transactionEntity = TransactionModelConverter.requestToEntity(transactionRequest, adminProcessingBankEntity.getUserEntity(), buyerBankEntity, adminProcessingBankEntity, savedInvoiceEntity);
                            transactionRepository.save(transactionEntity);
                            // credit invoice amount to admin
                            adminProcessingBankEntity.setBalance(adminProcessingBankbalance + FinalInvoiceAmount);
                            adminProcessingBankEntity.setLastModifiedDate(new Date());
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
            invoiceRequest.setIsInvoiceSettle(false);
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

    public List<InvoiceResponse> getAllInvoices() {
        List<InvoiceEntity> invoiceEntityList = invoiceRepository.findAll();

        List<InvoiceResponse> invoiceListResponse = new ArrayList<>();
        if(!invoiceEntityList.isEmpty()){
            for(InvoiceEntity invoice : invoiceEntityList){
                InvoiceResponse invoiceResponse = InvoiceModelConverter.entityToResponse(invoice);
                invoiceListResponse.add(invoiceResponse);
            }
        }
        return invoiceListResponse;
    }

    public Integer getAllInvoiceBetweenDates(Long userId, Integer month, Integer year){
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        Integer invoiceCount = 0;
        if(optionalUserEntity.isPresent()){
            UserEntity userEntity = optionalUserEntity.get();
            Date monthStart = utils.getFirstDateOfMonth(month, year);
            Date monthEnd = utils.getEndDateOfMonth(month, year);

            System.out.println("Calculated month start date : " + monthStart);
            System.out.println("Calculated month end date : " + monthEnd);

            List<InvoiceEntity> invoiceEnttiyList = new ArrayList<>();
            if(userEntity.getIsAdmin() == true){
                System.out.println("1");
                invoiceEnttiyList = invoiceRepository.getAllInvoiceBetweenDates(monthStart, monthEnd);
            } else if(userEntity.getIsAdmin() == false && userEntity.getIsCustomer() == true){
                System.out.println("2");
                invoiceEnttiyList = invoiceRepository.getBuyerInvoiceBetweenDates(userEntity.getId(), monthStart, monthEnd);
            }else if(userEntity.getIsAdmin() == false && userEntity.getIsCustomer() == false){
                System.out.println("3");
                invoiceEnttiyList = invoiceRepository.getSellerInvoiceBetweenDates(userEntity.getId(), monthStart, monthEnd);
            }

            invoiceCount = invoiceEnttiyList.size();
            System.out.println(".......Invoice Count: " + invoiceCount);
        }
        return invoiceCount;
    }

    public Double calculateTotalInvoicePriceMonthWise(Long userId, Integer month, Integer year){
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        Double totalInvoicePrice = 0.0;

        if(optionalUserEntity.isPresent()) {

            UserEntity userEntity = optionalUserEntity.get();
            Date monthStart = utils.getFirstDateOfMonth(month, year);
            Date monthEnd = utils.getEndDateOfMonth(month, year);

            System.out.println("Calculated month start date : " + monthStart);
            System.out.println("Calculated month end date : " + monthEnd);

            List<InvoiceEntity> invoiceEnttiyList = new ArrayList<>();
            if(userEntity.getIsAdmin() == true){
                System.out.println("4");
                invoiceEnttiyList = invoiceRepository.getAllInvoiceBetweenDates(monthStart, monthEnd);
            } else if(userEntity.getIsAdmin() == false && userEntity.getIsCustomer() == true){
                System.out.println("5");
                invoiceEnttiyList = invoiceRepository.getBuyerInvoiceBetweenDates(userEntity.getId(), monthStart, monthEnd);
            }else if(userEntity.getIsAdmin() == false && userEntity.getIsCustomer() == false){
                System.out.println("6");
                invoiceEnttiyList = invoiceRepository.getSellerInvoiceBetweenDates(userEntity.getId(), monthStart, monthEnd);
            }

            for (InvoiceEntity invoiceEntity : invoiceEnttiyList) {
                InvoiceResponse invoiceResponse = InvoiceModelConverter.entityToResponse(invoiceEntity);
                totalInvoicePrice = totalInvoicePrice + invoiceResponse.getFinalAmount();
            }

        }
        System.out.println(".......Total Invoice Price: " + totalInvoicePrice);
        return totalInvoicePrice;
    }

    public TotalOrder datasetGetMonthwiseTotalOrder(Long userId){
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        List<String> monthList = new ArrayList<>();

        TotalOrder totalOrderDataset = new TotalOrder();
        if(optionalUserEntity.isPresent()) {

            List<Integer> invoiceCountList = new ArrayList<>();
            List<Double> totalInvoicePriceList = new ArrayList<>();
            Integer averageOrderPerMonth = 0;
            Integer totalOrder = 0;

            UserEntity userEntity = optionalUserEntity.get();
            monthList = utils.getMonthListFromToCurrentDate(userEntity.getCreatedDate());

            for (String month : monthList) {
                String[] result = month.split("-");
                Integer monthIndex = utils.getMonthNameFromMonthIndex(result[0]);
                Integer invoiceCount = this.getAllInvoiceBetweenDates(userEntity.getId(), monthIndex, Integer.parseInt(result[1]));
                Double totalInvoicePrice = this.calculateTotalInvoicePriceMonthWise(userEntity.getId(), monthIndex, Integer.parseInt(result[1]));
                invoiceCountList.add(invoiceCount);
                totalInvoicePriceList.add(totalInvoicePrice);
                totalOrder = totalOrder + invoiceCount;
            }
            averageOrderPerMonth = totalOrder/invoiceCountList.size();

            totalOrderDataset.setMonthList(monthList);
            totalOrderDataset.setInvoiceCountList(invoiceCountList);
            totalOrderDataset.setTotalMonthPriceList(totalInvoicePriceList);
            totalOrderDataset.setAverageOrderPermonth(averageOrderPerMonth);
            totalOrderDataset.setTotalOrder(totalOrder);
            System.out.println("Total order Dataset: " + totalOrderDataset);
        }
        return totalOrderDataset;
    }

    public SellerProjectionDataset getAllSellerOrderProjection(Long userId){
        Optional<UserEntity> optionalAdminUserEntity = userRepository.findById(userId);
        List<UserEntity> sellerEntityList = userRepository.findByIsAdminIsFalseAndIsCustomer(false);
        System.out.println("Seller Projection"+optionalAdminUserEntity.isPresent() + "-"  + sellerEntityList.size());
        SellerProjectionDataset sellerProjectionDataset = new SellerProjectionDataset();
        List<SellerDataSet> sellerDatasetList = new ArrayList<>();
        List<String> monthList = new ArrayList<>();

        if(!sellerEntityList.isEmpty() && optionalAdminUserEntity.isPresent()){
            UserEntity adminUserEntity = optionalAdminUserEntity.get();
            monthList = utils.getMonthListFromToCurrentDate(adminUserEntity.getCreatedDate());

            System.out.println("Seller List: "+ sellerEntityList.size());
            for(UserEntity userEntity : sellerEntityList){
                System.out.println("Seller: " + userEntity.getFirstName() + " " + userEntity.getLastName());
                SellerDataSet sellerDataSet = new SellerDataSet();

                List<Integer> invoiceCountList = new ArrayList<>();
                List<Double> totalInvoicePriceList = new ArrayList<>();
                Integer averageOrderPerMonth = 0;
                Integer totalOrder = 0;

                for (String month : monthList) {
                    String[] result = month.split("-");
                    Integer monthIndex = utils.getMonthNameFromMonthIndex(result[0]);
                    Integer invoiceCount = this.getAllInvoiceBetweenDates(userEntity.getId(), monthIndex, Integer.parseInt(result[1]));
                    Double totalInvoicePrice = this.calculateTotalInvoicePriceMonthWise(userEntity.getId(), monthIndex, Integer.parseInt(result[1]));
                    invoiceCountList.add(invoiceCount);
                    totalInvoicePriceList.add(totalInvoicePrice);
                    totalOrder = totalOrder + invoiceCount;
                }
                averageOrderPerMonth = totalOrder/invoiceCountList.size();

                sellerDataSet.setLabel(userEntity.getFirstName() + " " + userEntity.getLastName());
                sellerDataSet.setInvoiceCountList(invoiceCountList);
                sellerDataSet.setTotalMonthPriceList(totalInvoicePriceList);
                sellerDataSet.setTotalOrder(totalOrder);
                sellerDataSet.setAverageOrderPermonth(averageOrderPerMonth);

                sellerDatasetList.add(sellerDataSet);
            }
            sellerProjectionDataset.setSellerDatasetList(sellerDatasetList);
        }

        sellerProjectionDataset.setMonthList(monthList);

        return sellerProjectionDataset;
    }

    public SellerProjectionDataset getAllBuyerOrderProjection(Long userId){
        Optional<UserEntity> optionalAdminUserEntity = userRepository.findById(userId);
        List<UserEntity> sellerEntityList = userRepository.findByIsAdminIsFalseAndIsCustomer(true);
        System.out.println("Buyer Projection"+optionalAdminUserEntity.isPresent() + "-"  + sellerEntityList.size());
        SellerProjectionDataset sellerProjectionDataset = new SellerProjectionDataset();
        List<SellerDataSet> sellerDatasetList = new ArrayList<>();
        List<String> monthList = new ArrayList<>();

        if(!sellerEntityList.isEmpty() && optionalAdminUserEntity.isPresent()){
            UserEntity adminUserEntity = optionalAdminUserEntity.get();
            monthList = utils.getMonthListFromToCurrentDate(adminUserEntity.getCreatedDate());

            System.out.println("Seller List: "+ sellerEntityList.size());
            for(UserEntity userEntity : sellerEntityList){
                System.out.println("Seller: " + userEntity.getFirstName() + " " + userEntity.getLastName());
                SellerDataSet sellerDataSet = new SellerDataSet();

                List<Integer> invoiceCountList = new ArrayList<>();
                List<Double> totalInvoicePriceList = new ArrayList<>();
                Integer averageOrderPerMonth = 0;
                Integer totalOrder = 0;

                for (String month : monthList) {
                    String[] result = month.split("-");
                    Integer monthIndex = utils.getMonthNameFromMonthIndex(result[0]);
                    Integer invoiceCount = this.getAllInvoiceBetweenDates(userEntity.getId(), monthIndex, Integer.parseInt(result[1]));
                    Double totalInvoicePrice = this.calculateTotalInvoicePriceMonthWise(userEntity.getId(), monthIndex, Integer.parseInt(result[1]));
                    invoiceCountList.add(invoiceCount);
                    totalInvoicePriceList.add(totalInvoicePrice);
                    totalOrder = totalOrder + invoiceCount;
                }
                averageOrderPerMonth = totalOrder/invoiceCountList.size();

                sellerDataSet.setLabel(userEntity.getFirstName() + " " + userEntity.getLastName());
                sellerDataSet.setInvoiceCountList(invoiceCountList);
                sellerDataSet.setTotalMonthPriceList(totalInvoicePriceList);
                sellerDataSet.setTotalOrder(totalOrder);
                sellerDataSet.setAverageOrderPermonth(averageOrderPerMonth);

                sellerDatasetList.add(sellerDataSet);
            }
            sellerProjectionDataset.setSellerDatasetList(sellerDatasetList);
        }

        sellerProjectionDataset.setMonthList(monthList);

        return sellerProjectionDataset;
    }

    @Override
    public InvoiceResponse updateInvoiceState(Long userId, Long invoiceId, InvoiceRequest invoiceRequest) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);

        InvoiceEntity invoiceEntity = new InvoiceEntity();
        Optional<InvoiceEntity> optionalInvoiceEntity = invoiceRepository.findById(invoiceId);
        if (optionalInvoiceEntity.isPresent()) {
            //Optional<InvoiceEntity> optionalInvocieEntity = invoiceRepository.findByIdAndSellerEntity(invoiceId, optionalSellerEntity.get());
            invoiceEntity = optionalInvoiceEntity.get();
            invoiceEntity.setInvoiceState(InvoiceStateCode.valueOf(invoiceRequest.getInvoiceState().toUpperCase().trim()));
            invoiceEntity.setLastModifiedDate(new Date());
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

    @Override
    public String processInvoicePaymentToSeller(Long invoiceId, InvoiceRequest invoiceRequest){

        Optional<InvoiceEntity> optionalInvoiceEntity = invoiceRepository.findById(invoiceId);
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        System.out.println(".......Invoice Id: " + invoiceId);
        System.out.println("Invocie Present: " + optionalInvoiceEntity.isPresent());
        System.out.println("Invocie Settle: "+ optionalInvoiceEntity.get().getIsInvoiceSettle());
        System.out.println("Iinvoice State: "+ invoiceRequest.getInvoiceState().toUpperCase().trim() +" "+ InvoiceStateCode.get("ORDER_SETTLED"));
        if (optionalInvoiceEntity.isPresent() && optionalInvoiceEntity.get().getIsInvoiceSettle() == false) {

            System.out.println("Start Processing");
            invoiceEntity = optionalInvoiceEntity.get();
            invoiceEntity.setInvoiceState(InvoiceStateCode.valueOf(invoiceRequest.getInvoiceState().toUpperCase().trim()));
            invoiceEntity.setLastModifiedDate(new Date());
            invoiceEntity.setIsInvoiceSettle(true);
            invoiceRepository.save(invoiceEntity);
            System.out.println("Invoice Updated");

            // Add Audit trail
            AuditTrailEntity auditTrailEntity = new AuditTrailEntity();
            auditTrailEntity = AuditTrailModelConverter.requestToEntity(invoiceEntity, InvoiceStateCode.valueOf(invoiceRequest.getInvoiceState().toUpperCase().trim()));
            auditTrailRepository.save(auditTrailEntity);
            System.out.println("Audit Trail Added");


            // Process Payment To Seller Account
            System.out.println("Process Amount Transafer to Selller Account");
            Optional<WallerBankEntity> optionalAdminProcessingBankAccount = walletBankRepository.findById(adminProcessingBankId);
            List<WallerBankEntity> sellerBankEntityList = walletBankRepository.findByUserEntity(optionalInvoiceEntity.get().getSellerEntity());
            if(sellerBankEntityList.isEmpty() && optionalAdminProcessingBankAccount.isPresent()){
                return "Bank Account Not Found";
            } else{
                System.out.println("Bank Found");
            }
            WallerBankEntity sellerBankEntity = sellerBankEntityList.get(0);
            WallerBankEntity adminProcessingBankEntity = optionalAdminProcessingBankAccount.get();

            System.out.println("S1");
            Double adminProcessingBankbalance = adminProcessingBankEntity.getBalance();
            Double sellerBankBalance = sellerBankEntity.getBalance();

            Double unitPrice = invoiceEntity.getUnitPrice();
            Double discountRate = invoiceEntity.getDiscountRate();
            Integer quantity = invoiceEntity.getQuantity();
            Double discountedAmount = unitPrice*(1-discountRate/100)* quantity;
            Double taxRate = invoiceEntity.getTaxRate();
            Double shippingCharge = invoiceEntity.getShippingCharge();
            Double FinalInvoiceAmount = shippingCharge + discountedAmount*(100 + taxRate)/100;
            System.out.println("Admin Processing Bank Balance: " + adminProcessingBankbalance);
            System.out.println("Seller Bank Balance" + sellerBankBalance);
            System.out.println("Final Invoice Amount: " + FinalInvoiceAmount);
            if(adminProcessingBankbalance >= FinalInvoiceAmount) {

                // Process Funding Debit from Admin Processing bank Account
                TransactionRequest transactionRequest = new TransactionRequest();
                TransactionEntity transactionEntity = new TransactionEntity();
                String transactionRefNo = utils.generateTransactionId();

                System.out.println("Initiating Funding Debit from Admin Processing account");
                transactionRequest.setReferenceNo(transactionRefNo);
                transactionRequest.setAmount(FinalInvoiceAmount);
                transactionRequest.setTransactionType("WALLET_DEBIT");
                transactionRequest.setTransactionStatus("COMPLETED");
                transactionRequest.setDescription(invoiceEntity.getOrderId() + " " + "Order Purchase");
                transactionRequest.setPaymentMethod(invoiceEntity.getPaymentMethod());
                transactionRequest.setInvoiceId(invoiceEntity.getId());
                transactionRequest.setToUserId(sellerBankEntity.getUserEntity().getId());

                transactionEntity = TransactionModelConverter.requestToEntity(transactionRequest, adminProcessingBankEntity.getUserEntity(), adminProcessingBankEntity, sellerBankEntity, invoiceEntity);
                transactionRepository.save(transactionEntity);
                // Debit invoice amount from Admin Processing bank account
                adminProcessingBankEntity.setBalance(adminProcessingBankbalance - FinalInvoiceAmount);
                adminProcessingBankEntity.setLastModifiedDate(new Date());
                walletBankRepository.save(adminProcessingBankEntity);
                System.out.println("Funding Debit Completed from Admin Processing bank Account");

                // Process Funding Credit To Seller Account
                System.out.println("Initiating Funding Credit");
                transactionRequest.setTransactionType("WALLET_CREDIT");
                transactionRequest.setTransactionStatus("COMPLETED");

                transactionEntity = TransactionModelConverter.requestToEntity(transactionRequest, sellerBankEntity.getUserEntity(), adminProcessingBankEntity, sellerBankEntity, invoiceEntity);
                transactionRepository.save(transactionEntity);
                // Credit invoice amount to Seller Bank Account
                sellerBankEntity.setBalance(sellerBankBalance + FinalInvoiceAmount);
                sellerBankEntity.setLastModifiedDate(new Date());
                walletBankRepository.save(sellerBankEntity);
                System.out.println("Funding Credit Completed for Seller Account");
                return "Payment Successfull";
            }
        }
        return "Payment Error";
    }

    @Override
    public String processRefundInvoicePaymentToUser(Long invoiceId, InvoiceRequest invoiceRequest){

        Optional<InvoiceEntity> optionalInvoiceEntity = invoiceRepository.findById(invoiceId);
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        System.out.println(".......Invoice Id: " + invoiceId);
        System.out.println("Invoice Present: " + optionalInvoiceEntity.isPresent());
        System.out.println("Invoice State: "+ optionalInvoiceEntity.get().getIsInvoiceSettle());
        System.out.println("Invoice State: "+ invoiceRequest.getInvoiceState().toUpperCase().trim() +" "+ InvoiceStateCode.get("ORDER_CANCELLED"));
        if (optionalInvoiceEntity.isPresent() && optionalInvoiceEntity.get().getIsInvoiceSettle() == false) {

            System.out.println("Start Processing");
            invoiceEntity = optionalInvoiceEntity.get();

            // Process Payment To User Account
            System.out.println("Process Amount Transafer to User Account");
            Optional<WallerBankEntity> optionalAdminProcessingBankAccount = walletBankRepository.findById(adminProcessingBankId);
            List<WallerBankEntity> userBankEntityList = walletBankRepository.findByUserEntity(optionalInvoiceEntity.get().getBuyerEntity());
            if(userBankEntityList.isEmpty() && optionalAdminProcessingBankAccount.isPresent()){
                return "Bank Account Not Found";
            } else{
                System.out.println("Bank Found");
            }
            WallerBankEntity userBankEntity = userBankEntityList.get(0);
            WallerBankEntity adminProcessingBankEntity = optionalAdminProcessingBankAccount.get();

            Double adminProcessingBankbalance = adminProcessingBankEntity.getBalance();
            Double sellerBankBalance = userBankEntity.getBalance();

            Double unitPrice = invoiceEntity.getUnitPrice();
            Double discountRate = invoiceEntity.getDiscountRate();
            Integer quantity = invoiceEntity.getQuantity();
            Double discountedAmount = unitPrice*(1-discountRate/100)* quantity;
            Double taxRate = invoiceEntity.getTaxRate();
            Double shippingCharge = invoiceEntity.getShippingCharge();
            Double FinalInvoiceAmount = shippingCharge + discountedAmount*(100 + taxRate)/100;
            System.out.println("Admin Processing Bank Balance: " + adminProcessingBankbalance);
            System.out.println("Seller Bank Balance" + sellerBankBalance);
            System.out.println("Final Invoice Amount: " + FinalInvoiceAmount);
            if(adminProcessingBankbalance >= FinalInvoiceAmount) {

                invoiceEntity.setInvoiceState(InvoiceStateCode.valueOf(invoiceRequest.getInvoiceState().toUpperCase().trim()));
                invoiceEntity.setLastModifiedDate(new Date());
                invoiceEntity.setIsInvoiceSettle(true);
                invoiceRepository.save(invoiceEntity);
                System.out.println("Invoice Updated");

                // Add Audit trail
                AuditTrailEntity auditTrailEntity = new AuditTrailEntity();
                auditTrailEntity = AuditTrailModelConverter.requestToEntity(invoiceEntity, InvoiceStateCode.valueOf(invoiceRequest.getInvoiceState().toUpperCase().trim()));
                auditTrailRepository.save(auditTrailEntity);
                System.out.println("Audit Trail Added");

                // Process Funding Debit from Admin Processing bank Account
                TransactionRequest transactionRequest = new TransactionRequest();
                TransactionEntity transactionEntity = new TransactionEntity();
                String transactionRefNo = utils.generateTransactionId();

                System.out.println("Initiating Funding Debit from Admin Processing account");
                transactionRequest.setReferenceNo(transactionRefNo);
                transactionRequest.setAmount(FinalInvoiceAmount);
                transactionRequest.setTransactionType("WALLET_DEBIT");
                transactionRequest.setTransactionStatus("COMPLETED");
                transactionRequest.setDescription(invoiceEntity.getOrderId() + " " + "Order Refund");
                transactionRequest.setPaymentMethod(invoiceEntity.getPaymentMethod());
                transactionRequest.setInvoiceId(invoiceEntity.getId());
                transactionRequest.setToUserId(userBankEntity.getUserEntity().getId());

                transactionEntity = TransactionModelConverter.requestToEntity(transactionRequest, adminProcessingBankEntity.getUserEntity(), adminProcessingBankEntity, userBankEntity, invoiceEntity);
                transactionRepository.save(transactionEntity);
                // Debit invoice amount from Admin Processing bank account
                adminProcessingBankEntity.setBalance(adminProcessingBankbalance - FinalInvoiceAmount);
                adminProcessingBankEntity.setLastModifiedDate(new Date());
                walletBankRepository.save(adminProcessingBankEntity);
                System.out.println("Funding Debit Completed from Admin Processing bank Account");

                // Process Funding Credit To user Account
                System.out.println("Initiating Funding Credit");
                transactionRequest.setTransactionType("WALLET_CREDIT");
                transactionRequest.setTransactionStatus("COMPLETED");

                transactionEntity = TransactionModelConverter.requestToEntity(transactionRequest, userBankEntity.getUserEntity(), adminProcessingBankEntity, userBankEntity, invoiceEntity);
                transactionRepository.save(transactionEntity);
                // Credit invoice amount to user Bank Account
                userBankEntity.setBalance(sellerBankBalance + FinalInvoiceAmount);
                userBankEntity.setLastModifiedDate(new Date());
                walletBankRepository.save(userBankEntity);
                System.out.println("Funding Credit Completed for user Account");

                return "Payment Successfull";
            }
        }
        return "Payment Error";
    }
}
