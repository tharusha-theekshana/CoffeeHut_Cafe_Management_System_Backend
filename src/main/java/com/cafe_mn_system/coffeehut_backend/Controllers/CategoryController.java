package com.cafe_mn_system.coffeehut_backend.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping(path = "api/v1/category")
public interface CategoryController {

    @PostMapping()
    ResponseEntity<String> addNewCategory(@RequestBody(required = true) Map<String,String> requestMap);

    @GetMapping()
    ResponseEntity<Map<String, Object>> getAllCategories(@RequestParam(required = false) String filterValue);
}
