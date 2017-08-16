package com.niuyun.hire.ui.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by sun.luwei on 2015/12/14.
 * 搜索关键词历史
 */
@Entity
public class SearchHistoryBean {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private int type;
    @Generated(hash = 395909940)
    public SearchHistoryBean(Long id, String name, int type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
    @Generated(hash = 1570282321)
    public SearchHistoryBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }


}
