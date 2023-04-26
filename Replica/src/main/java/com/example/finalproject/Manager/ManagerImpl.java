package com.example.finalproject.Manager;

import com.example.finalproject.Model.*;
import jakarta.persistence.criteria.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ManagerImpl implements Manager {

    @Autowired
    private ProductModel productModel;

    @Autowired
    private StoreOrderModel storeOrderModel;

    @Autowired
    private OrderHasProductModel orderHasProductModel;

    @Override
    public List<Product> viewAllProducts() {
        return productModel.findAll();
    }

    @Override
    public Product findProductById(int productId) {
        return productModel.findById(productId).orElse(null);
    }

    @Override
    @Transactional
    public List<OrderHasProduct> addToOrder(int productId) {
        // Deal with Order table
        StoreOrder currentStoreOrder = null;
        try {
            currentStoreOrder = storeOrderModel.findAll().stream().filter(storeOrder -> !storeOrder.isIs_completed()).findFirst().orElse(null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (currentStoreOrder == null) {
            currentStoreOrder = new StoreOrder();
            currentStoreOrder.setIs_completed(false);
            currentStoreOrder.setTotal_price(0);
            storeOrderModel.save(currentStoreOrder);
        }
        Product addedProduct = productModel.findById(productId).orElse(null);
        if (addedProduct != null) {
            int price = addedProduct.getPrice();
            currentStoreOrder.setTotal_price(currentStoreOrder.getTotal_price() + price);
        }
        storeOrderModel.save(currentStoreOrder);

        // Deal with OrderHasProduct table
        int oid = currentStoreOrder.getOid();
        OrderHasProduct orderHasProduct = null;
        try {
            orderHasProduct = orderHasProductModel.findAll().stream().filter(ohp -> ohp.getOid() == oid && ohp.getPid() == productId).findFirst().orElse(null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (addedProduct != null) {
            if (orderHasProduct == null && addedProduct != null) {
                orderHasProduct = new OrderHasProduct();
                orderHasProduct.setOid(oid);
                orderHasProduct.setPid(productId);
                orderHasProduct.setProduct_count(1);
            } else {
                orderHasProduct.setProduct_count(orderHasProduct.getProduct_count() + 1);
            }
            orderHasProductModel.save(orderHasProduct);
        }

        return orderHasProductModel.findAll().stream().filter(ohp -> ohp.getOid() == oid).toList();
    }

    @Override
    public List<OrderHasProduct> showOrder() {
        StoreOrder currentStoreOrder = storeOrderModel.findAll().stream().filter(storeOrder -> !storeOrder.isIs_completed()).findFirst().orElse(null);
        if (currentStoreOrder != null) {
            int oid = currentStoreOrder.getOid();
            return orderHasProductModel.findAll().stream().filter(ohp -> ohp.getOid() == oid).toList();
        }
        return null;
    }

    @Override
    @Transactional
    public List<OrderHasProduct> deleteFromOrder(int productId) {
        // Deal with Order table
        StoreOrder currentStoreOrder = storeOrderModel.findAll().stream().filter(storeOrder -> !storeOrder.isIs_completed()).findFirst().orElse(null);
        if (currentStoreOrder != null) {
            Product removedProduct = productModel.findById(productId).orElse(null);
            if (removedProduct != null) {
                int price = removedProduct.getPrice();
                currentStoreOrder.setTotal_price(currentStoreOrder.getTotal_price() - price);
            }
            storeOrderModel.save(currentStoreOrder);

            // Deal with OrderHasProduct table
            int oid = currentStoreOrder.getOid();
            OrderHasProduct orderHasProduct = orderHasProductModel.findAll().stream().filter(ohp -> ohp.getOid() == oid && ohp.getPid() == productId).findFirst().orElse(null);
            if (orderHasProduct != null) {
                if (orderHasProduct.getProduct_count() == 1) {
                    orderHasProductModel.delete(orderHasProduct);
                } else {
                    orderHasProduct.setProduct_count(orderHasProduct.getProduct_count() - 1);
                    orderHasProductModel.save(orderHasProduct);
                }
            }
        }
        int oid = currentStoreOrder.getOid();
        return orderHasProductModel.findAll().stream().filter(ohp -> ohp.getOid() == oid).toList();
    }

    @Override
    @Transactional
    // Remove button
    public List<OrderHasProduct> removeFromOrder(int productId, int quantity) {
        // Deal with Order table
        StoreOrder currentStoreOrder = storeOrderModel.findAll().stream().filter(storeOrder -> !storeOrder.isIs_completed()).findFirst().orElse(null);
        if (currentStoreOrder != null) {
            Product editProduct = productModel.findById(productId).orElse(null);
            if (editProduct != null) {
                int price = editProduct.getPrice();
                currentStoreOrder.setTotal_price(currentStoreOrder.getTotal_price() - price * quantity);
                if (currentStoreOrder.getTotal_price() == 0){
                    storeOrderModel.delete(currentStoreOrder);
                } else {
                    storeOrderModel.save(currentStoreOrder);
                }
            }

            // Deal with OrderHasProduct table
            int oid = currentStoreOrder.getOid();
            OrderHasProduct orderHasProduct = orderHasProductModel.findAll().stream().filter(ohp -> ohp.getOid() == oid && ohp.getPid() == productId).findFirst().orElse(null);
            if (orderHasProduct != null) {
                orderHasProduct.setProduct_count(orderHasProduct.getProduct_count() - quantity);
                if (orderHasProduct.getProduct_count() == 0) {
                    orderHasProductModel.delete(orderHasProduct);
                } else {
                    orderHasProductModel.save(orderHasProduct);
                }
            }
        }
        currentStoreOrder = storeOrderModel.findAll().stream().filter(storeOrder -> !storeOrder.isIs_completed()).findFirst().orElse(null);
        if (currentStoreOrder != null) {
            int oid = currentStoreOrder.getOid();
            return orderHasProductModel.findAll().stream().filter(ohp -> ohp.getOid() == oid).toList();
        }
        return null;
    }

    @Override
    @Transactional
    public void checkout() {
        StoreOrder currentStoreOrder = storeOrderModel.findAll().stream().filter(storeOrder -> !storeOrder.isIs_completed()).findFirst().orElse(null);
        if (currentStoreOrder != null) {
            currentStoreOrder.setIs_completed(true);
            storeOrderModel.save(currentStoreOrder);
        }
    }

    @Override
    @Transactional
    public void clearOrder() {
        StoreOrder currentStoreOrder = storeOrderModel.findAll().stream().filter(storeOrder -> !storeOrder.isIs_completed()).findFirst().orElse(null);
        if (currentStoreOrder != null) {
            List<OrderHasProduct> orderHasProducts = orderHasProductModel.findAll();
            for (OrderHasProduct orderHasProduct : orderHasProducts) {
                if (orderHasProduct.getOid() == currentStoreOrder.getOid()) {
                    orderHasProductModel.delete(orderHasProduct);
                }
            }
            storeOrderModel.delete(currentStoreOrder);
        }
    }


}

