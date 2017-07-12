package com.niuyun.hire.ui.bean;

import com.niuyun.hire.bean.ErrorBean;

/**
 * Created by chen.zhiwei on 2017-6-26.
 */

public class ShopsFilterBean extends ErrorBean {

    /**
     * id : 1
     * shopname : 中关村苏州街店
     */

    private int id;
    private String shopname;

    public ShopsFilterBean(int id, String shopname) {
        this.id = id;
        this.shopname = shopname;
    }

    public ShopsFilterBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }
}
