package com.example.finalproject.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

/**
 * OrderHasProduct is used to represent the order_has_product table in the database.
 */
@Entity
@IdClass(OrderHasProductId.class)
public class OrderHasProduct {

    @Id
    private int oid;
    @Id
    private int pid;
    private int product_count;

    public OrderHasProduct() {}

    public OrderHasProduct(int oid, int pid, int product_count) {
        this.oid = oid;
        this.pid = pid;
        this.product_count = product_count;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getProduct_count() {
        return product_count;
    }

    public void setProduct_count(int product_count) {
        this.product_count = product_count;
    }

    @Override
    public String toString() {
        return "OrderHasProduct {" +
                "oid=" + oid +
                ", pid=" + pid +
                ", product_count=" + product_count +
                '}';
    }

}
