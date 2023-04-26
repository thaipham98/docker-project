package com.example.finalproject.Model;

import java.io.Serializable;

public class OrderHasProductId implements Serializable {
    private Integer oid;
    private Integer pid;

    public OrderHasProductId() {}

    public OrderHasProductId(Integer oid, Integer pid) {
        this.oid = oid;
        this.pid = pid;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "OrderHasProductId{" +
                "oid=" + oid +
                ", pid=" + pid +
                '}';
    }
}
