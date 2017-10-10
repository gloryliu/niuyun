package com.niuyun.hire.base;

/**
 * Created by sun.luwei on 2016/12/20.
 */

public class Constants {
    public static final String COMMON_PERSON_URL = "http://niuyunzp.com/data/upload/avatar/";
    public static final String COMMON_IMAGE_URL = "http://niuyunzp.com/data/upload/company_logo/";
    public static final String COMMON_cover_URL = "http://niuyunzp.com/data/upload/coverImage_img/";
    public static final String COMMON_certificate_URL = "http://niuyunzp.com/data/upload/certificate_img/";
    public static final int AddressUpdateSuccess = 0x10;
    public static final String APP_KEY = "c3c5a51f3a4c70827523f8";
    public static String DEFAULT_CITYID = "110100000";//默认城市 Id 北京
    public static String SELECTED_CITY_ID = "SELECTED_CITY_ID";
    public static String SELECTED_CITY_NAME = "SELECTED_CITY_NAME";
    public static int successCode = 1000;
    public static String INT_TAG = "int_tag";
    public static String EXTRA_USER_ID="identify";
    public static String EXTRA_CAHT_TYPE="type";
    public static String EXTRA_CARD_MESSAGE="extra_chat_message";

    public static class ErrorCode {
        public static String check_code = "400001039";//验证码错误
    }

    public static final int SHOW_CHECK_CODE = 0x20;//显示验证码
    public static final int HIDE_CHECK_CODE = 0x21;//隐藏证码
    public static final int REGIST_SUCCESS = 0x22;//注册成功
    public static final int CHANGE_PASSWORD_SUCCESS = 0x23;//修改密码成功
    public static final int PAY_MEMBER_SUCCESS = 0x24;//修改密码成功
    public static final int LOGIN_FAILURE = 0x40;//登录失败
    public static final int LOGIN_SUCCESS = 0x41;//登录成功
    public static final int PERFECT_INFO_SUCCESS = 0x42;//完善信息成功
    public static final String LIVE_PASSWORD = "123456";//完善信息成功
    public static final int UPDATE_LIVE_RESUME = 0x43;//需要更新在线简历
    public static final int VIDEO_RECORD_REQUEST = 0x44;//录制视频
    public static final int UPDATE_POSITION_INTENT = 0x45;//需要更新求职意向
    public static final int GET_VIDEO_VID = 0x46;//获取视频vid
    public static final int UPDATE_PERSON = 0x47;//更新个人信息
    public static final int UPDATE_PUBLISHED_POSITION = 0x48;//更新发布的职位列表


    //个人头像
    public static final int resultCode_header_Photos = 10;//跳转到相册
    public static final int resultCode_header_Camera = 11;//跳转到相机

    //企业logo
    public static final int resultCode_logo_Photos = 12;//跳转到相册
    public static final int resultCode_logo_Camera = 13;//跳转到相机

    //发布职位获取地址
    public static final int resultCode_baidu_address = 14;
}
