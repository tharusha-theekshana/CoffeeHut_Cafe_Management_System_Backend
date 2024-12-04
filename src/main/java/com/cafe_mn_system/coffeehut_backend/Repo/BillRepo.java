package com.cafe_mn_system.coffeehut_backend.Repo;

import com.cafe_mn_system.coffeehut_backend.Models.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepo extends JpaRepository<Bill,Integer> {
}
