package com.cafe_mn_system.coffeehut_backend.Services.impl;

import com.cafe_mn_system.coffeehut_backend.Dto.ProductDto;
import com.cafe_mn_system.coffeehut_backend.Jwt.JwtFilter;
import com.cafe_mn_system.coffeehut_backend.Models.Category;
import com.cafe_mn_system.coffeehut_backend.Models.Product;
import com.cafe_mn_system.coffeehut_backend.Repo.ProductRepo;
import com.cafe_mn_system.coffeehut_backend.Services.ProductService;
import com.cafe_mn_system.coffeehut_backend.Utils.CoffeeHutConstants;
import com.cafe_mn_system.coffeehut_backend.Utils.CoffeeHutUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try{
            if (jwtFilter.isAdmin()){

                if (validateProductMap(requestMap,false)){
                    productRepo.save(getProductFromMap(requestMap,false));
                    return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.PRODUCT_SAVE_SUCCESSFULLY, HttpStatus.OK);

                }else{
                    return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.INVALID_DATA_PRODUCT, HttpStatus.BAD_REQUEST);
                }

            }else{
                return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.ACCESS_DENIED, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        try{
            List<ProductDto> productList = productRepo.getAllProducts();
            return CoffeeHutUtils.getResponseEntityForProductList(CoffeeHutConstants.FETCH_DATA_SUCCESSFULLY, productList, HttpStatus.OK);

        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CoffeeHutUtils.getResponseEntityForList(CoffeeHutConstants.SOMETHING_WENT_WRONG, new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try{
            if (jwtFilter.isAdmin()){

                if (validateProductMap(requestMap,true)){

                    Optional<Product> product = productRepo.findById(Integer.parseInt(requestMap.get("id")));

                    if (!product.isEmpty()){
                        Product updatedProduct = getProductFromMap(requestMap,true);
                        updatedProduct.setStatus(product.get().getStatus());
                        productRepo.save(updatedProduct);
                        return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.PRODUCT_UPDATED_SUCCESSFULLY, HttpStatus.OK);
                    }else{
                        return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.PRODUCT_NOT_FOUND, HttpStatus.BAD_REQUEST);
                    }
                }else{
                    return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.INVALID_DATA_PRODUCT, HttpStatus.BAD_REQUEST);
                }

            }else{
                return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.ACCESS_DENIED, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));

        Product product = new Product();

        if (isAdd){
            product.setId(Integer.parseInt(requestMap.get("id")));
        }else{
            product.setStatus("true");
        }

        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setDescription(requestMap.get("description"));
        product.setPrice(Double.parseDouble(requestMap.get("price")));

        return product;
    }

    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
        System.out.println(requestMap);
        if ((requestMap.containsKey("name") && requestMap.containsKey("description") && requestMap.containsKey("price"))){
            if (requestMap.containsKey("id") && validateId){
                return true;
            }else if(!validateId){
                return  true;
            }else{
                return false;
            }
        }
        return false;
    }
}
