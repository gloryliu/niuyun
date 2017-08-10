package com.niuyun.hire.ui.bean;

import java.util.List;

/**
 * Created by chen.zhiwei on 2017-8-10.
 */

public class MyAttentionListBean {

    /**
     * code : 1000
     * msg : 成功
     * data : {"pageNo":1,"totalCount":2,"pageCount":1,"pageSize":10,"data":[{"did":3,"personalUid":50,"jobsId":3,"jobsName":"Java","addtime":1502353672},{"did":4,"personalUid":50,"jobsId":2,"jobsName":"UI设计","addtime":1502354211}]}
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
         * pageNo : 1
         * totalCount : 2
         * pageCount : 1
         * pageSize : 10
         * data : [{"did":3,"personalUid":50,"jobsId":3,"jobsName":"Java","addtime":1502353672},{"did":4,"personalUid":50,"jobsId":2,"jobsName":"UI设计","addtime":1502354211}]
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
             * did : 3
             * personalUid : 50
             * jobsId : 3
             * jobsName : Java
             * addtime : 1502353672
             */

            private int did;
            private int personalUid;
            private int jobsId;
            private String jobsName;
            private int addtime;

            public int getDid() {
                return did;
            }

            public void setDid(int did) {
                this.did = did;
            }

            public int getPersonalUid() {
                return personalUid;
            }

            public void setPersonalUid(int personalUid) {
                this.personalUid = personalUid;
            }

            public int getJobsId() {
                return jobsId;
            }

            public void setJobsId(int jobsId) {
                this.jobsId = jobsId;
            }

            public String getJobsName() {
                return jobsName;
            }

            public void setJobsName(String jobsName) {
                this.jobsName = jobsName;
            }

            public int getAddtime() {
                return addtime;
            }

            public void setAddtime(int addtime) {
                this.addtime = addtime;
            }
        }
    }
}
