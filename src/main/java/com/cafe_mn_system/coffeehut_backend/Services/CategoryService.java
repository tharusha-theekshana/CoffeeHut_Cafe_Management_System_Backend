package com.cafe_mn_system.coffeehut_backend.Services;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface CategoryService {

    ResponseEntity<String> addNewCategory(Map<String, String> requestMap);
    ResponseEntity<Map<String, Object>> getAllCategories(String filterValue);
    ResponseEntity<String> updateCategory(Map<String, String> requestMap);
    ResponseEntity<String> deleteCategory(String id);
}
