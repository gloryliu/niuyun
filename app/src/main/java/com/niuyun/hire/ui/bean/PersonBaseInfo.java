package com.niuyun.hire.ui.bean;

/**
 * Created by chen.zhiwei on 2017-8-11.
 */

public class PersonBaseInfo {

    /**
     * code : 1000
     * msg : 成功
     * data : {"avatars":"{\"code\":1000,\"msg\":\"1707/C:\\\\Users\\\\WWW\\\\74cms_v4.2.3\\\\upload\\\\data\\\\upload\\\\avatar\\\\1707\\\\25/20170725170525811.jpg\",\"data\":null}","id":23,"uid":50,"realname":"测具体","sex":1,"sexCn":"男","birthday":"待确定","residence":"待确定","education":65,"educationCn":"初中","experience":75,"experienceCn":"1年以下","phone":"13214141414","email":"待确定"}
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
         * avatars : {"code":1000,"msg":"1707/C:\\Users\\WWW\\74cms_v4.2.3\\upload\\data\\upload\\avatar\\1707\\25/20170725170525811.jpg","data":null}
         * id : 23
         * uid : 50
         * realname : 测具体
         * sex : 1
         * sexCn : 男
         * birthday : 待确定
         * residence : 待确定
         * education : 65
         * educationCn : 初中
         * experience : 75
         * experienceCn : 1年以下
         * phone : 13214141414
         * email : 待确定
         */

        private String avatars;
        private int id;
        private int uid;
        private String realname;
        private int sex;
        private String sexCn;
        private String birthday;
        private String residence;
        private int education;
        private String educationCn;
        private int experience;
        private String experienceCn;
        private String phone;
        private String email;

        public String getAvatars() {
            return avatars;
        }

        public void setAvatars(String avatars) {
            this.avatars = avatars;
        }

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

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getSexCn() {
            return sexCn;
        }

        public void setSexCn(String sexCn) {
            this.sexCn = sexCn;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getResidence() {
            return residence;
        }

        public void setResidence(String residence) {
            this.residence = residence;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
