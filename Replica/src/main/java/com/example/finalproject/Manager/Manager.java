package com.example.finalproject.Manager;


import com.example.finalproject.Model.OrderHasProduct;
import com.example.finalproject.Model.Product;

import java.util.List;

/**
 * Manager interface. This interface is used to define the methods that will be implemented in ManagerImpl.
 * This interface is used by the controller to handle business logic after receiving a request from the client.
 */
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
