package com.cafe_mn_system.coffeehut_backend.Repo;

import com.cafe_mn_system.coffeehut_backend.Dto.ProductDto;
import com.cafe_mn_system.coffeehut_backend.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {

    @Query("SELECT new com.cafe_mn_system.coffeehut_backend.Dto.ProductDto(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name) " +
            "FROM Product p")
    List<ProductDto> getAllProducts();
}
