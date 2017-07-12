package com.niuyun.hire.ui.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chen.zhiwei on 2017-7-4.
 */

public class BuyOrderListItemBean implements Serializable {

    /**
     * data : {"pageNo":1,"totalCount":4,"pageCount":1,"pageSize":20,"data":[{"shopName":"中关村苏州街店","shop":1,"orderStatus":"交易进行","goodsimg":null,"count":1,"purchase":100,"totalmoney":1},{"shopName":"三里屯店","shop":3,"orderStatus":"交易进行","goodsimg":"http://img12.360buyimg.com/n1/jfs/t3094/355/4810845975/174436/87328e1b/585387d9Ne07010fd.jpg","count":1,"purchase":30,"totalmoney":1}]}
     */

    /**
     * pageNo : 1
     * totalCount : 4
     * pageCount : 1
     * pageSize : 20
     * data : [{"shopName":"中关村苏州街店","shop":1,"orderStatus":"交易进行","goodsimg":null,"count":1,"purchase":100,"totalmoney":1},{"shopName":"三里屯店","shop":3,"orderStatus":"交易进行","goodsimg":"http://img12.360buyimg.com/n1/jfs/t3094/355/4810845975/174436/87328e1b/585387d9Ne07010fd.jpg","count":1,"purchase":30,"totalmoney":1}]
     */

    private int pageNo;
    private int totalCount;
    private int pageCount;
    private int pageSize;
    private List<DataBean> data;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * shopName : 中关村苏州街店
         * shop : 1
         * orderStatus : 交易进行
         * goodsimg : null
         * count : 1
         * purchase : 100
         * totalmoney : 1
         */

        private String shopName;
        private int shop;
        private String orderStatus;
        private String goodsimg;
        private int count;
        private int purchase;
        private int totalmoney;
        private String goodsName;
        private String id;

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public int getShop() {
            return shop;
        }

        public void setShop(int shop) {
            this.shop = shop;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getGoodsimg() {
            return goodsimg;
        }

        public void setGoodsimg(String goodsimg) {
            this.goodsimg = goodsimg;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getPurchase() {
            return purchase;
        }

        public void setPurchase(int purchase) {
            this.purchase = purchase;
        }

        public int getTotalmoney() {
            return totalmoney;
        }

        public void setTotalmoney(int totalmoney) {
            this.totalmoney = totalmoney;
        }
    }
}
