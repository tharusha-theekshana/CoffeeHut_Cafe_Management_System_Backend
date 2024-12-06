package com.cafe_mn_system.coffeehut_backend.Services;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface BillService {
    ResponseEntity<String> generateReport(Map<String, String> requestMap);
    ResponseEntity<Map<String, Object>> getAllBills();
}
