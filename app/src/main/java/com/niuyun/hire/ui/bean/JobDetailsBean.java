package com.niuyun.hire.ui.bean;

import java.io.Serializable;

/**
 * Created by chen.zhiwei on 2017-7-26.
 */

public class JobDetailsBean implements Serializable{

    /**
     * code : 1000
     * msg : 成功
     * data : {"id":8,"uid":50,"companyId":8,"logo":"1707/26/c9f0f895fb98ab9159f51fd0297e236d.jpg","jobsName":"UI设计师","companyname":"APP公司测试","districtCn":"太原/小店区/体育路","experienceCn":"1年以下","educationCn":"初中","minwage":1000,"maxwage":2000,"tag":"145|环境好,146|年终奖","tagCn":"环境好,年终奖,双休","contents":"职位描述","video":"","nature":46,"natureCn":"国企","trade":1,"tradeCn":"计算机软件/硬件","address":"太原市小店区小店区一中","scale":80,"scaleCn":"20人以下"}
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

    public static class DataBean implements Serializable{
        /**
         * id : 8
         * uid : 50
         * companyId : 8
         * logo : 1707/26/c9f0f895fb98ab9159f51fd0297e236d.jpg
         * jobsName : UI设计师
         * companyname : APP公司测试
         * districtCn : 太原/小店区/体育路
         * experienceCn : 1年以下
         * educationCn : 初中
         * minwage : 1000
         * maxwage : 2000
         * tag : 145|环境好,146|年终奖
         * tagCn : 环境好,年终奖,双休
         * contents : 职位描述
         * video :
         * nature : 46
         * natureCn : 国企
         * trade : 1
         * tradeCn : 计算机软件/硬件
         * address : 太原市小店区小店区一中
         * scale : 80
         * scaleCn : 20人以下
         */

        private int id;
        private int uid;
        private int companyId;
        private String logo;
        private String jobsName;
        private String companyname;
        private String districtCn;
        private String experienceCn;
        private String educationCn;
        private int minwage;
        private int maxwage;
        private String tag;
        private String tagCn;
        private String contents;
        private String video;
        private int nature;
        private String natureCn;
        private int trade;
        private String tradeCn;
        private String address;
        private int scale;
        private String scaleCn;

        public String getFollowFlag() {
            return followFlag;
        }

        public void setFollowFlag(String followFlag) {
            this.followFlag = followFlag;
        }

        /**是否对该职位关注1为没有关注2为已经关注*/
        private String followFlag;


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

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getJobsName() {
            return jobsName;
        }

        public void setJobsName(String jobsName) {
            this.jobsName = jobsName;
        }

        public String getCompanyname() {
            return companyname;
        }

        public void setCompanyname(String companyname) {
            this.companyname = companyname;
        }

        public String getDistrictCn() {
            return districtCn;
        }

        public void setDistrictCn(String districtCn) {
            this.districtCn = districtCn;
        }

        public String getExperienceCn() {
            return experienceCn;
        }

        public void setExperienceCn(String experienceCn) {
            this.experienceCn = experienceCn;
        }

        public String getEducationCn() {
            return educationCn;
        }

        public void setEducationCn(String educationCn) {
            this.educationCn = educationCn;
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

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTagCn() {
            return tagCn;
        }

        public void setTagCn(String tagCn) {
            this.tagCn = tagCn;
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

        public int getNature() {
            return nature;
        }

        public void setNature(int nature) {
            this.nature = nature;
        }

        public String getNatureCn() {
            return natureCn;
        }

        public void setNatureCn(String natureCn) {
            this.natureCn = natureCn;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getScale() {
            return scale;
        }

        public void setScale(int scale) {
            this.scale = scale;
        }

        public String getScaleCn() {
            return scaleCn;
        }

        public void setScaleCn(String scaleCn) {
            this.scaleCn = scaleCn;
        }
    }
}
