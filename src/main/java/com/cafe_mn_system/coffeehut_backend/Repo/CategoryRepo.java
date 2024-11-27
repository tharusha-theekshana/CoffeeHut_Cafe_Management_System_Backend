package com.cafe_mn_system.coffeehut_backend.Repo;

import com.cafe_mn_system.coffeehut_backend.Models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Integer> {

    @Query(nativeQuery = true,value = "SELECT * FROM category")
    List<Category> getAllCategory();

    @Query(nativeQuery = true,value = "SELECT * FROM category WHERE name =:categoryName")
    Category getCategoryByName(@Param("categoryName") String categoryName);


}
