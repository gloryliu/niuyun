package com.niuyun.hire.ui.bean;

import java.io.Serializable;

/**
 * Created by chen.zhiwei on 2017-6-27.
 */

public class MemberRankBean implements Serializable{

    /**
     * id : 4
     * grade : 1
     * name : 一级会员
     * money : 199
     * discount : 0.9
     */

    private int id;
    private int grade;
    private String name;
    private int money;
    private double discount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
