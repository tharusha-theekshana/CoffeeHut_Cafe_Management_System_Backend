package com.cafe_mn_system.coffeehut_backend.Utils;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor
public class CoffeeHutUtils {

    public static ResponseEntity<String> getResponseEntity(String messageTitle, String message, HttpStatus httpStatus) {
        String responseBody = String.format(
                "{\n" +
                        "    \"Status\": %d,\n" +
                        "    \"Reason\": \"%s\",\n" +
                        "    \"%s\": \"%s\"\n" +
                        "}",
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                messageTitle,
                message
        );
        return new ResponseEntity<>(responseBody, httpStatus);
    }

}
