package com.niuyun.hire.utils;

/**
 * Created by zhao.wenchao on 2017/5/19.
 * email: zhao.wenchao@jyall.com
 * introduce:
 */

public enum AppSchemeAndPkgEnum {
    CLOUD_SCHEME("jyyallcloud://"), JYALL_SCHEME("myapp://"), YSALL_SCHEME("ysallHomeDoctor://"), SHOUBA_SCHEME("shoubajyall://")
    ,CLOUD_PKG("com.jyall.cloud"),JYALL_PKG("com.jyall.app.home"),YSALL_PKG("com.jyall.health.client"),SHOUBA_PKG("com.jyall.mpos");

    private String value;

    AppSchemeAndPkgEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
