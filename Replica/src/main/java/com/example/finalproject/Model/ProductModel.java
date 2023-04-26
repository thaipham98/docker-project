package com.example.finalproject.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductModel extends JpaRepository<Product, Integer> {

}
