package com.niuyun.hire.ui.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by sun.luwei on 2017/1/3.
 */
@Entity
public class City {
    @Id()
    @SerializedName("fake_id")
    private long id;  // "00000",
    @Expose
    @SerializedName("id")
    @Property(nameInDb = "CITY_ID")
    public String cityId;  // "00000",
    public String name;  // "全国站",
    public String shortPy;  // "",
    public String provinceId;  // "0",
    public String provinceName;  // "",
    public String enabled;  // "1",
    public String isHot;  // "1"
    @Transient
    public String sort;  // ""   ,
    public String getIsHot() {
        return this.isHot;
    }
    public void setIsHot(String isHot) {
        this.isHot = isHot;
    }
    public String getEnabled() {
        return this.enabled;
    }
    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }
    public String getProvinceName() {
        return this.provinceName;
    }
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
    public String getProvinceId() {
        return this.provinceId;
    }
    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }
    public String getShortPy() {
        return this.shortPy;
    }
    public void setShortPy(String shortPy) {
        this.shortPy = shortPy;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCityId() {
        return this.cityId;
    }
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    @Generated(hash = 1188408014)
    public City(long id, String cityId, String name, String shortPy,
                String provinceId, String provinceName, String enabled, String isHot) {
        this.id = id;
        this.cityId = cityId;
        this.name = name;
        this.shortPy = shortPy;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.enabled = enabled;
        this.isHot = isHot;
    }
    @Generated(hash = 750791287)
    public City() {
    }
   
}
