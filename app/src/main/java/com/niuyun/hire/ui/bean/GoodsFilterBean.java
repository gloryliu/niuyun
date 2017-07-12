package com.niuyun.hire.ui.bean;

import com.niuyun.hire.bean.ErrorBean;

/**
 * Created by chen.zhiwei on 2017-6-26.
 */

public class GoodsFilterBean extends ErrorBean {

    /**
     * id : 1
     * typename : 启蒙玩具
     */

    private int id;
    private String typename;

    public GoodsFilterBean(int id, String typename) {
        this.id = id;
        this.typename = typename;
    }

    public GoodsFilterBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }
}
