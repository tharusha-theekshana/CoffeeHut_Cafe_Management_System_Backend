package com.cafe_mn_system.coffeehut_backend.Services;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ProductService {
    ResponseEntity<String> addNewProduct(Map<String, String> requestMap);
    ResponseEntity<Map<String, Object>> getAllProducts();
    ResponseEntity<String> updateProduct(Map<String, String> requestMap);
    ResponseEntity<String> deleteProduct(Integer id);
    ResponseEntity<String> updateStatus(Map<String, String> requestMap);
    ResponseEntity<Map<String, Object>> gerProductsByCategoryId(Integer id);
    ResponseEntity<Map<String, Object>> getProductById(Integer id);
}
