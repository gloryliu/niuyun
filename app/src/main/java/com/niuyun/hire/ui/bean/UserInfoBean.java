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
    public int utype;//1企业
    public int status;
    public int perfect;
    public String token;
    /**
     * 云通讯登陆id
     */
    private String identify;

    /**
     * 服务器授权签名
     */
    private String userSig;


    /**
     * 简历id
     */
    public int resumeId;

    /**
     * 企业id
     */
    public int companyId;

    public int memberUid;
    /**
     * 目前状态id
     */
    public int current;

    /**
     * 目前状态
     */
    public String currentCn;

    /**
     * 简历完整度
     */
    public int completePercent;
    /**
     * 联系人职位
     */
    public String contactTitle;


}
