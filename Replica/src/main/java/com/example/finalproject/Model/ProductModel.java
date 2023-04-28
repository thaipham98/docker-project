package com.example.finalproject.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** ProductModel is used to access the Product table in the database. */
@Repository
public interface ProductModel extends JpaRepository<Product, Integer> {

}
