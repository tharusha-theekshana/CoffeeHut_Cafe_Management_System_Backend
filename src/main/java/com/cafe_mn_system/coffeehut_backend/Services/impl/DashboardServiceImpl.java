package com.cafe_mn_system.coffeehut_backend.Services.impl;

import com.cafe_mn_system.coffeehut_backend.Repo.BillRepo;
import com.cafe_mn_system.coffeehut_backend.Repo.CategoryRepo;
import com.cafe_mn_system.coffeehut_backend.Repo.ProductRepo;
import com.cafe_mn_system.coffeehut_backend.Services.DashboardService;
import com.cafe_mn_system.coffeehut_backend.Utils.CoffeeHutConstants;
import com.cafe_mn_system.coffeehut_backend.Utils.CoffeeHutUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private BillRepo billRepo;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String,Object> map = new HashMap<>();
        map.put("Category",categoryRepo.count());
        map.put("Product",productRepo.count());
        map.put("Bill",billRepo.count());

        return CoffeeHutUtils.getResponseEntityForDashboard(CoffeeHutConstants.FETCH_DATA_SUCCESSFULLY, map, HttpStatus.OK);
    }
}
