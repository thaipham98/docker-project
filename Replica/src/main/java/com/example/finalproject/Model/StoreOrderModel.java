package com.example.finalproject.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * StoreOrderModel is used to access the StoreOrder table in the database.
 */
@Repository
public interface StoreOrderModel extends JpaRepository<StoreOrder, Integer> {
}
