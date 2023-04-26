package com.example.finalproject.Model;

import jakarta.persistence.*;

@Entity
public class StoreOrder {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer oid;
        private boolean is_completed;
        private double total_price;

        public StoreOrder() {}

        public StoreOrder(Integer oid, boolean is_completed, double total_price) {
            this.oid = oid;
            this.is_completed = is_completed;
            this.total_price = total_price;
        }

        public Integer getOid() {
            return oid;
        }

        public void setOid(Integer oid) {
            this.oid = oid;
        }

        public boolean isIs_completed() {
            return is_completed;
        }

        public void setIs_completed(boolean is_completed) {
            this.is_completed = is_completed;
        }

        public double getTotal_price() {
            return total_price;
        }

        public void setTotal_price(double total_price) {
            this.total_price = total_price;
        }

        @Override
        public String toString() {
            return "Order {" +
                    "oid=" + oid +
                    ", is_completed=" + is_completed +
                    ", total_price=" + total_price +
                    '}';
        }
}
