package com.example.eshopbackend.eshopbackend.service.impl;

import com.example.eshopbackend.eshopbackend.datamodel.MasterProduct.MasterProductBrandRequest;
import com.example.eshopbackend.eshopbackend.datamodel.MasterProduct.MasterProductCategoryRequest;
import com.example.eshopbackend.eshopbackend.datamodel.MasterProduct.MasterProductModelRequest;
import com.example.eshopbackend.eshopbackend.datamodel.MasterProduct.MasterProductSubCategoryRequest;
import com.example.eshopbackend.eshopbackend.entity.masterProduct.MasterProductBrandEntity;
import com.example.eshopbackend.eshopbackend.entity.masterProduct.MasterProductCategoryEntity;
import com.example.eshopbackend.eshopbackend.entity.masterProduct.MasterProductModelEntity;
import com.example.eshopbackend.eshopbackend.entity.masterProduct.MasterProductSubCategoryEntity;
import com.example.eshopbackend.eshopbackend.repository.masterProduct.MasterProductBrandRepository;
import com.example.eshopbackend.eshopbackend.repository.masterProduct.MasterProductCategoryRepository;
import com.example.eshopbackend.eshopbackend.repository.masterProduct.MasterProductModelRepository;
import com.example.eshopbackend.eshopbackend.repository.masterProduct.MasterProductSubCategoryRepository;
import com.example.eshopbackend.eshopbackend.service.MasterProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MasterProductServiceImpl implements MasterProductService {
    @Autowired
    MasterProductCategoryRepository masterProductCategoryRepository;

    @Autowired
    MasterProductSubCategoryRepository masterProductSubCategoryRepository;
    @Autowired
    MasterProductBrandRepository masterProductBrandRepository;

    @Autowired
    MasterProductModelRepository masterProductModelRepository;

    @Override
    public MasterProductCategoryRequest addMasterProductCategory(MasterProductCategoryRequest request) {
        MasterProductCategoryEntity masterProductCategoryEntity = new MasterProductCategoryEntity();
        masterProductCategoryEntity.setDisplayName(request.getDisplayName());
        masterProductCategoryEntity.setCreatedDate(new Date());
        masterProductCategoryEntity.setIsArchive(false);
        masterProductCategoryRepository.save(masterProductCategoryEntity);
        return request;
    }

    @Override
    public MasterProductSubCategoryRequest addMasterProductSubCategory(MasterProductSubCategoryRequest request) {
        Optional<MasterProductCategoryEntity> optionalMasterProductCategoryEntity = masterProductCategoryRepository.findById(request.getMasterProductCategoryId());
        if(optionalMasterProductCategoryEntity.isPresent()){
            MasterProductSubCategoryEntity masterProductSubCategoryEntity = new MasterProductSubCategoryEntity();
            masterProductSubCategoryEntity.setMasterProductCategoryEntity(optionalMasterProductCategoryEntity.get());
            masterProductSubCategoryEntity.setDisplayName(request.getDisplayName());
            masterProductSubCategoryEntity.setCreatedDate(new Date());
            masterProductSubCategoryEntity.setIsArchive(false);
            masterProductSubCategoryRepository.save(masterProductSubCategoryEntity);
        } else {
            System.out.println("Master Product Category is not present");
        }

        return request;
    }


    @Override
    public MasterProductBrandRequest addMasterProductBrand(MasterProductBrandRequest request) {
        Optional<MasterProductCategoryEntity> optionalMasterProductCategoryEntity = masterProductCategoryRepository.findById(request.getMasterProductCategoryId());
        Optional<MasterProductSubCategoryEntity> optionalMasterProductSubCategoryEntity = masterProductSubCategoryRepository.findById(request.getMasterProductSubCategoryId());
        MasterProductBrandEntity masterProductBrandEntity = new MasterProductBrandEntity();
        if(optionalMasterProductCategoryEntity.isPresent() && optionalMasterProductSubCategoryEntity.isPresent()){
            masterProductBrandEntity.setBrnadName(request.getBrnadName());
            masterProductBrandEntity.setManufacturerDetails(request.getManufacturerDetails());
            masterProductBrandEntity.setMasterProductCategoryEntity(optionalMasterProductCategoryEntity.get());
            masterProductBrandEntity.setMasterProductSubCategoryEntity(optionalMasterProductSubCategoryEntity.get());
            masterProductBrandEntity.setCreatedDate(new Date());
            masterProductBrandEntity.setLastModifiedDate(new Date());
            masterProductBrandEntity.setIsArchive(false);
            masterProductBrandRepository.save(masterProductBrandEntity);
        } else {
            System.out.println("Product Not Found");
        }
        return request;
    }

    @Override
    public MasterProductModelRequest addMasterProductModel(MasterProductModelRequest request) {
        Optional<MasterProductCategoryEntity> optionalMasterProductCategoryEntity = masterProductCategoryRepository.findById(request.getMasterProductCategoryId());
        Optional<MasterProductSubCategoryEntity> optionalMasterProductSubCategoryEntity = masterProductSubCategoryRepository.findById(request.getMasterProductSubCategoryId());
        Optional<MasterProductBrandEntity> optionalMasterProductBrandEntity = masterProductBrandRepository.findById(request.getMasterProductBrandId());

        MasterProductModelEntity masterProductModelEntity = new MasterProductModelEntity();
        if(optionalMasterProductCategoryEntity.isPresent() && optionalMasterProductSubCategoryEntity.isPresent() && optionalMasterProductBrandEntity.isPresent()){
            masterProductModelEntity.setModelName(request.getModelName());
            masterProductModelEntity.setMarketRatePrice(request.getMarketRatePrice());
            masterProductModelEntity.setWarranty(request.getWarranty());
            masterProductModelEntity.setProductImageUrl(request.getProductImageUrl());
            masterProductModelEntity.setDescription(request.getDescription());
            masterProductModelEntity.setVariant(request.getVariant());
            masterProductModelEntity.setCreatedDate(new Date());
            masterProductModelEntity.setLastModifiedDate(new Date());
            masterProductModelEntity.setIsArchive(false);
            masterProductModelEntity.setMasterProductCategoryEntity(optionalMasterProductCategoryEntity.get());
            masterProductModelEntity.setMasterProductSubCategoryEntity(optionalMasterProductSubCategoryEntity.get());
            masterProductModelEntity.setMasterProductBrandEntity(optionalMasterProductBrandEntity.get());

            masterProductModelRepository.save(masterProductModelEntity);
        }
        return request;
    }

    @Override
    public List<MasterProductCategoryRequest> getAllMasterProductCategory(){

        List<MasterProductCategoryRequest> masterProductCategoryRequestList = new ArrayList<>();
        List<MasterProductCategoryEntity> masterProductCategoryEntityList = masterProductCategoryRepository.findAll();

        if(!masterProductCategoryEntityList.isEmpty()){
            for(MasterProductCategoryEntity masterProductCategoryEntity : masterProductCategoryEntityList){
                MasterProductCategoryRequest tempMasterProductRequest = new MasterProductCategoryRequest();
                tempMasterProductRequest.setId(masterProductCategoryEntity.getId());
                tempMasterProductRequest.setDisplayName(masterProductCategoryEntity.getDisplayName());
                tempMasterProductRequest.setCreatedDate(masterProductCategoryEntity.getCreatedDate());
                tempMasterProductRequest.setIsArchive(masterProductCategoryEntity.getIsArchive());
                masterProductCategoryRequestList.add(tempMasterProductRequest);
            }
        }
        return masterProductCategoryRequestList;
    }


    @Override
    public List<MasterProductSubCategoryRequest> getAllMasterProductSubCategory(){

        List<MasterProductSubCategoryRequest> masterProductSubCategoryRequestList = new ArrayList<>();
        List<MasterProductSubCategoryEntity> masterProductSubCategoryEntityList = masterProductSubCategoryRepository.findAll();

        if(!masterProductSubCategoryEntityList.isEmpty()){
            for(MasterProductSubCategoryEntity masterProductSubCategoryEntity : masterProductSubCategoryEntityList){

                MasterProductSubCategoryRequest tempMasterProductSubCategoryRequest = new MasterProductSubCategoryRequest();
                tempMasterProductSubCategoryRequest.setId(masterProductSubCategoryEntity.getId());
                tempMasterProductSubCategoryRequest.setDisplayName(masterProductSubCategoryEntity.getDisplayName());
                tempMasterProductSubCategoryRequest.setCreatedDate(masterProductSubCategoryEntity.getCreatedDate());
                tempMasterProductSubCategoryRequest.setIsArchive(masterProductSubCategoryEntity.getIsArchive());
                tempMasterProductSubCategoryRequest.setMasterProductCategoryId(masterProductSubCategoryEntity.getMasterProductCategoryEntity().getId());
                masterProductSubCategoryRequestList.add(tempMasterProductSubCategoryRequest);
            }
        }
        return masterProductSubCategoryRequestList;
    }

    @Override
    public List<MasterProductBrandRequest> getAllMasterProductBrand(){

        List<MasterProductBrandRequest> masterProductBrandRequestList = new ArrayList<>();
        List<MasterProductBrandEntity> masterProductBrandEntityList = masterProductBrandRepository.findAll();

        if(!masterProductBrandEntityList.isEmpty()){
            for(MasterProductBrandEntity masterProductBrandEntity : masterProductBrandEntityList){
                MasterProductBrandRequest tempMasterProductBrandRequest = new MasterProductBrandRequest();
                tempMasterProductBrandRequest.setId(masterProductBrandEntity.getId());
                tempMasterProductBrandRequest.setBrnadName(masterProductBrandEntity.getBrnadName());
                tempMasterProductBrandRequest.setCreatedDate(masterProductBrandEntity.getCreatedDate());
                tempMasterProductBrandRequest.setLastModifiedDate(masterProductBrandEntity.getLastModifiedDate());
                tempMasterProductBrandRequest.setManufacturerDetails(masterProductBrandEntity.getManufacturerDetails());
                tempMasterProductBrandRequest.setIsArchive(masterProductBrandEntity.getIsArchive());
                tempMasterProductBrandRequest.setMasterProductCategoryId(masterProductBrandEntity.getMasterProductCategoryEntity().getId());
                tempMasterProductBrandRequest.setMasterProductSubCategoryId(masterProductBrandEntity.getMasterProductSubCategoryEntity().getId());
                masterProductBrandRequestList.add(tempMasterProductBrandRequest);
            }
        }
        return masterProductBrandRequestList;
    }

    @Override
    public List<MasterProductModelRequest> getAllMasterProductModels(){

        List<MasterProductModelRequest> masterProductModelRequestList = new ArrayList<>();
        List<MasterProductModelEntity> masterProductModelEntityList = masterProductModelRepository.findAll();

        if(!masterProductModelEntityList.isEmpty()){
            for(MasterProductModelEntity masterProductModelEntity : masterProductModelEntityList){
                MasterProductModelRequest tempMasterProductModelRequest = new MasterProductModelRequest();
                tempMasterProductModelRequest.setId(masterProductModelEntity.getId());
                tempMasterProductModelRequest.setModelName(masterProductModelEntity.getModelName());
                tempMasterProductModelRequest.setVariant(masterProductModelEntity.getVariant());
                tempMasterProductModelRequest.setProductImageUrl(masterProductModelEntity.getProductImageUrl());
                tempMasterProductModelRequest.setMarketRatePrice(masterProductModelEntity.getMarketRatePrice());
                tempMasterProductModelRequest.setWarranty(masterProductModelEntity.getWarranty());
                tempMasterProductModelRequest.setDescription(masterProductModelEntity.getDescription());
                tempMasterProductModelRequest.setCreatedDate(masterProductModelEntity.getCreatedDate());
                tempMasterProductModelRequest.setLastModifiedDate(masterProductModelEntity.getLastModifiedDate());
                tempMasterProductModelRequest.setIsArchive(masterProductModelEntity.getIsArchive());
                tempMasterProductModelRequest.setMasterProductCategoryId(masterProductModelEntity.getMasterProductCategoryEntity().getId());
                tempMasterProductModelRequest.setMasterProductSubCategoryId(masterProductModelEntity.getMasterProductSubCategoryEntity().getId());
                tempMasterProductModelRequest.setMasterProductBrandId(masterProductModelEntity.getMasterProductBrandEntity().getId());
                masterProductModelRequestList.add(tempMasterProductModelRequest);
            }
        }
        return masterProductModelRequestList;
    }
}
