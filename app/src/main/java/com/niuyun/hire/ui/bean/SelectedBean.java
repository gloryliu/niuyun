package com.niuyun.hire.ui.bean;

/**
 * Created by liu.zhenrong on 2017/2/17.
 */

public class SelectedBean {
    private String key;
    private String name;

    public SelectedBean(){

    }

    public SelectedBean(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }
}
