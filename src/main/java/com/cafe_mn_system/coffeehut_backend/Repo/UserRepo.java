package com.cafe_mn_system.coffeehut_backend.Repo;

import com.cafe_mn_system.coffeehut_backend.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

    @Query(nativeQuery = true,value = "SELECT * FROM user WHERE email = :email")
    User findByEmail(@Param("email") String email);

}
