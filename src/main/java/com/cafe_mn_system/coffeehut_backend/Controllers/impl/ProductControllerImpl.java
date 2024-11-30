package com.cafe_mn_system.coffeehut_backend.Controllers.impl;

import com.cafe_mn_system.coffeehut_backend.Controllers.ProductController;
import com.cafe_mn_system.coffeehut_backend.Services.ProductService;
import com.cafe_mn_system.coffeehut_backend.Utils.CoffeeHutConstants;
import com.cafe_mn_system.coffeehut_backend.Utils.CoffeeHutUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

@RestController
public class ProductControllerImpl implements ProductController {

    @Autowired
    private ProductService productService;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try{
            return productService.addNewProduct(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        try{
            return productService.getAllProducts();
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CoffeeHutUtils.getResponseEntityForList(CoffeeHutConstants.MESSAGE,  new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try{
            return productService.updateProduct(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try{
            return productService.deleteProduct(id);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProductStatus(Map<String, String> requestMap) {
        try{
            return productService.updateStatus(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Map<String, Object>> getByCategory(Integer id) {
        try{
            return productService.gerProductsByCategoryId(id);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CoffeeHutUtils.getResponseEntityForList(CoffeeHutConstants.MESSAGE,  new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Map<String, Object>> getProductById(Integer id) {
        try{
            return productService.getProductById(id);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CoffeeHutUtils.getResponseEntityForList(CoffeeHutConstants.MESSAGE,  new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
