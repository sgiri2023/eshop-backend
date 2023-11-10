package com.example.eshopbackend.eshopbackend.service;

import com.example.eshopbackend.eshopbackend.datamodel.ProductRequest;
import com.example.eshopbackend.eshopbackend.datamodel.ProductResponse;
import com.example.eshopbackend.eshopbackend.datamodel.UserRequest;
import com.example.eshopbackend.eshopbackend.datamodel.UserResponse;

import java.util.List;

public interface ProductService {
    public ProductResponse addProduct(Long userId, ProductRequest productRequest);
    public List<ProductResponse> getProductList();
    public ProductResponse addProductFromMasterProduct(Long userId, ProductRequest productRequest);
}
