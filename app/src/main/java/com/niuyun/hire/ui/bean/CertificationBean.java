package com.niuyun.hire.ui.bean;

/**
 * Created by chen.zhiwei on 2017-9-1.
 */

public class CertificationBean {

    /**
     * code : 1000
     * msg : 成功
     * data : {"audit":0,"auditCn":"未认证","certificateImg":"待确定"}
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
         * audit : 0
         * auditCn : 未认证
         * certificateImg : 待确定
         */

        private int audit;
        private String auditCn;
        private String certificateImg;

        public int getAudit() {
            return audit;
        }

        public void setAudit(int audit) {
            this.audit = audit;
        }

        public String getAuditCn() {
            return auditCn;
        }

        public void setAuditCn(String auditCn) {
            this.auditCn = auditCn;
        }

        public String getCertificateImg() {
            return certificateImg;
        }

        public void setCertificateImg(String certificateImg) {
            this.certificateImg = certificateImg;
        }
    }
}
