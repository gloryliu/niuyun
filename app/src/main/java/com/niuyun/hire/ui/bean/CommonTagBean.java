package com.niuyun.hire.ui.bean;

import java.util.List;

/**
 * Created by chen.zhiwei on 2017-7-25.
 */

public class CommonTagBean {

    /**
     * code : 1000
     * msg : 成功
     * data : {"QS_experience":[{"cId":79,"cParentid":0,"cAlias":"QS_experience","cName":"10年以上","cOrder":0,"cIndex":"","cNote":"","statJobs":"","statResume":""}],"QS_education":[{"cId":73,"cParentid":0,"cAlias":"QS_education","cName":"博后","cOrder":0,"cIndex":"","cNote":"","statJobs":"","statResume":""}]}
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
        private List<CommonTagItemBean> QS_experience;
        private List<CommonTagItemBean> QS_education;
        private List<CommonTagItemBean> QS_trade;
        private List<CommonTagItemBean> QS_company_type;
        private List<CommonTagItemBean> QS_wage;
        private List<CommonTagItemBean> QS_scale;
        private List<CommonTagItemBean> QS_resumetag;
        private List<CommonTagItemBean> QS_language;
        private List<CommonTagItemBean> QS_language_level;
        private List<CommonTagItemBean> QS_current;
        private List<CommonTagItemBean> QS_age;
        private List<CommonTagItemBean> QS_jobtag;
        private List<CommonTagItemBean> QS_jobs_nature;

        public List<CommonTagItemBean> getQS_jobs_nature() {
            return QS_jobs_nature;
        }

        public void setQS_jobs_nature(List<CommonTagItemBean> QS_jobs_nature) {
            this.QS_jobs_nature = QS_jobs_nature;
        }

        public List<CommonTagItemBean> getQS_jobtag() {
            return QS_jobtag;
        }

        public void setQS_jobtag(List<CommonTagItemBean> QS_jobtag) {
            this.QS_jobtag = QS_jobtag;
        }

        public List<CommonTagItemBean> getQS_experience() {
            return QS_experience;
        }

        public void setQS_experience(List<CommonTagItemBean> QS_experience) {
            this.QS_experience = QS_experience;
        }

        public List<CommonTagItemBean> getQS_education() {
            return QS_education;
        }

        public void setQS_education(List<CommonTagItemBean> QS_education) {
            this.QS_education = QS_education;
        }

        public List<CommonTagItemBean> getQS_trade() {
            return QS_trade;
        }

        public void setQS_trade(List<CommonTagItemBean> QS_trade) {
            this.QS_trade = QS_trade;
        }

        public List<CommonTagItemBean> getQS_company_type() {
            return QS_company_type;
        }

        public void setQS_company_type(List<CommonTagItemBean> QS_company_type) {
            this.QS_company_type = QS_company_type;
        }

        public List<CommonTagItemBean> getQS_wage() {
            return QS_wage;
        }

        public void setQS_wage(List<CommonTagItemBean> QS_wage) {
            this.QS_wage = QS_wage;
        }

        public List<CommonTagItemBean> getQS_scale() {
            return QS_scale;
        }

        public void setQS_scale(List<CommonTagItemBean> QS_scale) {
            this.QS_scale = QS_scale;
        }

        public List<CommonTagItemBean> getQS_resumetag() {
            return QS_resumetag;
        }

        public void setQS_resumetag(List<CommonTagItemBean> QS_resumetag) {
            this.QS_resumetag = QS_resumetag;
        }

        public List<CommonTagItemBean> getQS_language() {
            return QS_language;
        }

        public void setQS_language(List<CommonTagItemBean> QS_language) {
            this.QS_language = QS_language;
        }

        public List<CommonTagItemBean> getQS_language_level() {
            return QS_language_level;
        }

        public void setQS_language_level(List<CommonTagItemBean> QS_language_level) {
            this.QS_language_level = QS_language_level;
        }

        public List<CommonTagItemBean> getQS_current() {
            return QS_current;
        }

        public void setQS_current(List<CommonTagItemBean> QS_current) {
            this.QS_current = QS_current;
        }

        public List<CommonTagItemBean> getQS_age() {
            return QS_age;
        }

        public void setQS_age(List<CommonTagItemBean> QS_age) {
            this.QS_age = QS_age;
        }
    }
}
