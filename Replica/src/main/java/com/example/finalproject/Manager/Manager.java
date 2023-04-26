package com.example.finalproject.Manager;


import com.example.finalproject.Model.OrderHasProduct;
import com.example.finalproject.Model.Product;

import java.util.List;

public interface Manager {
    List<Product> viewAllProducts();

    Product findProductById(int productId);

    List<OrderHasProduct> addToOrder(int productId);

    List<OrderHasProduct> showOrder();

    List<OrderHasProduct> deleteFromOrder(int productId);

    List<OrderHasProduct> removeFromOrder(int productId, int quantity);

    void checkout();

    void clearOrder();

}
