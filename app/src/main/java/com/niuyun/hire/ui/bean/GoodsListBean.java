package com.niuyun.hire.ui.bean;

import com.niuyun.hire.bean.ErrorBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chen.zhiwei on 2017-6-26.
 */

public class GoodsListBean extends ErrorBean implements Serializable {

    /**
     * id : 1
     * name : 随便
     * price : 10
     * vipprice : 8
     * purchase : 10
     * goodstype : 1
     * shop : 3
     * goodsdetail : 阿斯蒂芬地方撒旦法师的法师打发
     * binners : ["//img13.360buyimg.com/n1/jfs/t2731/27/3738229696/379848/274ca0dc/579aeb6aNdc1f6ed5.jpg","//img13.360buyimg.com/n1/jfs/t2602/30/3884890450/309662/2c8cfb1d/579aeb60N78ba24cb.jpg","//img13.360buyimg.com/n1/jfs/t2911/359/2070652650/421509/3ebec13a/579aeb82N71a513b6.jpg","//img13.360buyimg.com/n1/jfs/t3016/238/302627775/261748/dfbb658f/579aeb7dN10f1d0b5.jpg"]
     * details : ["https://img10.360buyimg.com/imgzone/jfs/t3040/330/1092199623/523176/112c0a18/57bd3bcfN57bd4117.jpg","https://img10.360buyimg.com/imgzone/jfs/t3067/295/690060163/351438/96c83443/57bd3bd0N4db75a57.jpg","https://img10.360buyimg.com/imgzone/jfs/t3157/114/652856633/336899/98a40fa5/57bd3bd0N9e3675dd.jpg","https://img10.360buyimg.com/imgzone/jfs/t3187/142/706300869/352432/2bf80dc3/57bd3bd1Nc6dd803c.jpg","https://img10.360buyimg.com/imgzone/jfs/t3187/142/706300869/352432/2bf80dc3/57bd3bd1Nc6dd803c.jpg","https://img10.360buyimg.com/imgzone/jfs/t3034/187/1108666807/334427/a405a803/57bd3bd1N5bb30d36.jpg","https://img10.360buyimg.com/imgzone/jfs/t3142/85/678921523/551754/602c129/57bd3bd2N7583c634.jpg"]
     * shopName : 天安门店
     * typeName : null
     */

    private int id;
    private String name;
    private int price;
    private int vipprice;
    private int purchase;

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    private int deposit;
    private int goodstype;
    private int shop;
    private int stock;//商品库存

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getTypeName() {
        return typeName;
    }

    private String goodsdetail;
    private String shopName;
    private String typeName;
    private String goodsImg;

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    private List<String> binners;
    private List<String> details;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getVipprice() {
        return vipprice;
    }

    public void setVipprice(int vipprice) {
        this.vipprice = vipprice;
    }

    public int getPurchase() {
        return purchase;
    }

    public void setPurchase(int purchase) {
        this.purchase = purchase;
    }

    public int getGoodstype() {
        return goodstype;
    }

    public void setGoodstype(int goodstype) {
        this.goodstype = goodstype;
    }

    public int getShop() {
        return shop;
    }

    public void setShop(int shop) {
        this.shop = shop;
    }

    public String getGoodsdetail() {
        return goodsdetail;
    }

    public void setGoodsdetail(String goodsdetail) {
        this.goodsdetail = goodsdetail;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<String> getBinners() {
        return binners;
    }

    public void setBinners(List<String> binners) {
        this.binners = binners;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}
