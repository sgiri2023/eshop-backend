package com.example.eshopbackend.eshopbackend.controller;

import com.example.eshopbackend.eshopbackend.datamodel.MasterProduct.MasterProductBrandRequest;
import com.example.eshopbackend.eshopbackend.datamodel.MasterProduct.MasterProductCategoryRequest;
import com.example.eshopbackend.eshopbackend.datamodel.MasterProduct.MasterProductModelRequest;
import com.example.eshopbackend.eshopbackend.datamodel.MasterProduct.MasterProductSubCategoryRequest;
import com.example.eshopbackend.eshopbackend.datamodel.ProductRequest;
import com.example.eshopbackend.eshopbackend.datamodel.SessionDataModel;
import com.example.eshopbackend.eshopbackend.service.impl.MasterProductServiceImpl;
import com.example.eshopbackend.eshopbackend.session.SessionDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.eshopbackend.eshopbackend.session.SessionStore.SESSION_TRACKER;

@RestController
@RequestMapping("/api/master-product")
public class MasterProductController {
    @Autowired
    private SessionDataManager sessionDataManager;

    @Autowired
    private MasterProductServiceImpl masterProductService;


    // http://localhost:8090/api/master-product/category/add
    @PostMapping("/category/add")
    public ResponseEntity<?> addMasterProductCategory(@RequestBody MasterProductCategoryRequest request){
        return new ResponseEntity<>(masterProductService.addMasterProductCategory(request), HttpStatus.OK);
    }

    // http://localhost:8090/api/master-product/sub-category/add
    @PostMapping("/sub-category/add")
    public ResponseEntity<?> addMasterProductSubCategory(@RequestBody MasterProductSubCategoryRequest request){
        return new ResponseEntity<>(masterProductService.addMasterProductSubCategory(request), HttpStatus.OK);
    }

    // http://localhost:8090/api/master-product/brand/add
    @PostMapping("/brand/add")
    public ResponseEntity<?> addMasterProductBrand(@RequestBody MasterProductBrandRequest request){
        return new ResponseEntity<>(masterProductService.addMasterProductBrand(request), HttpStatus.OK);
    }

    // http://localhost:8090/api/master-product/model/add
    @PostMapping("/model/add")
    public ResponseEntity<?> addMasterProductModel(@RequestBody MasterProductModelRequest request){
        return new ResponseEntity<>(masterProductService.addMasterProductModel(request), HttpStatus.OK);
    }

    // http://localhost:8090/api/master-product/category/list
    @GetMapping("/category/list")
    public ResponseEntity<?> getAllMasterProductCategory(){
        return new ResponseEntity<>(masterProductService.getAllMasterProductCategory(), HttpStatus.OK);
    }

    // http://localhost:8090/api/master-product/sub-category/list
    @GetMapping("/sub-category/list")
    public ResponseEntity<?> getAllMasterProductSubCategory(){
        return new ResponseEntity<>(masterProductService.getAllMasterProductSubCategory(), HttpStatus.OK);
    }

    // http://localhost:8090/api/master-product/brand/list
    @GetMapping("/brand/list")
    public ResponseEntity<?> getAllMasterProductBrands(){
        return new ResponseEntity<>(masterProductService.getAllMasterProductBrand(), HttpStatus.OK);
    }

    // http://localhost:8090/api/master-product/model/list
    @GetMapping("/model/list")
    public ResponseEntity<?> getAllMasterProductModels(){
        return new ResponseEntity<>(masterProductService.getAllMasterProductModels(), HttpStatus.OK);
    }
}
