package com.niuyun.hire.ui.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/13.
 */

public class PositionIntentBean {

    /**
     * code : 1000
     * msg : 成功
     * data : [{"id":3,"uid":50,"trade":0,"tradeCn":"string","category":0,"categoryCn":"string","district":0,"sdistrict":0,"tdistrict":0,"districtCn":"string","minwage":0,"maxwage":0,"wage":0,"wageCn":"string"}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 3
         * uid : 50
         * trade : 0
         * tradeCn : string
         * category : 0
         * categoryCn : string
         * district : 0
         * sdistrict : 0
         * tdistrict : 0
         * districtCn : string
         * minwage : 0
         * maxwage : 0
         * wage : 0
         * wageCn : string
         */

        private int id;
        private int uid;
        private int trade;
        private String tradeCn;
        private int category;
        private String categoryCn;
        private int district;
        private int sdistrict;
        private int tdistrict;
        private String districtCn;
        private int minwage;
        private int maxwage;
        private int wage;
        private String wageCn;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getTrade() {
            return trade;
        }

        public void setTrade(int trade) {
            this.trade = trade;
        }

        public String getTradeCn() {
            return tradeCn;
        }

        public void setTradeCn(String tradeCn) {
            this.tradeCn = tradeCn;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public String getCategoryCn() {
            return categoryCn;
        }

        public void setCategoryCn(String categoryCn) {
            this.categoryCn = categoryCn;
        }

        public int getDistrict() {
            return district;
        }

        public void setDistrict(int district) {
            this.district = district;
        }

        public int getSdistrict() {
            return sdistrict;
        }

        public void setSdistrict(int sdistrict) {
            this.sdistrict = sdistrict;
        }

        public int getTdistrict() {
            return tdistrict;
        }

        public void setTdistrict(int tdistrict) {
            this.tdistrict = tdistrict;
        }

        public String getDistrictCn() {
            return districtCn;
        }

        public void setDistrictCn(String districtCn) {
            this.districtCn = districtCn;
        }

        public int getMinwage() {
            return minwage;
        }

        public void setMinwage(int minwage) {
            this.minwage = minwage;
        }

        public int getMaxwage() {
            return maxwage;
        }

        public void setMaxwage(int maxwage) {
            this.maxwage = maxwage;
        }

        public int getWage() {
            return wage;
        }

        public void setWage(int wage) {
            this.wage = wage;
        }

        public String getWageCn() {
            return wageCn;
        }

        public void setWageCn(String wageCn) {
            this.wageCn = wageCn;
        }
    }
}
