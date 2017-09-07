package com.niuyun.hire.ui.bean;

import java.util.List;

/**
 * Created by chen.zhiwei on 2017-9-7.
 */

public class AttentionMeBean {

    /**
     * code : 1000
     * msg : 成功
     * data : [{"companyId":18,"uid":281,"companyname":"测试","logo":"待确定","jobsName":"测试测试","districtCn":"太原/小店区/体育路","educationCn":"高中","experienceCn":"无经验","minwage":1500,"maxwage":2000,"addtime":1504690035},{"companyId":18,"uid":281,"companyname":"测试","logo":"待确定","jobsName":"测试测试","districtCn":"太原/小店区/体育路","educationCn":"高中","experienceCn":"无经验","minwage":1500,"maxwage":2000,"addtime":1504690035}]
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
         * companyId : 18
         * uid : 281
         * companyname : 测试
         * logo : 待确定
         * jobsName : 测试测试
         * districtCn : 太原/小店区/体育路
         * educationCn : 高中
         * experienceCn : 无经验
         * minwage : 1500
         * maxwage : 2000
         * addtime : 1504690035
         */

        private int companyId;
        private int uid;
        private String companyname;
        private String logo;
        private String jobsName;
        private String districtCn;
        private String educationCn;
        private String experienceCn;
        private int minwage;
        private int maxwage;
        private int addtime;

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
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

        public String getDistrictCn() {
            return districtCn;
        }

        public void setDistrictCn(String districtCn) {
            this.districtCn = districtCn;
        }

        public String getEducationCn() {
            return educationCn;
        }

        public void setEducationCn(String educationCn) {
            this.educationCn = educationCn;
        }

        public String getExperienceCn() {
            return experienceCn;
        }

        public void setExperienceCn(String experienceCn) {
            this.experienceCn = experienceCn;
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

        public int getAddtime() {
            return addtime;
        }

        public void setAddtime(int addtime) {
            this.addtime = addtime;
        }
    }
}
