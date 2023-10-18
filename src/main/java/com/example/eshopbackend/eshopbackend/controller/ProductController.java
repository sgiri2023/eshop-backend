package com.example.eshopbackend.eshopbackend.controller;

import com.example.eshopbackend.eshopbackend.datamodel.*;
import com.example.eshopbackend.eshopbackend.service.ProductService;
import com.example.eshopbackend.eshopbackend.service.impl.ProductServiceImpl;
import com.example.eshopbackend.eshopbackend.session.SessionDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.eshopbackend.eshopbackend.session.SessionStore.SESSION_TRACKER;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductServiceImpl productService;

    @Autowired
    private SessionDataManager sessionDataManager;

     // http://localhost:8090/api/product/add
     @PostMapping("/add")
    public ResponseEntity<?>addProduct(@RequestHeader String Authorization, @RequestBody ProductRequest request){
        System.out.println("Add Product request: " + request);
         SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
         if (sessionData == null) {
             return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
         }
        return new ResponseEntity<>(productService.addProduct(sessionData.getUserId(), request), HttpStatus.OK);
    }

    // http://localhost:8090/api/product/get-product-list
    @GetMapping("/get-product-list")
    public ResponseEntity<?> getProductList(){
        System.out.println("Get Product List");
        return new ResponseEntity<>(productService.getProductList(), HttpStatus.OK);
    }

}
