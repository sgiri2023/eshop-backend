package com.example.eshopbackend.eshopbackend.service.impl;

import com.example.eshopbackend.eshopbackend.common.utils.Utils;
import com.example.eshopbackend.eshopbackend.datamodel.Dataset.ProductDataSet;
import com.example.eshopbackend.eshopbackend.datamodel.Dataset.ProjectionDataset;
import com.example.eshopbackend.eshopbackend.datamodel.ProductRequest;
import com.example.eshopbackend.eshopbackend.datamodel.ProductResponse;
import com.example.eshopbackend.eshopbackend.entity.InvoiceEntity;
import com.example.eshopbackend.eshopbackend.entity.ProductEntity;
import com.example.eshopbackend.eshopbackend.entity.UserEntity;
import com.example.eshopbackend.eshopbackend.entity.masterProduct.MasterProductModelEntity;
import com.example.eshopbackend.eshopbackend.modelconverter.ProductModelConverter;
import com.example.eshopbackend.eshopbackend.repository.InvoiceRepository;
import com.example.eshopbackend.eshopbackend.repository.ProductRepository;
import com.example.eshopbackend.eshopbackend.repository.UserRepository;
import com.example.eshopbackend.eshopbackend.repository.masterProduct.MasterProductBrandRepository;
import com.example.eshopbackend.eshopbackend.repository.masterProduct.MasterProductCategoryRepository;
import com.example.eshopbackend.eshopbackend.repository.masterProduct.MasterProductModelRepository;
import com.example.eshopbackend.eshopbackend.repository.masterProduct.MasterProductSubCategoryRepository;
import com.example.eshopbackend.eshopbackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    MasterProductCategoryRepository masterProductCategoryRepository;

    @Autowired
    MasterProductSubCategoryRepository masterProductSubCategoryRepository;
    @Autowired
    MasterProductBrandRepository masterProductBrandRepository;

    @Autowired
    MasterProductModelRepository masterProductModelRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    Utils utils;

    @Override
    public ProductResponse addProduct(Long userId, ProductRequest productRequest) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        ProductEntity newProductEntity = new ProductEntity();
        if (optionalUserEntity.isPresent()) {
            newProductEntity = ProductModelConverter.requestToEntity(productRequest, optionalUserEntity.get(), null);

        }
        ProductEntity savedProductEntity = productRepository.save(newProductEntity);

        return ProductModelConverter.entityToResponse(savedProductEntity);
    }

    @Override
    public ProductResponse addProductFromMasterProduct(Long userId, ProductRequest productRequest) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        Optional<MasterProductModelEntity> optionalMasterProductModelEntity = masterProductModelRepository.findById(productRequest.getMasterProductModelId());

        ProductEntity newProductEntity = new ProductEntity();
        if (optionalUserEntity.isPresent() && optionalMasterProductModelEntity.isPresent()) {
            productRequest.setCreatedDate(new Date());
            productRequest.setLastModifyDate(new Date());
            newProductEntity = ProductModelConverter.requestToEntity(productRequest, optionalUserEntity.get(), optionalMasterProductModelEntity.get());

        }
        ProductEntity savedProductEntity = productRepository.save(newProductEntity);

        return ProductModelConverter.entityToResponse(savedProductEntity);
    }

    @Override
    public List<ProductResponse> getProductList() {
        List<ProductEntity> productEntityList = new ArrayList<>();
        productEntityList = productRepository.findAll();

        List<ProductResponse> productResponseList = new ArrayList<>();
        if(!productEntityList.isEmpty()){
            for(ProductEntity product : productEntityList){
                productResponseList.add(ProductModelConverter.entityToResponse(product));
            }
        }
        return productResponseList;
    }

    public ProjectionDataset getProductProjection(Long userId){

        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);

        List<String> monthList = new ArrayList<>();
        List<ProductDataSet> productDataSet = new ArrayList<>();
        ProjectionDataset<ProductDataSet> projectionDataset = new ProjectionDataset<>();

        if(optionalUserEntity.isPresent()){
            UserEntity userEntity = optionalUserEntity.get();
            List<ProductEntity> productEntityList = productRepository.findBySellerEntity(userEntity);


            if(!productEntityList.isEmpty()){

                System.out.println("User Product Found: " + productEntityList.size());
                monthList = utils.getMonthListFromToCurrentDate(userEntity.getCreatedDate());

                for(ProductEntity productEntity: productEntityList){

                    ProductDataSet dataset = new ProductDataSet();
                    List<Long> productQuantityCountList = new ArrayList<>();
                    System.out.println("Checking for Product: " + productEntity.getMasterProductModelEntity().getModelName());
                    System.out.println("Month List: "+ monthList);
                    for (String month : monthList) {
                        String[] result = month.split("-");
                        Integer monthIndex = utils.getMonthNameFromMonthIndex(result[0]);
                        Integer year = Integer.parseInt(result[1]);
                        Date monthStartDate = utils.getFirstDateOfMonth(monthIndex, year);
                        Date monthEndDate = utils.getEndDateOfMonth(monthIndex, year);

                        Long quantity = invoiceRepository.countQuantityByProductIdAndSellerIdAndInvoicedateBetweenDates(productEntity.getId(), userId, monthStartDate, monthEndDate);

                        System.out.println("Quantity: "+ quantity);
                        productQuantityCountList.add(quantity == null ? 0 : quantity);
                    }

                    dataset.setLabel(productEntity.getMasterProductModelEntity().getModelName());
                    dataset.setProductQuantityCountList(productQuantityCountList);
                    System.out.println("Product Quantity Count Array: " + dataset);
                    productDataSet.add(dataset);
                }
                projectionDataset.setDataList(productDataSet);
            }
        }

        projectionDataset.setMonthList(monthList);

        return projectionDataset;
    }


}
