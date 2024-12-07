package com.cafe_mn_system.coffeehut_backend.Utils;

import com.cafe_mn_system.coffeehut_backend.Dto.ProductDto;
import com.cafe_mn_system.coffeehut_backend.Dto.UserDto;
import com.cafe_mn_system.coffeehut_backend.Models.Bill;
import com.cafe_mn_system.coffeehut_backend.Models.Category;
import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Slf4j
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

    public static ResponseEntity<Map<String, Object>> getResponseEntityForUserList(String message, List<UserDto> userList, HttpStatus httpStatus) {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("Status", httpStatus.value());
        responseBody.put("Message", message);
        responseBody.put("Data", userList);

        return new ResponseEntity<>(responseBody, httpStatus);
    }

    public static ResponseEntity<Map<String, Object>> getResponseEntityForCategoryList(String message, List<Category> categoryList, HttpStatus httpStatus) {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("Status", httpStatus.value());
        responseBody.put("Message", message);
        responseBody.put("Data", categoryList);

        return new ResponseEntity<>(responseBody, httpStatus);
    }

    public static ResponseEntity<Map<String, Object>> getResponseEntityForProductList(String message, List<ProductDto> productList, HttpStatus httpStatus) {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("Status", httpStatus.value());
        responseBody.put("Message", message);
        responseBody.put("Data", productList);

        return new ResponseEntity<>(responseBody, httpStatus);
    }

    public static ResponseEntity<Map<String, Object>> getResponseEntityForProduct(String message, ProductDto product, HttpStatus httpStatus) {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("Status", httpStatus.value());
        responseBody.put("Message", message);
        responseBody.put("Data", product);

        return new ResponseEntity<>(responseBody, httpStatus);
    }

    public static ResponseEntity<Map<String, Object>> getResponseEntityForBillList(String message, List<Bill> billList, HttpStatus httpStatus) {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("Status", httpStatus.value());
        responseBody.put("Message", message);
        responseBody.put("Data", billList);

        return new ResponseEntity<>(responseBody, httpStatus);
    }

    public static String getUuid() {
        Date date = new Date();
        Long time = date.getTime();
        return "BILL-" + time;
    }

    // Convert string to json array
    public static JSONArray getJsonArray(String data) throws JSONException {
        JSONArray jsonArray = new JSONArray(data);
        return jsonArray;
    }

    public static Map<String, Object> getMapFromJson(String data) {
        if (!Strings.isNullOrEmpty(data)) {
            return new Gson().fromJson(data, new TypeToken<Map<String, Object>>() {
            }.getType());
        } else {
            return new HashMap<>();
        }
    }

    public static Boolean isFileExist(String path){
        log.info("Inside isFileExist {}",path);
        try{
            File file = new File(path);
            return (file != null && file.exists()) ? Boolean.TRUE : Boolean.FALSE;

        }catch (Exception exception){
            exception.printStackTrace();
        }
        return false;
    }

}