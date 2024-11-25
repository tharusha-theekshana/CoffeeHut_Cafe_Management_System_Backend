package com.cafe_mn_system.coffeehut_backend.Utils;

import com.cafe_mn_system.coffeehut_backend.Dto.UserDto;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static ResponseEntity<Map<String, Object>> getResponseEntityForList(String message, List<UserDto> userList, HttpStatus httpStatus) {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("Status", httpStatus.value());
        responseBody.put("Message", message);
        responseBody.put("Data", userList);

        return new ResponseEntity<>(responseBody, httpStatus);
    }
}
