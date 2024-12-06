package com.cafe_mn_system.coffeehut_backend.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping(path = "api/v1/bill")
public interface BillController {

    @PostMapping(path = "/generateReport")
    ResponseEntity<String> generateReport(@RequestBody(required = true) Map<String,String> requestMap);

    @GetMapping()
    ResponseEntity<Map<String, Object>> getAllBills();

    @PostMapping("/getPdf")
    ResponseEntity<byte[]> getPdf(@RequestBody(required = true) Map<String,String> requestMap);

}
