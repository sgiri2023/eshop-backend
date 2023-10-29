package com.example.eshopbackend.eshopbackend.service.impl;

import com.example.eshopbackend.eshopbackend.datamodel.ProductRequest;
import com.example.eshopbackend.eshopbackend.datamodel.ProductResponse;
import com.example.eshopbackend.eshopbackend.entity.ProductEntity;
import com.example.eshopbackend.eshopbackend.entity.UserEntity;
import com.example.eshopbackend.eshopbackend.modelconverter.ProductModelConverter;
import com.example.eshopbackend.eshopbackend.repository.ProductRepository;
import com.example.eshopbackend.eshopbackend.repository.UserRepository;
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


    @Override
    public ProductResponse addProduct(Long userId, ProductRequest productRequest) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        ProductEntity newProductEntity = new ProductEntity();
        if (optionalUserEntity.isPresent()) {
            newProductEntity = ProductModelConverter.requestToEntity(productRequest, optionalUserEntity.get());

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


}
