package com.example.eshopbackend.eshopbackend.service;

import com.example.eshopbackend.eshopbackend.datamodel.MasterProduct.MasterProductBrandRequest;
import com.example.eshopbackend.eshopbackend.datamodel.MasterProduct.MasterProductCategoryRequest;
import com.example.eshopbackend.eshopbackend.datamodel.MasterProduct.MasterProductModelRequest;
import com.example.eshopbackend.eshopbackend.datamodel.MasterProduct.MasterProductSubCategoryRequest;
import com.example.eshopbackend.eshopbackend.entity.masterProduct.MasterProductModelEntity;

import java.util.List;

public interface MasterProductService {

    public MasterProductCategoryRequest addMasterProductCategory(MasterProductCategoryRequest request);

    public MasterProductSubCategoryRequest addMasterProductSubCategory(MasterProductSubCategoryRequest request);

    public MasterProductBrandRequest addMasterProductBrand(MasterProductBrandRequest request);
    public MasterProductModelRequest addMasterProductModel(MasterProductModelRequest request);
    public List<MasterProductCategoryRequest> getAllMasterProductCategory();
    public List<MasterProductBrandRequest> getAllMasterProductBrand();
    public List<MasterProductSubCategoryRequest> getAllMasterProductSubCategory();
    public List<MasterProductModelRequest> getAllMasterProductModels();
}
