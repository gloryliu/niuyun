package com.niuyun.hire.ui.bean;

import com.niuyun.hire.bean.ErrorBean;

/**
 * Created by chen.zhiwei on 2017-6-22.
 */

public class UserInfoBean extends ErrorBean {


    public String username;
    public String email;
    public String mobile;
    public String regTime;
    public String avatars;
    public String video;
    public int uid;
    public int utype;
    public int status;
    public int perfect;
    public String token;
    /**
     * 环信登陆用户名
     */
    public String chatUserName;

    /**
     * 环信登陆密码
     */
    public String chatPwd;

    /**
     * 简历id
     */
    public int resumeId;

    /**
     * 企业id
     */
    public int companyId;

}
