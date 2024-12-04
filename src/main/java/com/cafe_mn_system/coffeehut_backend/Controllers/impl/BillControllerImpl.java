package com.cafe_mn_system.coffeehut_backend.Controllers.impl;

import com.cafe_mn_system.coffeehut_backend.Controllers.BillController;
import com.cafe_mn_system.coffeehut_backend.Services.BillService;
import com.cafe_mn_system.coffeehut_backend.Utils.CoffeeHutConstants;
import com.cafe_mn_system.coffeehut_backend.Utils.CoffeeHutUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class BillControllerImpl implements BillController {

    @Autowired
    private BillService billService;

    @Override
    public ResponseEntity<String> generateReport(Map<String, String> requestMap) {
        try{
            return billService.generateReport(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
