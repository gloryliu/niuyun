package com.niuyun.hire.ui.bean;

import java.util.List;

/**
 * Created by chen.zhiwei on 2017-7-25.
 */

public class JobTagBean {

    /**
     * code : 1000
     * msg : 成功
     * data : [{"id":9,"parentid":0,"categoryname":"医疗 | 制药 | 环保","categoryOrder":0,"statJobs":"","statResume":"","content":"","spell":"yiliaozhiyao","relation1":"","relation1Cn":"","relation2":"","relation2Cn":""},{"id":10,"parentid":0,"categoryname":"建筑 | 物业 | 农林牧渔 | 其他","categoryOrder":0,"statJobs":"","statResume":"","content":"","spell":"jianzhuwuye","relation1":"","relation1Cn":"","relation2":"","relation2Cn":""}]
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
         * id : 9
         * parentid : 0
         * categoryname : 医疗 | 制药 | 环保
         * categoryOrder : 0
         * statJobs :
         * statResume :
         * content :
         * spell : yiliaozhiyao
         * relation1 :
         * relation1Cn :
         * relation2 :
         * relation2Cn :
         */

        private int id;
        private int parentid;
        private String categoryname;
        private int categoryOrder;
        private String statJobs;
        private String statResume;
        private String content;
        private String spell;
        private String relation1;
        private String relation1Cn;
        private String relation2;
        private String relation2Cn;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getParentid() {
            return parentid;
        }

        public void setParentid(int parentid) {
            this.parentid = parentid;
        }

        public String getCategoryname() {
            return categoryname;
        }

        public void setCategoryname(String categoryname) {
            this.categoryname = categoryname;
        }

        public int getCategoryOrder() {
            return categoryOrder;
        }

        public void setCategoryOrder(int categoryOrder) {
            this.categoryOrder = categoryOrder;
        }

        public String getStatJobs() {
            return statJobs;
        }

        public void setStatJobs(String statJobs) {
            this.statJobs = statJobs;
        }

        public String getStatResume() {
            return statResume;
        }

        public void setStatResume(String statResume) {
            this.statResume = statResume;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getSpell() {
            return spell;
        }

        public void setSpell(String spell) {
            this.spell = spell;
        }

        public String getRelation1() {
            return relation1;
        }

        public void setRelation1(String relation1) {
            this.relation1 = relation1;
        }

        public String getRelation1Cn() {
            return relation1Cn;
        }

        public void setRelation1Cn(String relation1Cn) {
            this.relation1Cn = relation1Cn;
        }

        public String getRelation2() {
            return relation2;
        }

        public void setRelation2(String relation2) {
            this.relation2 = relation2;
        }

        public String getRelation2Cn() {
            return relation2Cn;
        }

        public void setRelation2Cn(String relation2Cn) {
            this.relation2Cn = relation2Cn;
        }
    }
}
