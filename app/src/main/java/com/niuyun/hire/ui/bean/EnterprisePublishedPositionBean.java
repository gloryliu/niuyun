package com.niuyun.hire.ui.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chen.zhiwei on 2017-8-23.
 */

public class EnterprisePublishedPositionBean implements Serializable{

    /**
     * code : 1000
     * msg : 成功
     * data : {"pageNo":1,"totalCount":1,"pageCount":1,"pageSize":10,"data":[{"id":101,"uid":281,"companyId":18,"logo":null,"jobsName":"测试","companyname":"测试企业","district":1,"sdistrict":14,"tdistrict":296,"districtCn":"太原/小店区/体育路","experience":75,"experienceCn":"1年以下","education":66,"educationCn":"高中","minwage":1500,"maxwage":2000}]}
     */

    private int code;
    private String msg;
    private DataBeanX data;

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

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX implements Serializable{
        /**
         * pageNo : 1
         * totalCount : 1
         * pageCount : 1
         * pageSize : 10
         * data : [{"id":101,"uid":281,"companyId":18,"logo":null,"jobsName":"测试","companyname":"测试企业","district":1,"sdistrict":14,"tdistrict":296,"districtCn":"太原/小店区/体育路","experience":75,"experienceCn":"1年以下","education":66,"educationCn":"高中","minwage":1500,"maxwage":2000}]
         */

        private int pageNo;
        private int totalCount;
        private int pageCount;
        private int pageSize;
        private List<DataBean> data;

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean implements Serializable{
            /**
             * id : 101
             * uid : 281
             * companyId : 18
             * logo : null
             * jobsName : 测试
             * companyname : 测试企业
             * district : 1
             * sdistrict : 14
             * tdistrict : 296
             * districtCn : 太原/小店区/体育路
             * experience : 75
             * experienceCn : 1年以下
             * education : 66
             * educationCn : 高中
             * minwage : 1500
             * maxwage : 2000
             */

            private int id;
            private int uid;
            private int companyId;
            private Object logo;
            private String jobsName;
            private String companyname;
            private int district;
            private int sdistrict;
            private int tdistrict;
            private String districtCn;
            private int experience;
            private String experienceCn;
            private int education;
            private String educationCn;
            private int minwage;
            private int maxwage;

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

            public Object getLogo() {
                return logo;
            }

            public void setLogo(Object logo) {
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

            public int getExperience() {
                return experience;
            }

            public void setExperience(int experience) {
                this.experience = experience;
            }

            public String getExperienceCn() {
                return experienceCn;
            }

            public void setExperienceCn(String experienceCn) {
                this.experienceCn = experienceCn;
            }

            public int getEducation() {
                return education;
            }

            public void setEducation(int education) {
                this.education = education;
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
        }
    }
}
