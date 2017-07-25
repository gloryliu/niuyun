package com.niuyun.hire.ui.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chen.zhiwei on 2017-7-25.
 */

@Entity
public class DataBean {
    /**
     * galias : QS_trade
     * gname : 行业分类
     * gid : 1
     * gsys : 1
     */
    @Id(autoincrement = true)
    private Long id;
    private String galias;
    private String gname;
    @NotNull
    private long gid;
    private int gsys;
    @Generated(hash = 1234427854)
    public DataBean(Long id, String galias, String gname, long gid, int gsys) {
        this.id = id;
        this.galias = galias;
        this.gname = gname;
        this.gid = gid;
        this.gsys = gsys;
    }
    @Generated(hash = 908697775)
    public DataBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getGalias() {
        return this.galias;
    }
    public void setGalias(String galias) {
        this.galias = galias;
    }
    public String getGname() {
        return this.gname;
    }
    public void setGname(String gname) {
        this.gname = gname;
    }
    public long getGid() {
        return this.gid;
    }
    public void setGid(long gid) {
        this.gid = gid;
    }
    public int getGsys() {
        return this.gsys;
    }
    public void setGsys(int gsys) {
        this.gsys = gsys;
    }

}
