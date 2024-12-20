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
import com.cafe_mn_system.coffeehut_backend.Utils.EmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    private EmailUtil emailUtil;

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
            } else {
                return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.BAD_CREDENTIALS, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Get All Users
    @Override
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        log.info("Inside get all users");

        try {

            if (jwtFilter.isAdmin()) {

                List<UserDto> userList = userRepo.getAllUsers();
                return CoffeeHutUtils.getResponseEntityForUserList(CoffeeHutConstants.FETCH_DATA_SUCCESSFULLY, userList, HttpStatus.OK);

            } else {
                return CoffeeHutUtils.getResponseEntityForUserList(CoffeeHutConstants.ACCESS_DENIED, new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CoffeeHutUtils.getResponseEntityForUserList(CoffeeHutConstants.SOMETHING_WENT_WRONG, new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateUser(Map<String, String> requestMap) {
        log.info("Inside update user {}", requestMap);

        try {

            if (jwtFilter.isAdmin()) {

                Optional<User> user = userRepo.findById(Integer.parseInt(requestMap.get("id")));

                if (!user.isEmpty()) {
                    userRepo.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    sendMailToAllAdmins(requestMap.get("status"), user.get().getEmail(), userRepo.getAllAdmins());
                    return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.USER_UPDATED_SUCCESSFULLY, HttpStatus.OK);

                } else {
                    return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
                }


            } else {
                return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.ACCESS_DENIED, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.TOKEN_VALIDATE, HttpStatus.OK);
    }

    // Change password  method
    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        log.info("Inside change password {}", requestMap);

        try {
           User user = userRepo.findByEmail(jwtFilter.getCurrentUser());

           if (!user.equals(null)){
               if (user.getPassword().equals(requestMap.get("oldPassword"))){

                   if (requestMap.get("newPassword").isEmpty()){
                       return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.INCORRECT_NEW_PASSWORD, HttpStatus.BAD_REQUEST);
                   }else{
                       if (requestMap.get("newPassword").equalsIgnoreCase(user.getPassword())){
                           return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.ALREADY_EXIST_PASSWORD, HttpStatus.BAD_REQUEST);
                       }else{
                           user.setPassword(requestMap.get("newPassword"));
                           userRepo.save(user);
                           return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.PASSWORD_UPDATED_SUCCESSFULLY, HttpStatus.OK);
                       }
                   }
               }else{
                   return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.INCORRECT_OLD_PASSWORD, HttpStatus.BAD_REQUEST);
               }

           }else{
               return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
           }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Forgot password method
    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        log.info("Inside forgot password {}", requestMap);

        try {
            if (requestMap.get("email").isEmpty()){
                return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.EMAIL_IS_REQUIRED, HttpStatus.BAD_REQUEST);
            }

            User user = userRepo.findByEmail(requestMap.get("email"));

            if (!Objects.isNull(user)){

                emailUtil.forgotMail(user.getEmail().toString(),"Credentials",user.getPassword());
                return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.MAIL_SENT_SUCCESSFULLY, HttpStatus.OK);

            }else{
                return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Send mails to all admin mails
    private void sendMailToAllAdmins(String status, String user, List<String> allAdmins) {

        // Remove login admin user account form admin list
        allAdmins.remove(jwtFilter.getCurrentUser());

        // Mail send for account activate and disable
        if (status != null && status.equalsIgnoreCase("true")) {
            emailUtil.sendSimpleMessage(jwtFilter.getCurrentUser(), CoffeeHutConstants.ACCOUNT_APPROVED, "User Account - " + user + "\n is approved by \n Admin - " + jwtFilter.getCurrentUser(), allAdmins);
        }else{
            emailUtil.sendSimpleMessage(jwtFilter.getCurrentUser(), CoffeeHutConstants.ACCOUNT_APPROVED, "User Account :- " + user + "\n is disabled by \n Admin - " + jwtFilter.getCurrentUser(), allAdmins);
        }

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
