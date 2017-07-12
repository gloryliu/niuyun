package com.niuyun.hire.ui.bean;

import java.io.Serializable;

/**
 * Created by chen.zhiwei on 2017-7-5.
 */

public class OrderDetailBean implements Serializable{

    /**
     * ordername : hhhhh
     * orderphone : 13214151415
     * orderaddress : yggvvbbh
     * count : 1
     * goodsprice : 1000
     * payamount : null
     * goodsName : null
     * goodsImg : null
     * orderpayno : null
     * createtime : null
     * paytime : null
     */

    private String ordername;
    private String orderphone;
    private String orderaddress;
    private int count;
    private int goodsprice;
    private int payamount;
    private String goodsName;
    private String goodsImg;
    private String orderpayno;
    private String createtime;
    private String paytime;

    public String getOrdername() {
        return ordername;
    }

    public void setOrdername(String ordername) {
        this.ordername = ordername;
    }

    public String getOrderphone() {
        return orderphone;
    }

    public void setOrderphone(String orderphone) {
        this.orderphone = orderphone;
    }

    public String getOrderaddress() {
        return orderaddress;
    }

    public void setOrderaddress(String orderaddress) {
        this.orderaddress = orderaddress;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getGoodsprice() {
        return goodsprice;
    }

    public void setGoodsprice(int goodsprice) {
        this.goodsprice = goodsprice;
    }

    public int getPayamount() {
        return payamount;
    }

    public void setPayamount(int payamount) {
        this.payamount = payamount;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getOrderpayno() {
        return orderpayno;
    }

    public void setOrderpayno(String orderpayno) {
        this.orderpayno = orderpayno;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }
}
