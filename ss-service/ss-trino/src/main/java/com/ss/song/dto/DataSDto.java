package com.ss.song.dto;

import java.io.Serializable;

/**
 * author shangsong 2023/7/21
 */
public class DataSDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer orderkey;

    private Integer custkey;

    private String orderstatus;

    private Double totalprice;

    private String orderpriority;

    private String clerk;

    private Integer shippriority;

    public Integer getOrderkey() {
        return orderkey;
    }

    public void setOrderkey(Integer orderkey) {
        this.orderkey = orderkey;
    }

    public Integer getCustkey() {
        return custkey;
    }

    public void setCustkey(Integer custkey) {
        this.custkey = custkey;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public Double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(Double totalprice) {
        this.totalprice = totalprice;
    }

    public String getOrderpriority() {
        return orderpriority;
    }

    public void setOrderpriority(String orderpriority) {
        this.orderpriority = orderpriority;
    }

    public String getClerk() {
        return clerk;
    }

    public void setClerk(String clerk) {
        this.clerk = clerk;
    }

    public Integer getShippriority() {
        return shippriority;
    }

    public void setShippriority(Integer shippriority) {
        this.shippriority = shippriority;
    }
}

