package com.niuyun.hire.api;


import com.niuyun.hire.bean.ErrorBean;
import com.niuyun.hire.ui.bean.BuyOrderListItemBean;
import com.niuyun.hire.ui.bean.OrderDetailBean;
import com.niuyun.hire.ui.bean.GoodsFilterBean;
import com.niuyun.hire.ui.bean.GoodsListBean;
import com.niuyun.hire.ui.bean.MemberRankBean;
import com.niuyun.hire.ui.bean.ShoppingAddressListItemBean;
import com.niuyun.hire.ui.bean.ShopsFilterBean;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.bean.UserInfoBean;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


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
    Call<String> reister(@Body Map<String, String> map);

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
     * 增加收货地址
     *
     * @param map
     * @return
     */
    @POST("/resource/deliveryAddress/addAddress")
    Call<String> addAddress(@Body Map<String, String> map);

    /**
     * 编辑收货地址
     *
     * @param map
     * @return
     */
    @POST("/resource/deliveryAddress/updateDeliveryAddress")
    Call<String> editAddress(@Body Map<String, String> map);

    /**
     * 删除收货地址
     *
     * @param id
     * @return
     */
    @GET("/resource/deliveryAddress/deleteById")
    Call<ErrorBean> deleteAddress(@Query("id") String id);

    /**
     * 获取地址列表
     *
     * @param userid
     * @return
     */
    @GET("/resource/deliveryAddress/getAllDeliveryAddress")
    Call<SuperBean<List<ShoppingAddressListItemBean>>> getAddressList(@Query("userid") String userid);


    /**
     * 获取商品类型筛选条件
     *
     * @return
     */
    @GET("/resource/goodstype/appGetAllList")
    Call<SuperBean<List<GoodsFilterBean>>> getAllFilterType();

    /**
     * 获取商品门店筛选条件
     *
     * @return
     */
    @POST("/resource/shop/appGetAllList")
    Call<SuperBean<List<ShopsFilterBean>>> getAllFilterShops();

    /**
     * 获取首页列表
     *
     * @param map
     * @return
     */
    @POST("/resource/goods/goodsAllList")
    Call<SuperBean<List<GoodsListBean>>> getGoodsList(@Body Map<String, String> map);

    /**
     * 实名认证
     *
     * @param map
     * @return
     */
    @POST("/resource/user/userAuth")
    Call<String> commitRealName(@Body Map<String, String> map);

    /**
     * 获取会员登记信息
     *
     * @return
     */
    @GET("/resource/viptype/appGetAllList")
    Call<SuperBean<List<MemberRankBean>>> getMemberRank();

    /**
     * 广告位数据
     */
    @GET("/resource/goods/adList")
    Call<SuperBean<List<GoodsListBean>>> getAdList(@Query("userId") String userId);

    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    @Multipart
    @POST("/sys/uploadFile")
    Call<String> uploadFile(@Part MultipartBody.Part file);

    /**
     * 上传个人信息
     *
     * @param map
     * @return
     */
    @POST("/resource/user/userUpdate")
    Call<String> upLoadInfo(@Body Map<String, String> map);

    /**
     * 获取商品详细信息
     *
     * @param id
     * @param userId
     * @return
     */
    @GET("/resource/goods/appGetDetail")
    Call<SuperBean<GoodsListBean>> getGoodsDetail(@Query("id") String id, @Query("userId") String userId);

    /**
     * 提交购买订单
     *
     * @param map
     * @return
     */
    @POST("/resource/buyorder/insert")
    Call<SuperBean<String>> getCommitOrder(@Body Map<String, String> map);

    /**
     * 获取购买订单列表
     *
     * @param map
     * @return
     */
    @POST("/resource/buyorder/appGetAllList")
    Call<SuperBean<BuyOrderListItemBean>> getBuyOrderList(@Body Map<String, String> map);

    /**
     * 获取支付加密后的信息
     *
     * @param map
     * @return
     */
    @POST("/resource/pay/payConfirm")
    Call<SuperBean<String>> getRsaOrderInfo(@Body Map<String, String> map);

    /**
     * 获取订单详情
     *
     * @param id
     * @return
     */
    @GET("/resource/buyorder/appGetBuyOrderDetail")
    Call<SuperBean<OrderDetailBean>> getOrderDetails(@Query("id") String id);


    /**
     * 押金支付获取订单号
     *
     * @param map
     * @return
     */
    @POST("/resource/vipDeposit/insert")
    Call<SuperBean<String>> getDepositOrder(@Body Map<String, String> map);


    /**
     * 提交租赁订单
     *
     * @param map
     * @return
     */
    @POST("/resource/zuorder/insert")
    Call<SuperBean<String>> getRentOrder(@Body Map<String, String> map);


    /**
     * 获取租赁订单列表
     *
     * @param map
     * @return
     */
    @POST("/resource/zuorder/appGetAllList")
    Call<SuperBean<BuyOrderListItemBean>> getRentOrderList(@Body Map<String, String> map);
}
