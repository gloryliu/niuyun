package com.niuyun.hire.api;


import com.niuyun.hire.bean.ErrorBean;
import com.niuyun.hire.ui.bean.AllJobsBean;
import com.niuyun.hire.ui.bean.AllTagBean;
import com.niuyun.hire.ui.bean.AroundResultBean;
import com.niuyun.hire.ui.bean.AttentionMeBean;
import com.niuyun.hire.ui.bean.CertificationBean;
import com.niuyun.hire.ui.bean.CommonTagBean;
import com.niuyun.hire.ui.bean.CompanyDetailsBean;
import com.niuyun.hire.ui.bean.DianboListBean;
import com.niuyun.hire.ui.bean.EnterpriseFindPersonBean;
import com.niuyun.hire.ui.bean.EnterprisePublishedPositionBean;
import com.niuyun.hire.ui.bean.GetBaseTagBean;
import com.niuyun.hire.ui.bean.HotSearchBean;
import com.niuyun.hire.ui.bean.JobDetailsBean;
import com.niuyun.hire.ui.bean.JobTagBean;
import com.niuyun.hire.ui.bean.LiveListBean;
import com.niuyun.hire.ui.bean.MyAttentionListBean;
import com.niuyun.hire.ui.bean.PersonBaseInfo;
import com.niuyun.hire.ui.bean.PositionIntentBean;
import com.niuyun.hire.ui.bean.PreviewResumeBean;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.bean.UpdateBean;
import com.niuyun.hire.ui.bean.UserInfoBean;

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
     * 登陆
     */
    @GET("/resource/members/getMemberInfo")
    Call<SuperBean<UserInfoBean>> getUser(@Query("uid") String uid);

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
    @POST("/resource/members/updatePwdByPhone")
    Call<ErrorBean> commitNewPassword(@Body Map<String, String> map);

    /**
     * 修改密码
     *
     * @param map
     * @return
     */
    @POST("/resource/members/updatePwd")
    Call<ErrorBean> updatePassword(@Body Map<String, String> map);

    /**
     * 修改电话号码
     *
     * @param map
     * @return
     */
    @POST("/resource/members/updateMobile")
    Call<ErrorBean> updatePhne(@Body Map<String, String> map);

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
     * 完善企业个人资料
     *
     * @return
     */
    @POST("/resource/companyProfile/updateCompanyPersonInfo")
    Call<SuperBean<String>> editEnterpreInfo(@Body Map<String, String> map);

    /**
     * 获取企业认证信息
     *
     * @return
     */
    @GET("/resource/companyProfile/getCertificateInfo")
    Call<CertificationBean> getEnterpriseCertification(@Query("companyId") String companyId);

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
     * 提交企业认证图片
     *
     * @param map
     * @return
     */
    @POST("/resource/companyProfile/certificateCompany")
    Call<String> bindCenterpriseImage(@Body Map<String, String> map);

    /**
     * 上传个人信息
     *
     * @param map
     * @return
     */
    @POST("/resource/membersInfo/update")
    Call<String> upLoadInfo(@Body Map<String, String> map);

    /**
     * 公司详情页中获取所有的职位
     *
     * @param map
     * @return
     */
    @POST("/resource/jobs/findJobList")
    Call<AllJobsBean> getCompanyJobs(@Body Map<String, String> map);

    /**
     * 获取所有的职位
     *
     * @param map
     * @return
     */
    @POST("/resource/jobs/jobSearch")
    Call<AllJobsBean> getAllJobs(@Body Map<String, String> map);

    /**
     * 获取附近
     *
     * @param map
     * @return
     */
    @POST("/resource/nearby/getNearby")
    Call<AroundResultBean> getAround(@Body Map<String, String> map);

    /**
     * 更新个人位置信息
     *
     * @param map
     * @return
     */
    @POST("/resource/members/updateLocation")
    Call<SuperBean<String>> updateLocation(@Body Map<String, String> map);

    /**
     * 根据意向筛选职位
     *
     * @param map
     * @return
     */
    @POST("/resource/jobs/indexFindJobList")
    Call<AllJobsBean> getFilterJobs(@Body Map<String, String> map);

    /**
     * 修改在职状态
     *
     * @param map
     * @return
     */
    @POST("/resource/resume/updateCurrent")
    Call<SuperBean<String>> setCurrentState(@Body Map<String, String> map);

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
     * 获取点播列表
     *
     * @param map
     * @return
     */
    @POST("/resource/tencentYun/getAllVideoList")
    Call<DianboListBean> geDianboList(@Body Map<String, String> map);

    /**
     * 创建直播
     *
     * @param map
     * @return
     */
//    @POST("/resource/live/createLive")
//    Call<CreateLiveBean> createLive(@Body Map<String, String> map);

    /**
     * 添加工作经历
     *
     * @param map
     * @return
     */
    @POST("/resource/resumeWork/addWork")
    Call<SuperBean<String>> addWorkExperience(@Body Map<String, String> map);

    /**
     * 编辑工作经历
     *
     * @param map
     * @return
     */
    @POST("/resource/resumeWork/updateWork")
    Call<SuperBean<String>> editWorkExperience(@Body Map<String, String> map);

    /**
     * 删除工作经历
     *
     * @param
     * @return
     */
    @GET("/resource/resumeWork/deleteWork")
    Call<SuperBean<String>> deleteWorkExperience(@Query("id") String id);

    /**
     * 编辑教育经历
     *
     * @param map
     * @return
     */
    @POST("/resource/resumeEducation/updateEducation")
    Call<SuperBean<String>> editEducation(@Body Map<String, String> map);

    /**
     * 删除教育经历
     *
     * @param
     * @return
     */
    @GET("/resource/resumeEducation/deleteEducation")
    Call<SuperBean<String>> deleteEducation(@Query("id") String id);

    /**
     * 添加教育
     *
     * @param map
     * @return
     */
    @POST("/resource/resumeEducation/addEducation")
    Call<SuperBean<String>> addEducation(@Body Map<String, String> map);

    /**
     * 添加描述
     *
     * @param map
     * @return
     */
    @POST("/resource/resume/addSpecialty")
    Call<SuperBean<String>> addEvaluation(@Body Map<String, String> map);

    /**
     * 预览简历数据
     */
    @GET("/resource/resume/resumePreView")
    Call<PreviewResumeBean> previewResume(@Query("uid") String uid,@Query("companyId") String companyId);

    /**
     * 获取个人信息
     */
    @GET("/resource/membersInfo/getInfo")
    Call<PersonBaseInfo> getPersonInfo(@Query("uid") String uid);

    /**
     * 获取求职意向列表
     */
    @GET("/resource/membersIntention/findIntentions")
    Call<PositionIntentBean> gePositionIntentList(@Query("uid") String uid);


    /**
     * 关注
     *
     * @param map
     * @return
     */
    @POST("/resource/jobs/addFollow")
    Call<SuperBean<String>> addAttention(@Body Map<String, String> map);

    /**
     * 关注
     *
     * @param map
     * @return
     */
    @POST("/resource/jobs/getFollows")
    Call<MyAttentionListBean> getMyAttention(@Body Map<String, String> map);
    /**
     * 谁看过我
     *
     * @param uid
     * @return
     */
    @GET("/resource/lookme/getAllLookMe")
    Call<AttentionMeBean> getAttentionMe(@Query("uid") String uid);

    /**
     * 新增求职意向
     *
     * @param map
     * @return
     */
    @POST("/resource/membersIntention/addIntention")
    Call<SuperBean<String>> commitPositionIntent(@Body Map<String, String> map);

    /**
     * 编辑求职意向
     *
     * @param map
     * @return
     */
    @POST("/resource/membersIntention/updateIntention")
    Call<SuperBean<String>> editPositionIntent(@Body Map<String, String> map);


    /**
     * 删除求职意向
     *
     * @param id
     * @return
     */
    @GET("/resource/membersIntention/deleteIntention")
    Call<SuperBean<String>> deletePositionIntent(@Query("id") String id);

    /**
     * 上传视频建立vid
     *
     * @param map
     * @return
     */
    @POST("/resource/members/addVideo")
    Call<SuperBean<String>> commitVideoVid(@Body Map<String, String> map);

    /**
     * 获取热搜标签
     */
    @GET("/resource/searchKeyword/getKeywordList")
    Call<HotSearchBean> getHotSearchTag();

    /**
     * 企业发布职位
     *
     * @return
     */
    @POST("/resource/jobs/publishJobs")
    Call<SuperBean<String>> publishPosition(@Body Map<String, String> map);

    /**
     * 编辑企业发布的职位
     *
     * @return
     */
    @POST("/resource/jobs/updatePublishJobs")
    Call<SuperBean<String>> editPosition(@Body Map<String, String> map);

    /**
     * 企业发布的职位列表
     *
     * @param map
     * @return
     */
    @POST("/resource/jobs/findMyPublishJobList")
    Call<EnterprisePublishedPositionBean> getMyPublishedPosition(@Body Map<String, String> map);

    /**
     * 企业发布的职位列表
     *
     * @param map
     * @return
     */
    @POST("/resource/resume/getResumeList")
    Call<EnterpriseFindPersonBean> getPersonon(@Body Map<String, String> map);

    /**
     * 删除企业发布的职位
     */
    @GET("/resource/jobs/deleteJobs")
    Call<SuperBean<String>> deleteEnterprisePosition(@Query("id") String id);

    /**
     * 获取腾讯视频上传所需签名
     */
    @GET("/resource/tencentYun/getAppUploadVideoSign")
    Call<SuperBean<String>> getVideoUploadSign();
    /**
     * app检查升级
     *
     * @param platformType 平台Android
     * @param alias        app名称
     * @param version      本地版本
     * @return
     */
    @GET("/resource/appVersion/queryAppVersion")
    Call<SuperBean<UpdateBean>> checkVersion(@Query("platformType") String platformType,
                                             @Query("appAlias") String alias,
                                             @Query("version") String version);
}
