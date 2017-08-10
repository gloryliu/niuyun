package com.niuyun.hire.ui.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chen.zhiwei on 2017-8-10.
 */

public class PreviewResumeBean implements Serializable{

    /**
     * code : 1000
     * msg : 成功
     * data : {"realname":"测具体","sex":1,"sexCn":"男","birthdate":"待确定","avatars":"{\"code\":1000,\"msg\":\"1707/C:\\\\Users\\\\WWW\\\\74cms_v4.2.3\\\\upload\\\\data\\\\upload\\\\avatar\\\\1707\\\\25/20170725170525811.jpg\",\"data\":null}","residence":"待确定","experience":75,"experienceCn":"1年以下","education":65,"educationCn":"初中","telephone":"13214141414","email":"待确定","nature":0,"natureCn":"待确定","wage":56,"wageCn":"1000~1500/月","intentionJobsId":"2.18.124","intentionJobs":"前台/总机/接待","specialty":"哦哦哦哦哦哦哦哦哦哦","resumeWork":[{"id":153,"pid":18,"uid":50,"startyear":2015,"startmonth":8,"endyear":2017,"endmonth":8,"companyname":"哈哈","jobs":"哈哈","achievements":"tut默默哦流量监控哦哦","todate":0}],"resumeEducation":[{"id":120,"pid":18,"uid":50,"startyear":1,"startmonth":2015,"endyear":2016,"endmonth":6,"school":"东京大学","speciality":"计算机","education":71,"educationCn":"硕士","todate":0,"campusId":0},{"id":121,"pid":18,"uid":50,"startyear":2010,"startmonth":8,"endyear":2017,"endmonth":8,"school":"测测","speciality":"测测","education":66,"educationCn":"高中","todate":0,"campusId":0},{"id":122,"pid":18,"uid":50,"startyear":2014,"startmonth":8,"endyear":2016,"endmonth":8,"school":"阿伦","speciality":"阿菊","education":66,"educationCn":"高中","todate":0,"campusId":0}],"completePercent":35}
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
         * realname : 测具体
         * sex : 1
         * sexCn : 男
         * birthdate : 待确定
         * avatars : {"code":1000,"msg":"1707/C:\\Users\\WWW\\74cms_v4.2.3\\upload\\data\\upload\\avatar\\1707\\25/20170725170525811.jpg","data":null}
         * residence : 待确定
         * experience : 75
         * experienceCn : 1年以下
         * education : 65
         * educationCn : 初中
         * telephone : 13214141414
         * email : 待确定
         * nature : 0
         * natureCn : 待确定
         * wage : 56
         * wageCn : 1000~1500/月
         * intentionJobsId : 2.18.124
         * intentionJobs : 前台/总机/接待
         * specialty : 哦哦哦哦哦哦哦哦哦哦
         * resumeWork : [{"id":153,"pid":18,"uid":50,"startyear":2015,"startmonth":8,"endyear":2017,"endmonth":8,"companyname":"哈哈","jobs":"哈哈","achievements":"tut默默哦流量监控哦哦","todate":0}]
         * resumeEducation : [{"id":120,"pid":18,"uid":50,"startyear":1,"startmonth":2015,"endyear":2016,"endmonth":6,"school":"东京大学","speciality":"计算机","education":71,"educationCn":"硕士","todate":0,"campusId":0},{"id":121,"pid":18,"uid":50,"startyear":2010,"startmonth":8,"endyear":2017,"endmonth":8,"school":"测测","speciality":"测测","education":66,"educationCn":"高中","todate":0,"campusId":0},{"id":122,"pid":18,"uid":50,"startyear":2014,"startmonth":8,"endyear":2016,"endmonth":8,"school":"阿伦","speciality":"阿菊","education":66,"educationCn":"高中","todate":0,"campusId":0}]
         * completePercent : 35
         */

        private String realname;
        private int sex;
        private String sexCn;
        private String birthdate;
        private String avatars;
        private String residence;
        private int experience;
        private String experienceCn;
        private int education;
        private String educationCn;
        private String telephone;
        private String email;
        private int nature;
        private String natureCn;
        private int wage;
        private String wageCn;
        private String intentionJobsId;
        private String intentionJobs;
        private String specialty;
        private int completePercent;
        private List<ResumeWorkBean> resumeWork;
        private List<ResumeEducationBean> resumeEducation;

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

        public String getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(String birthdate) {
            this.birthdate = birthdate;
        }

        public String getAvatars() {
            return avatars;
        }

        public void setAvatars(String avatars) {
            this.avatars = avatars;
        }

        public String getResidence() {
            return residence;
        }

        public void setResidence(String residence) {
            this.residence = residence;
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

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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

        public int getWage() {
            return wage;
        }

        public void setWage(int wage) {
            this.wage = wage;
        }

        public String getWageCn() {
            return wageCn;
        }

        public void setWageCn(String wageCn) {
            this.wageCn = wageCn;
        }

        public String getIntentionJobsId() {
            return intentionJobsId;
        }

        public void setIntentionJobsId(String intentionJobsId) {
            this.intentionJobsId = intentionJobsId;
        }

        public String getIntentionJobs() {
            return intentionJobs;
        }

        public void setIntentionJobs(String intentionJobs) {
            this.intentionJobs = intentionJobs;
        }

        public String getSpecialty() {
            return specialty;
        }

        public void setSpecialty(String specialty) {
            this.specialty = specialty;
        }

        public int getCompletePercent() {
            return completePercent;
        }

        public void setCompletePercent(int completePercent) {
            this.completePercent = completePercent;
        }

        public List<ResumeWorkBean> getResumeWork() {
            return resumeWork;
        }

        public void setResumeWork(List<ResumeWorkBean> resumeWork) {
            this.resumeWork = resumeWork;
        }

        public List<ResumeEducationBean> getResumeEducation() {
            return resumeEducation;
        }

        public void setResumeEducation(List<ResumeEducationBean> resumeEducation) {
            this.resumeEducation = resumeEducation;
        }

        public static class ResumeWorkBean implements Serializable{
            /**
             * id : 153
             * pid : 18
             * uid : 50
             * startyear : 2015
             * startmonth : 8
             * endyear : 2017
             * endmonth : 8
             * companyname : 哈哈
             * jobs : 哈哈
             * achievements : tut默默哦流量监控哦哦
             * todate : 0
             */

            private int id;
            private int pid;
            private int uid;
            private int startyear;
            private int startmonth;
            private int endyear;
            private int endmonth;
            private String companyname;
            private String jobs;
            private String achievements;
            private int todate;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public int getStartyear() {
                return startyear;
            }

            public void setStartyear(int startyear) {
                this.startyear = startyear;
            }

            public int getStartmonth() {
                return startmonth;
            }

            public void setStartmonth(int startmonth) {
                this.startmonth = startmonth;
            }

            public int getEndyear() {
                return endyear;
            }

            public void setEndyear(int endyear) {
                this.endyear = endyear;
            }

            public int getEndmonth() {
                return endmonth;
            }

            public void setEndmonth(int endmonth) {
                this.endmonth = endmonth;
            }

            public String getCompanyname() {
                return companyname;
            }

            public void setCompanyname(String companyname) {
                this.companyname = companyname;
            }

            public String getJobs() {
                return jobs;
            }

            public void setJobs(String jobs) {
                this.jobs = jobs;
            }

            public String getAchievements() {
                return achievements;
            }

            public void setAchievements(String achievements) {
                this.achievements = achievements;
            }

            public int getTodate() {
                return todate;
            }

            public void setTodate(int todate) {
                this.todate = todate;
            }
        }

        public static class ResumeEducationBean implements Serializable{
            /**
             * id : 120
             * pid : 18
             * uid : 50
             * startyear : 1
             * startmonth : 2015
             * endyear : 2016
             * endmonth : 6
             * school : 东京大学
             * speciality : 计算机
             * education : 71
             * educationCn : 硕士
             * todate : 0
             * campusId : 0
             */

            private int id;
            private int pid;
            private int uid;
            private int startyear;
            private int startmonth;
            private int endyear;
            private int endmonth;
            private String school;
            private String speciality;
            private int education;
            private String educationCn;
            private int todate;
            private int campusId;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public int getStartyear() {
                return startyear;
            }

            public void setStartyear(int startyear) {
                this.startyear = startyear;
            }

            public int getStartmonth() {
                return startmonth;
            }

            public void setStartmonth(int startmonth) {
                this.startmonth = startmonth;
            }

            public int getEndyear() {
                return endyear;
            }

            public void setEndyear(int endyear) {
                this.endyear = endyear;
            }

            public int getEndmonth() {
                return endmonth;
            }

            public void setEndmonth(int endmonth) {
                this.endmonth = endmonth;
            }

            public String getSchool() {
                return school;
            }

            public void setSchool(String school) {
                this.school = school;
            }

            public String getSpeciality() {
                return speciality;
            }

            public void setSpeciality(String speciality) {
                this.speciality = speciality;
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

            public int getTodate() {
                return todate;
            }

            public void setTodate(int todate) {
                this.todate = todate;
            }

            public int getCampusId() {
                return campusId;
            }

            public void setCampusId(int campusId) {
                this.campusId = campusId;
            }
        }
    }
}
