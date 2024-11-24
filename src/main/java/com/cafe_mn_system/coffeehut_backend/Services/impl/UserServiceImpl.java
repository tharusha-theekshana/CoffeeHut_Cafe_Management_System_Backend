package com.cafe_mn_system.coffeehut_backend.Services.impl;

import com.cafe_mn_system.coffeehut_backend.Dto.UserDto;
import com.cafe_mn_system.coffeehut_backend.Jwt.JwtFilter;
import com.cafe_mn_system.coffeehut_backend.Jwt.JwtUtils;
import com.cafe_mn_system.coffeehut_backend.Models.User;
import com.cafe_mn_system.coffeehut_backend.Repo.UserRepo;
import com.cafe_mn_system.coffeehut_backend.Security.CustomUserDetailService;
import com.cafe_mn_system.coffeehut_backend.Services.UserService;
import com.cafe_mn_system.coffeehut_backend.Utils.CoffeeHutConstants;
import com.cafe_mn_system.coffeehut_backend.Utils.CoffeeHutUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signUp {] ", requestMap);

        try {
            if (validateSignUpRequestMap(requestMap)) {
                User user = userRepo.findByEmail(requestMap.get("email"));

                if (Objects.isNull(user)) {

                    User createdUser = getUserFromRequestMap(requestMap);
                    userRepo.save(createdUser);
                    return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.USER_REGISTER_SUCCESSFULLY, HttpStatus.CREATED);

                } else {
                    return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.EMAIL_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
                }
            } else {
                return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login {] ", requestMap);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );

            if (authentication.isAuthenticated()) {
                if (customUserDetailService.getUserDetails().getStatus().equalsIgnoreCase("true")) {
                    String token = jwtUtils.tokenGenerate(customUserDetailService.getUserDetails().getEmail(), customUserDetailService.getUserDetails().getRole());

                    return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.TOKEN, token, HttpStatus.OK);
                } else {
                    return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.WAIT_FOR_APPROVAL, HttpStatus.BAD_REQUEST);
                }
            }else{
                return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.BAD_CREDENTIALS, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Get All Users
    @Override
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("Inside get all users");

        try {

            if(jwtFilter.isAdmin()){

                List<UserDto> userList = userRepo.getAllUsers();

                return new ResponseEntity<>(userList,HttpStatus.OK);


            }else {
                return new ResponseEntity<List<UserDto>>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<List<UserDto>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
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
