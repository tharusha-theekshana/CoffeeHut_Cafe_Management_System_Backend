package com.cafe_mn_system.coffeehut_backend.Repo;

import com.cafe_mn_system.coffeehut_backend.Dto.ProductDto;
import com.cafe_mn_system.coffeehut_backend.Models.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {

    @Query("SELECT new com.cafe_mn_system.coffeehut_backend.Dto.ProductDto(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name) " +
            "FROM Product p")
    List<ProductDto> getAllProducts();

    @Query("UPDATE Product p set p.status = :status where p.id = :id")
    @Transactional
    @Modifying
    void updateProductStatus(@Param("status") String status,@Param("id") int id);

    @Query("SELECT new com.cafe_mn_system.coffeehut_backend.Dto.ProductDto(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name) " +
            "FROM Product p where p.category.id = :id and p.status ='true'")
    List<ProductDto> getProductByCategoryId(@Param("id") Integer id);

    @Query("SELECT new com.cafe_mn_system.coffeehut_backend.Dto.ProductDto(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name) " +
            "FROM Product p where p.id = :id")
    ProductDto getProductById(@Param("id") Integer id);

}
