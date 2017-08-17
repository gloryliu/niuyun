package com.niuyun.hire.ui.bean;

import java.util.List;

/**
 * Created by chen.zhiwei on 2017-8-10.
 */

public class MyAttentionListBean {


    /**
     * code : 1000
     * msg : 成功
     * data : {"pageNo":0,"totalCount":0,"pageCount":0,"pageSize":0,"data":[{"id":3,"uid":6,"companyId":3,"logo":null,"jobsName":"Java","companyname":"山西昱达传媒有限公司","districtCn":"太原/小店区/小店","experienceCn":"不限","educationCn":"不限","minwage":6200,"maxwage":8000}]}
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

    public static class DataBeanX {
        /**
         * pageNo : 0
         * totalCount : 0
         * pageCount : 0
         * pageSize : 0
         * data : [{"id":3,"uid":6,"companyId":3,"logo":null,"jobsName":"Java","companyname":"山西昱达传媒有限公司","districtCn":"太原/小店区/小店","experienceCn":"不限","educationCn":"不限","minwage":6200,"maxwage":8000}]
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

        public static class DataBean {
            /**
             * id : 3
             * uid : 6
             * companyId : 3
             * logo : null
             * jobsName : Java
             * companyname : 山西昱达传媒有限公司
             * districtCn : 太原/小店区/小店
             * experienceCn : 不限
             * educationCn : 不限
             * minwage : 6200
             * maxwage : 8000
             */

            private int id;
            private int uid;
            private int companyId;
            private Object logo;
            private String jobsName;
            private String companyname;
            private String districtCn;
            private String experienceCn;
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
        }
    }
}
