package com.cafe_mn_system.coffeehut_backend.Services.impl;

import com.cafe_mn_system.coffeehut_backend.Models.User;
import com.cafe_mn_system.coffeehut_backend.Repo.UserRepo;
import com.cafe_mn_system.coffeehut_backend.Services.UserService;
import com.cafe_mn_system.coffeehut_backend.Utils.CoffeeHutConstants;
import com.cafe_mn_system.coffeehut_backend.Utils.CoffeeHutUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signUp {] ", requestMap);

        try {
            if (validateSignUpRequestMap(requestMap)) {
                User user = userRepo.findByEmail(requestMap.get("email"));

                if (Objects.isNull(user)) {

                    User createdUser = getUserFromRequestMap(requestMap);
                    userRepo.save(createdUser);
                    return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.USER_REGISTER_SUCCESSFULLY, HttpStatus.CREATED);

                } else {
                    return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.EMAIL_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
                }
            } else {
                return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Validate request map for SignUp
    private boolean validateSignUpRequestMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber") && requestMap.containsKey("email") && requestMap.containsKey("password")) {
            if (!requestMap.get("name").isEmpty() && !requestMap.get("contactNumber").isEmpty() && !requestMap.get("email").isEmpty() && !requestMap.get("password").isEmpty()) {
                return true;
            }
            return false;
        }
        return false;
    }

    // Map to new User
    private User getUserFromRequestMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");

        return user;
    }
}
