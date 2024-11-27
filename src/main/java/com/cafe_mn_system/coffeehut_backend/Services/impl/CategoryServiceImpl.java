package com.cafe_mn_system.coffeehut_backend.Services.impl;

import com.cafe_mn_system.coffeehut_backend.Jwt.JwtFilter;
import com.cafe_mn_system.coffeehut_backend.Models.Category;
import com.cafe_mn_system.coffeehut_backend.Repo.CategoryRepo;
import com.cafe_mn_system.coffeehut_backend.Services.CategoryService;
import com.cafe_mn_system.coffeehut_backend.Utils.CoffeeHutConstants;
import com.cafe_mn_system.coffeehut_backend.Utils.CoffeeHutUtils;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Category category = categoryRepo.getCategoryByName(requestMap.get("name").toLowerCase());

                if (category != null) {
                    return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.CATEGORY_ALREADY_EXISTS, HttpStatus.CREATED);
                } else {
                    if (validateCategoryMap(requestMap, false)) {
                        categoryRepo.save(getCategoryFromMap(requestMap, false));
                        return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.CATEGORY_SAVE_SUCCESSFULLY, HttpStatus.CREATED);
                    } else {
                        return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.CATEGORY_NAME_REQUIRED, HttpStatus.BAD_REQUEST);
                    }
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
    public ResponseEntity<Map<String, Object>> getAllCategories(String filterValue) {
        try {

            if (!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")){
                List<Category> categoryList = categoryRepo.getAllCategory();
                return CoffeeHutUtils.getResponseEntityForCategoryList(CoffeeHutConstants.FETCH_DATA_SUCCESSFULLY, categoryList, HttpStatus.OK);
            }else{
                List<Category> categoryList = categoryRepo.findAll();
                return CoffeeHutUtils.getResponseEntityForCategoryList(CoffeeHutConstants.FETCH_DATA_SUCCESSFULLY, categoryList, HttpStatus.OK);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CoffeeHutUtils.getResponseEntityForList(CoffeeHutConstants.SOMETHING_WENT_WRONG, new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private boolean checkCategoryAlreadyExist(Map<String, String> requestMap) {
        Category category = categoryRepo.getCategoryByName(requestMap.get("name"));

        if (Objects.isNull(category)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private Category getCategoryFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category = new Category();

        if (isAdd) {
            category.setId(Integer.parseInt(requestMap.get("id")));
        }

        category.setName(requestMap.get("name").toLowerCase());
        return category;
    }
}
