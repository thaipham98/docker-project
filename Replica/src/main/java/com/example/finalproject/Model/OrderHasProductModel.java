package com.example.finalproject.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * OrderHasProductModel is used to access the OrderHasProduct table in the database.
 */
@Repository
public interface OrderHasProductModel extends JpaRepository<OrderHasProduct, Integer> {
}
