package com.niuyun.hire.api;


import com.niuyun.hire.bean.ErrorBean;
import com.niuyun.hire.ui.bean.AllJobsBean;
import com.niuyun.hire.ui.bean.AllTagBean;
import com.niuyun.hire.ui.bean.CommonTagBean;
import com.niuyun.hire.ui.bean.CompanyDetailsBean;
import com.niuyun.hire.ui.bean.GetBaseTagBean;
import com.niuyun.hire.ui.bean.JobDetailsBean;
import com.niuyun.hire.ui.bean.JobTagBean;
import com.niuyun.hire.ui.bean.LiveListBean;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.bean.UserInfoBean;
import com.niuyun.hire.ui.polyvLive.bean.CreateLiveBean;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


/**
 * 网络请求接口
 */

public interface JyApi {


    /**
     * 登陆
     */
    @POST("/resource/members/login")
    Call<SuperBean<UserInfoBean>> login(@Body Map<String, String> map);

    /**
     * 第三方登录
     */
    @POST("/resource/user/userAuthLogin")
    Call<SuperBean<UserInfoBean>> authLogin(@Body Map<String, String> map);

    /**
     * 发送短信验证码
     *
     * @param phone
     * @return
     */
    @GET("/resource/sms/getCheckCode")
    Call<ErrorBean> getCheckCode(@Query("phone") String phone);

    /**
     * 注册
     *
     * @param map
     * @return
     */
    @POST("/resource/members/register")
    Call<SuperBean<UserInfoBean>> reister(@Body Map<String, String> map);

    /**
     * 忘记密码
     *
     * @param map
     * @return
     */
    @POST("/resource/user/forgetPwd")
    Call<ErrorBean> commitNewPassword(@Body Map<String, String> map);

    /**
     * 修改密码
     *
     * @param map
     * @return
     */
    @POST("/resource/user/accountSecurity")
    Call<ErrorBean> accountSafety(@Body Map<String, String> map);

    /**
     * 获取所有的标签
     *
     * @return
     */
    @GET("/resource/categoryGroup/findAll")
    Call<AllTagBean> getAllTags();

    /**
     * 获取岗位类型
     *
     * @return
     */
    @GET("/resource/categoryJobs/findJobsById")
    Call<JobTagBean> getJobType(@Query("id") String id);

    /**
     * 获取所在城市
     *
     * @return
     */
    @GET("/resource/categoryDistrict/getDistrict")
    Call<JobTagBean> getDistrict(@Query("id") String id);

    /**
     * 根据标签获取相应的数据
     *
     * @param bean
     * @return
     */
    @POST("/resource/category/getCategory")
    Call<CommonTagBean> getWorkAgeAndResume(@Body GetBaseTagBean bean);

    /**
     * 完善个人资料
     *
     * @return
     */
    @POST("/resource/membersInfo/save")
    Call<SuperBean<UserInfoBean>> perfectBaseInfo(@Body Map<String, String> map);

    /**
     * 完善企业资料
     *
     * @return
     */
    @POST("/resource/companyProfile/completeCompanyInfo")
    Call<SuperBean<UserInfoBean>> perfectEnterprefectInfo(@Body Map<String, String> map);


    /**
     * 上传公司营业执照图片
     *
     * @param file
     * @return
     */
    @Multipart
    @POST("/sys/uploadCertificate")
    Call<SuperBean<String>> uploadCertificateImage(@Part MultipartBody.Part file);

    /**
     * 上传公司logo图片
     *
     * @param file
     * @return
     */
    @Multipart
    @POST("/sys/uploadLogo")
    Call<SuperBean<String>> uploadLogoImage(@Part MultipartBody.Part file);

    /**
     * 上传个人头像图片
     *
     * @param file
     * @return
     */
    @Multipart
    @POST("/sys/uploadLiveCoverImage")
    Call<SuperBean<String>> uploadCover(@Part MultipartBody.Part file);

    /**
     * 上传个人头像图片
     *
     * @param file
     * @return
     */
    @Multipart
    @POST("/sys/uploadHeadPic")
    Call<SuperBean<String>> uploadFile(@Part MultipartBody.Part file);

    /**
     * 上传个人信息
     *
     * @param map
     * @return
     */
    @POST("/resource/user/userUpdate")
    Call<String> upLoadInfo(@Body Map<String, String> map);

    /**
     * 获取所有的职位
     *
     * @param map
     * @return
     */
    @POST("/resource/jobs/findJobList")
    Call<AllJobsBean> getAllJobs(@Body Map<String, String> map);

    /**
     * 筛选职位
     *
     * @param map
     * @return
     */
    @POST("/resource/jobs/jobSearch")
    Call<AllJobsBean> getFilterJobs(@Body Map<String, String> map);

    /**
     * 获取职位详情
     *
     * @param map
     * @return
     */
    @POST("/resource/jobs/jobDetail")
    Call<JobDetailsBean> getJobDetails(@Body Map<String, String> map);

    /**
     * 获取公司详情
     *
     * @param map
     * @return
     */
    @POST("/resource/companyProfile/companyProfileDetail")
    Call<CompanyDetailsBean> getCompanyDetails(@Body Map<String, String> map);

    /**
     * 获取直播列表
     *
     * @param map
     * @return
     */
    @POST("/resource/live/getLiveList")
    Call<LiveListBean> getLiveList(@Body Map<String, String> map);

    /**
     * 创建直播
     *
     * @param map
     * @return
     */
    @POST("/resource/live/createLive")
    Call<CreateLiveBean> createLive(@Body Map<String, String> map);

    /**
     * 添加工作经历
     *
     * @param map
     * @return
     */
    @POST("/resource/resumeWork/addWork")
    Call<SuperBean<String>> addWorkExperience(@Body Map<String, String> map);


}
