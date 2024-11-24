package com.cafe_mn_system.coffeehut_backend.Repo;

import com.cafe_mn_system.coffeehut_backend.Dto.UserDto;
import com.cafe_mn_system.coffeehut_backend.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

    @Query(nativeQuery = true,value = "SELECT * FROM user WHERE email = :email")
    User findByEmail(@Param("email") String email);

    @Query("SELECT new com.cafe_mn_system.coffeehut_backend.Dto.UserDto(u.id, u.name, u.email, u.contactNumber, u.status) " +
            "FROM User u WHERE u.role = 'user'")
    List<UserDto> getAllUsers();

}
