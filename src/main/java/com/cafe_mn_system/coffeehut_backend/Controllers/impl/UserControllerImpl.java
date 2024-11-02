package com.cafe_mn_system.coffeehut_backend.Controllers.impl;

import com.cafe_mn_system.coffeehut_backend.Controllers.UserController;
import com.cafe_mn_system.coffeehut_backend.Services.UserService;
import com.cafe_mn_system.coffeehut_backend.Utils.CoffeeHutConstants;
import com.cafe_mn_system.coffeehut_backend.Utils.CoffeeHutUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserControllerImpl implements UserController {

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try{
            return userService.signUp(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
