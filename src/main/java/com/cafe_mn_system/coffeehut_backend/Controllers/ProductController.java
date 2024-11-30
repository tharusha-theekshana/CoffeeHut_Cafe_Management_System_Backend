package com.cafe_mn_system.coffeehut_backend.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping(path = "api/v1/product")
public interface ProductController {

    @PostMapping()
    ResponseEntity<String> addNewProduct(@RequestBody(required = true) Map<String,String> requestMap);

    @GetMapping()
    ResponseEntity<Map<String, Object>> getAllProducts();

    @PutMapping
    ResponseEntity<String> updateProduct(@RequestBody(required = true) Map<String,String> requestMap);


}
