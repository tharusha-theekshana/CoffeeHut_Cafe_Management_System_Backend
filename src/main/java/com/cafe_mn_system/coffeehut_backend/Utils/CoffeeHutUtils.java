package com.cafe_mn_system.coffeehut_backend.Utils;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor
public class CoffeeHutUtils {

    public static ResponseEntity<String> getResponseEntity(String message, HttpStatus httpStatus){
        return new ResponseEntity<>("{\"message\" : \"" + message +"\" }",httpStatus);
    }

}
