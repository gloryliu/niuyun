package com.niuyun.hire.ui.bean;

import java.util.List;

/**
 * Created by chen.zhiwei on 2017-9-1.
 */

public class AroundResultBean {

    /**
     * code : 1000
     * msg : 成功
     * data : [{"type":1,"logo":"1707/04/eccbc87e4b5ce2fe28308fd9f2a7baf3.png","companyname":"山西昱达传媒有限公司","companyId":3,"jobsName":"程序员","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":6},{"type":1,"logo":"","companyname":"都查不出上档次哦你","companyId":5,"jobsName":null,"distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":16},{"type":1,"logo":"","companyname":"北斗科技有限公司","companyId":8,"jobsName":null,"distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":60},{"type":1,"logo":"","companyname":"山西昱达广告传媒有限公司","companyId":10,"jobsName":null,"distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":68},{"type":1,"logo":"","companyname":"山西爱特商贸有限公司","companyId":12,"jobsName":"撒大幅","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":75},{"type":1,"logo":"","companyname":"山西快洁科技有限公司","companyId":15,"jobsName":null,"distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":179},{"type":1,"logo":"","companyname":"山西中北斗科技公司","companyId":16,"jobsName":null,"distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":270},{"type":1,"logo":"待确定","companyname":"测试","companyId":18,"jobsName":null,"distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":281},{"type":1,"logo":"","companyname":"app测试用","companyId":20,"jobsName":"android开发工程师","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":307},{"type":1,"logo":"","companyname":"福万家互联网信息服务有限公司","companyId":22,"jobsName":null,"distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":530},{"type":1,"logo":"","companyname":"满圆红家居有限公司","companyId":23,"jobsName":"生产家具制造","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":533},{"type":1,"logo":"","companyname":"深圳昊德贸易有限公司太原分公司","companyId":24,"jobsName":"市场营销员","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":547},{"type":1,"logo":"","companyname":"太原德邦物流有限公司","companyId":26,"jobsName":"快递员","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":552},{"type":1,"logo":"","companyname":"山西灵鑫茂贸易有限公司","companyId":27,"jobsName":"销售代表6000+提供住宿+带薪培训","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":553},{"type":1,"logo":"","companyname":"泰康人寿保险股份有限公司山西分公司","companyId":28,"jobsName":"客服专员","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":557},{"type":1,"logo":"","companyname":"山西省科普科技教育发展中心","companyId":29,"jobsName":"复原军人","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":566},{"type":1,"logo":"","companyname":"太原市迎泽区皓邦净水器经营部","companyId":30,"jobsName":"核磁工程师","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":574},{"type":1,"logo":"","companyname":"山西鑫源梦网络科技有限公司","companyId":31,"jobsName":"互联网管培生","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":578},{"type":1,"logo":"","companyname":"太原市保安服务有限公司城管分公司","companyId":32,"jobsName":"保安员","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":590},{"type":1,"logo":"","companyname":"太原人才大市场","companyId":33,"jobsName":"文秘","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":605},{"type":1,"logo":"","companyname":"太原市小店区鲲鹏中环房屋信息服务部","companyId":34,"jobsName":"店长   储备店长","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":609},{"type":1,"logo":"","companyname":"奥瑞安能源国际有限公司","companyId":37,"jobsName":null,"distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":620},{"type":1,"logo":"","companyname":"北京凡恩教育科技有限公司","companyId":39,"jobsName":"课程顾问/销售【太航校区】","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":624},{"type":1,"logo":"","companyname":"太原陈泽铭配送服务有限公司","companyId":40,"jobsName":"快递送餐员","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":636},{"type":1,"logo":"","companyname":"山西浩旭工程项目管理有限公司","companyId":41,"jobsName":"电话销售","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":649},{"type":1,"logo":"","companyname":"洛阳威尔若普检测技术有限公司","companyId":42,"jobsName":"销售代表","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":652},{"type":1,"logo":"","companyname":"北京鑫水世界环境工程有限公司","companyId":43,"jobsName":"办公文秘","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":655},{"type":1,"logo":"","companyname":"山西中盛企业管理咨询有限公司","companyId":44,"jobsName":"客服代表","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":657},{"type":1,"logo":"","companyname":"太原吉亿达科技有限公司","companyId":45,"jobsName":"销售主管","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":658},{"type":1,"logo":"","companyname":"太原爱赢天下教育信息咨询有限公司","companyId":46,"jobsName":"英语讲师","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":659},{"type":1,"logo":"","companyname":"山西阜鑫消防工程有限公司","companyId":47,"jobsName":"办公室文员","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":660},{"type":1,"logo":"","companyname":"山西豪百利商贸有限公司","companyId":48,"jobsName":"人事","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":661},{"type":1,"logo":"","companyname":"山西欧瑞利山西欧瑞利电子商务有限公司","companyId":49,"jobsName":null,"distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":662},{"type":1,"logo":"","companyname":"山西正诚矿山安全技术研究所（有限公司）","companyId":50,"jobsName":"环境影响评价师","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":663},{"type":1,"logo":"","companyname":"亚太石油有限公司","companyId":51,"jobsName":"美工/设计【五险/双休/朝九晚五】","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":664},{"type":1,"logo":"","companyname":"北京星空明月文化传媒有限公司","companyId":52,"jobsName":"诚聘护士护理包食宿","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":689},{"type":1,"logo":"1708/14/a684eceee76fc522773286a895bc8436.jpg","companyname":"app测试招聘","companyId":54,"jobsName":"Android开发","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":732},{"type":1,"logo":"","companyname":"太原一和瑞景企业管理咨询有限公司","companyId":56,"jobsName":"高薪直聘销售","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":766},{"type":1,"logo":"","companyname":"郑州晋勤贸易有限公司","companyId":58,"jobsName":null,"distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":899},{"type":1,"logo":"","companyname":"华英教育","companyId":59,"jobsName":null,"distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":913},{"type":1,"logo":"","companyname":"阳泉煤业（集团）有限责任公司太原漾泉大酒店","companyId":60,"jobsName":"包饺子工","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":914},{"type":1,"logo":"1709/01/44f683a84163b3523afe57c2e008bc8c.jpg","companyname":"昊德贸易太原分公司","companyId":62,"jobsName":"电话销售","distence":0,"personName":null,"avatars":null,"sexCn":null,"specialty":null,"uid":917}]
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
         * type : 1
         * logo : 1707/04/eccbc87e4b5ce2fe28308fd9f2a7baf3.png
         * companyname : 山西昱达传媒有限公司
         * companyId : 3
         * jobsName : 程序员
         * distence : 0
         * personName : null
         * avatars : null
         * sexCn : null
         * specialty : null
         * uid : 6
         */

        private int type;
        private String logo;
        private String companyname;
        private int companyId;
        private String jobsName;
        private int distence;
        private Object personName;
        private Object avatars;
        private Object sexCn;
        private Object specialty;
        private int uid;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getCompanyname() {
            return companyname;
        }

        public void setCompanyname(String companyname) {
            this.companyname = companyname;
        }

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }

        public String getJobsName() {
            return jobsName;
        }

        public void setJobsName(String jobsName) {
            this.jobsName = jobsName;
        }

        public int getDistence() {
            return distence;
        }

        public void setDistence(int distence) {
            this.distence = distence;
        }

        public Object getPersonName() {
            return personName;
        }

        public void setPersonName(Object personName) {
            this.personName = personName;
        }

        public Object getAvatars() {
            return avatars;
        }

        public void setAvatars(Object avatars) {
            this.avatars = avatars;
        }

        public Object getSexCn() {
            return sexCn;
        }

        public void setSexCn(Object sexCn) {
            this.sexCn = sexCn;
        }

        public Object getSpecialty() {
            return specialty;
        }

        public void setSpecialty(Object specialty) {
            this.specialty = specialty;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
    }
}
