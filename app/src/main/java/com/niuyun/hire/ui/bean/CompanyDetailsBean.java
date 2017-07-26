package com.niuyun.hire.ui.bean;

/**
 * Created by chen.zhiwei on 2017-7-26.
 */

public class CompanyDetailsBean {

    /**
     * code : 1000
     * msg : 成功
     * data : {"id":8,"uid":50,"companyname":"APP公司测试","natureCn":"国企","tradeCn":"计算机软件/硬件","districtCn":"太原/小店区/体育路","scaleCn":"20人以下","logo":"1707/26/c9f0f895fb98ab9159f51fd0297e236d.jpg","contents":"公司简介","video":""}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 8
         * uid : 50
         * companyname : APP公司测试
         * natureCn : 国企
         * tradeCn : 计算机软件/硬件
         * districtCn : 太原/小店区/体育路
         * scaleCn : 20人以下
         * logo : 1707/26/c9f0f895fb98ab9159f51fd0297e236d.jpg
         * contents : 公司简介
         * video :
         */

        private int id;
        private int uid;
        private String companyname;
        private String natureCn;
        private String tradeCn;
        private String districtCn;
        private String scaleCn;
        private String logo;
        private String contents;
        private String video;

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

        public String getCompanyname() {
            return companyname;
        }

        public void setCompanyname(String companyname) {
            this.companyname = companyname;
        }

        public String getNatureCn() {
            return natureCn;
        }

        public void setNatureCn(String natureCn) {
            this.natureCn = natureCn;
        }

        public String getTradeCn() {
            return tradeCn;
        }

        public void setTradeCn(String tradeCn) {
            this.tradeCn = tradeCn;
        }

        public String getDistrictCn() {
            return districtCn;
        }

        public void setDistrictCn(String districtCn) {
            this.districtCn = districtCn;
        }

        public String getScaleCn() {
            return scaleCn;
        }

        public void setScaleCn(String scaleCn) {
            this.scaleCn = scaleCn;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getContents() {
            return contents;
        }

        public void setContents(String contents) {
            this.contents = contents;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }
    }
}
