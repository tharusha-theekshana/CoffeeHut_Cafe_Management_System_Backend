package com.cafe_mn_system.coffeehut_backend.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping(path = "api/v1/category")
public interface CategoryController {

    @PostMapping()
    ResponseEntity<String> addNewCategory(@RequestBody(required = true) Map<String,String> requestMap);
}
