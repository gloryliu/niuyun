package com.niuyun.hire.ui.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chen.zhiwei on 2017-8-25.
 */

public class EnterpriseFindPersonBean implements Serializable{

    /**
     * code : 1000
     * msg : 成功
     * data : {"pageNo":1,"totalCount":1004,"pageCount":1004,"pageSize":1,"data":[{"id":992,"uid":1176,"fullname":"秦慧玲","sexCn":"女","educationCn":"本科","experienceCn":"3到5年","wageCn":"2K~3K/月","specialty":"工作认真负责，不骄不躁，曾在一私企担任会计一职，熟悉小规模和一般纳税人账务流程；建筑业材料会计任职三年。","intentionJobs":"会计/会计师,出纳,审计专员/助理","avatars":"","video":""}]}
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
         * totalCount : 1004
         * pageCount : 1004
         * pageSize : 1
         * data : [{"id":992,"uid":1176,"fullname":"秦慧玲","sexCn":"女","educationCn":"本科","experienceCn":"3到5年","wageCn":"2K~3K/月","specialty":"工作认真负责，不骄不躁，曾在一私企担任会计一职，熟悉小规模和一般纳税人账务流程；建筑业材料会计任职三年。","intentionJobs":"会计/会计师,出纳,审计专员/助理","avatars":"","video":""}]
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
             * id : 992
             * uid : 1176
             * fullname : 秦慧玲
             * sexCn : 女
             * educationCn : 本科
             * experienceCn : 3到5年
             * wageCn : 2K~3K/月
             * specialty : 工作认真负责，不骄不躁，曾在一私企担任会计一职，熟悉小规模和一般纳税人账务流程；建筑业材料会计任职三年。
             * intentionJobs : 会计/会计师,出纳,审计专员/助理
             * avatars :
             * video :
             */

            private int id;
            private int uid;
            private String fullname;
            private String sexCn;
            private String educationCn;
            private String experienceCn;
            private String wageCn;
            private String specialty;
            private String intentionJobs;
            private String avatars;
            private String video;

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

            public String getFullname() {
                return fullname;
            }

            public void setFullname(String fullname) {
                this.fullname = fullname;
            }

            public String getSexCn() {
                return sexCn;
            }

            public void setSexCn(String sexCn) {
                this.sexCn = sexCn;
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

            public String getWageCn() {
                return wageCn;
            }

            public void setWageCn(String wageCn) {
                this.wageCn = wageCn;
            }

            public String getSpecialty() {
                return specialty;
            }

            public void setSpecialty(String specialty) {
                this.specialty = specialty;
            }

            public String getIntentionJobs() {
                return intentionJobs;
            }

            public void setIntentionJobs(String intentionJobs) {
                this.intentionJobs = intentionJobs;
            }

            public String getAvatars() {
                return avatars;
            }

            public void setAvatars(String avatars) {
                this.avatars = avatars;
            }

            public String getVideo() {
                return video;
            }

            public void setVideo(String video) {
                this.video = video;
            }
        }
    }
}
