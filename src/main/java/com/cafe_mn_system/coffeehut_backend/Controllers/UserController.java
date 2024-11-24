package com.cafe_mn_system.coffeehut_backend.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping(path = "api/v1/user")
public interface UserController {

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String,String> requestMap);

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody(required = true) Map<String,String> requestMap);

    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllUsers();

    @PutMapping()
    public ResponseEntity<String> updateUser(@RequestBody(required = true) Map<String,String> requestMap);

}
