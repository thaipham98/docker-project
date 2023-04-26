package com.example.finalproject.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreOrderModel extends JpaRepository<StoreOrder, Integer> {
}
