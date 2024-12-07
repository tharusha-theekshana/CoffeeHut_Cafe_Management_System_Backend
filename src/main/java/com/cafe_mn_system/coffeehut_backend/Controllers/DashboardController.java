package com.cafe_mn_system.coffeehut_backend.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("api/v1/dashboard")
public interface DashboardController {

    @GetMapping
    ResponseEntity<Map<String,Object>> getCountDetails();

}
