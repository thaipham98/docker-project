package com.example.finalproject.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Product {

//    CREATE TABLE product (

//    pid varchar(100) NOT NULL PRIMARY KEY,
//    product_name varchar(200) NOT NULL,
//    price int NOT NULL,
//    is_on_sale boolean NOT NULL,
//    description varchar(200) NOT NULL
//);

    @Id
    private int pid;
    private String product_name;
    private int price;
    private String description;

    public Product() {}

    public Product(int pid, String product_name, int price, String description) {
        this.pid = pid;
        this.product_name = product_name;
        this.price = price;
        this.description = description;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Product {" +
                "pid=" + pid +
                ", product_name='" + product_name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }



}
