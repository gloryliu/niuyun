package com.niuyun.hire.ui.bean;

import java.util.List;

/**
 * Created by chen.zhiwei on 2017-8-16.
 */

public class HotSearchBean {

    /**
     * code : 1000
     * msg : 成功
     * data : [{"id":11,"keyword":"老罗","searchCount":4},{"id":2,"keyword":"UI","searchCount":3},{"id":1,"keyword":"安卓","searchCount":2},{"id":4,"keyword":"哈哈","searchCount":2},{"id":5,"keyword":"哥哥","searchCount":1},{"id":3,"keyword":"弟弟","searchCount":1},{"id":7,"keyword":"解决","searchCount":1},{"id":8,"keyword":"正在","searchCount":1},{"id":9,"keyword":"VV","searchCount":1},{"id":10,"keyword":"看看","searchCount":1}]
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
         * id : 11
         * keyword : 老罗
         * searchCount : 4
         */

        private int id;
        private String keyword;
        private int searchCount;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public int getSearchCount() {
            return searchCount;
        }

        public void setSearchCount(int searchCount) {
            this.searchCount = searchCount;
        }
    }
}
