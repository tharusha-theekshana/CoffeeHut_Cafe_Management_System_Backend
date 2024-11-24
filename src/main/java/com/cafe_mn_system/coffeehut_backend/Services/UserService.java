package com.cafe_mn_system.coffeehut_backend.Services;

import com.cafe_mn_system.coffeehut_backend.Dto.UserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {
    ResponseEntity<String> signUp(Map<String, String> requestMap);
    ResponseEntity<String> login(Map<String, String> requestMap);
    ResponseEntity<List<UserDto>> getAllUsers();
}
