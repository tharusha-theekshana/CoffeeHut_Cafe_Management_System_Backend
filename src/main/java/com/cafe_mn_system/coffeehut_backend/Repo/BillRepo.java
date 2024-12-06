package com.cafe_mn_system.coffeehut_backend.Repo;

import com.cafe_mn_system.coffeehut_backend.Models.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillRepo extends JpaRepository<Bill,Integer> {

    @Query(value = "SELECT b from Bill b order by b.id desc")
    List<Bill> getAllBills();

    @Query(value = "SELECT b from Bill b where b.createdBy = :currentuser order by b.id desc")
    List<Bill> getBillByUserName(@Param("currentuser") String currentUser);
}
