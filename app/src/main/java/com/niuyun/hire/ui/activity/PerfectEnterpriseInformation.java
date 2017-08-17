package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.adapter.CommonPerfectInfoTagAdapter;
import com.niuyun.hire.ui.adapter.JobPerfectInfoTagAdapter;
import com.niuyun.hire.ui.bean.CommonTagBean;
import com.niuyun.hire.ui.bean.CommonTagItemBean;
import com.niuyun.hire.ui.bean.GetBaseTagBean;
import com.niuyun.hire.ui.bean.JobTagBean;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.bean.UserInfoBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.ErrorMessageUtils;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.utils.UploadFile;
import com.niuyun.hire.utils.photoutils.TakeSimpleActivity;
import com.niuyun.hire.view.CircularImageView;
import com.niuyun.hire.view.MyDialog;
import com.niuyun.hire.view.TitleBar;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.niuyun.hire.base.Constants.resultCode_header_Camera;
import static com.niuyun.hire.base.Constants.resultCode_header_Photos;
import static com.niuyun.hire.base.Constants.resultCode_logo_Photos;

/**
 * Created by chen.zhiwei on 2017-7-31.
 */

public class PerfectEnterpriseInformation extends BaseActivity implements View.OnClickListener , TakePhoto.TakeResultListener,InvokeListener {
    @BindView(R.id.title_view)
    TitleBar titleView;
    @BindView(R.id.iv_header)
    CircularImageView ivHeader;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.iv_position_more)
    ImageView ivPositionMore;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.et_enterprise_name)
    EditText etEnterpriseName;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.iv_location_city_more)
    ImageView ivLocationCityMore;
    @BindView(R.id.tv_scale)
    TextView tvScale;
    @BindView(R.id.iv_enterprise_scale_more)
    ImageView ivEnterpriseScaleMore;
    @BindView(R.id.tv_nature)
    TextView tvNature;
    @BindView(R.id.iv_enterprise_nature_more)
    ImageView ivEnterpriseNatureMore;
    @BindView(R.id.et_address_details_name)
    EditText etAddressDetailsName;
    @BindView(R.id.iv_address_details_location)
    ImageView ivAddressDetailsLocation;
    @BindView(R.id.iv_address_details_more)
    ImageView ivAddressDetailsMore;
    @BindView(R.id.bt_next)
    Button bt_next;
    private Call<SuperBean<String>> upLoadImageCall;
    private String headimg;
    private String logoimg;
    List<String> list = new ArrayList<>();
    List<String> listLogo = new ArrayList<>();

    private int selectedType;//0为头像选择，1位logo选择

    private String intentionJobsId;//期望职位,每一级的id中间用 . 隔开
    private String intentionJobs;

    private String intentionJobsId1;
    private String intentionJobsId2;
    private String intentionJobsId3;


    private int jobStep = 0;
    private JobPerfectInfoTagAdapter adapter1;
    private JobPerfectInfoTagAdapter adapter2;
    private JobTagBean.DataBean cacheJobTag;
    private JobTagBean JobBean;

    private String intentionCityId;//所在城市,每一级的name中间用 / 隔开
    private String intentionCity;

    private String intentionCityCn1;
    private String intentionCityCn2;
    private String intentionCityCn3;
    private String intentionCityId1;
    private String intentionCityId2;
    private String intentionCityId3;
    private int cityStep = 0;
    private JobTagBean.DataBean cacheCityTag;
    private JobTagBean cityBean;
    private JobPerfectInfoTagAdapter adapter11;
    private JobPerfectInfoTagAdapter adapter22;

    private CommonTagBean commonTagBean;
    private String clickTag;
    private CommonTagItemBean cacheCommonTagBean;

    private String scale;//企业规模
    private String scaleCn;

    private String companyType;//企业性质
    private String companyTypeCn;

    private String uid;
    private String companyId;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_perfect_enterprise_information;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            uid = bundle.getString("uid");
            companyId = bundle.getString("companyId");
        }
        ivHeader.setOnClickListener(this);
        tvPosition.setOnClickListener(this);
        ivPositionMore.setOnClickListener(this);
        ivLogo.setOnClickListener(this);
        tvCity.setOnClickListener(this);
        ivLocationCityMore.setOnClickListener(this);
        tvScale.setOnClickListener(this);
        ivEnterpriseScaleMore.setOnClickListener(this);
        tvNature.setOnClickListener(this);
        ivEnterpriseNatureMore.setOnClickListener(this);
        ivAddressDetailsLocation.setOnClickListener(this);
        ivAddressDetailsMore.setOnClickListener(this);
        bt_next.setOnClickListener(this);
    }

    @Override
    public void loadData() {
        getTagItmes();
    }

    @Override
    public boolean isRegistEventBus() {
        return false;
    }

    @Override
    public void onMsgEvent(EventBusCenter eventBusCenter) {

    }

    @Override
    protected View isNeedLec() {
        return null;
    }

    private void initTitle() {

        titleView.setTitle("企业认证信息");
        titleView.setTitleColor(Color.WHITE);
        titleView.setLeftImageResource(R.mipmap.ic_title_back);
        titleView.setLeftText("返回");
        titleView.setLeftTextColor(Color.WHITE);
        titleView.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleView.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        titleView.setActionTextColor(Color.WHITE);
        titleView.addAction(new TitleBar.TextAction("下一步") {
            @Override
            public void performAction(View view) {
                if (!UIUtil.isFastDoubleClick()) {
                    if (checkData()) {
                        upLoadImage();
                    }
                }
            }
        });
//        titleView.setImmersive(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_header://个人头像
                selectedType = 0;
                photodialog(selectedType);
                break;
            case R.id.iv_logo://企业logo
                selectedType = 1;
                photodialog(selectedType);
                break;
            case R.id.tv_position://职位
            case R.id.iv_position_more:
                if (!UIUtil.isFastDoubleClick()) {
                    jobStep = 0;
                    if (JobBean == null) {
                        DialogUtils.showDialog(this, "加载...", false);
                        getJobData("0");
                    } else {
                        jobTagDialog(JobBean);
                    }
                }
                break;
            case R.id.tv_city://所在城市
            case R.id.iv_location_city_more:
                if (!UIUtil.isFastDoubleClick()) {
                    cityStep = 0;
                    if (cityBean == null) {
                        DialogUtils.showDialog(this, "加载...", false);
                        getCityData("0");
                    } else {
                        cityTagDialog(cityBean);
                    }
                }
                break;
            case R.id.tv_scale://企业规模
            case R.id.iv_enterprise_scale_more:
                clickTag = "QS_scale";
                if (!UIUtil.isFastDoubleClick()) {

                    click(clickTag);
                }
                break;
            case R.id.tv_nature://性质
            case R.id.iv_enterprise_nature_more:
                clickTag = "QS_company_type";
                if (!UIUtil.isFastDoubleClick()) {

                    click(clickTag);
                }
                break;
            case R.id.iv_address_details_more://详细地址
            case R.id.iv_address_details_location:

                break;
            case R.id.bt_next:
                //下一步
                if (!UIUtil.isFastDoubleClick()) {
                    if (checkData()) {
                        upLoadImage();
                    }
                }
                break;
        }
    }

    private boolean checkData() {
        if (list.size() <= 0) {
            UIUtil.showToast("请选择头像");
            return false;
        }
        if (TextUtils.isEmpty(etName.getText().toString())) {
            UIUtil.showToast("请输入名字");
            return false;
        }
        if (TextUtils.isEmpty(tvPosition.getText())) {
            UIUtil.showToast("请选择职位");
            return false;
        }
        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            UIUtil.showToast("请输入电话");
            return false;
        }
        if (listLogo.size() <= 0) {
            UIUtil.showToast("请选择Logo");
            return false;
        }
        if (TextUtils.isEmpty(etEnterpriseName.getText().toString())) {
            UIUtil.showToast("请输入企业名称");
            return false;
        }
        if (TextUtils.isEmpty(tvCity.getText())) {
            UIUtil.showToast("请选择城市");
            return false;
        }
        if (TextUtils.isEmpty(tvScale.getText())) {
            UIUtil.showToast("请选择规模");
            return false;
        }
        if (TextUtils.isEmpty(tvNature.getText())) {
            UIUtil.showToast("请选择性质");
            return false;
        }
        if (TextUtils.isEmpty(etAddressDetailsName.getText().toString())) {
            UIUtil.showToast("请输入详细地址");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        if (selectedType == 0) {
            list.clear();
            if (resultCode == resultCode_header_Camera) {
                //相机返回图片
//                Bundle b = data.getExtras();
//                String fileName = b.getString("picture");
                list = data.getStringArrayListExtra("picture");
            } else if (resultCode == resultCode_header_Photos) {
                // 图库中选择
                if (data == null || "".equals(data)) {
                    return;
                }
                list = data.getStringArrayListExtra("photo");
            }
        } else if (selectedType == 1) {
            listLogo.clear();
            if (resultCode == resultCode_header_Camera) {
                //相机返回图片
                listLogo=data.getStringArrayListExtra("picture");
            } else if (resultCode == resultCode_header_Photos) {
                // 图库中选择
                if (data == null || "".equals(data)) {
                    return;
                }
                listLogo = data.getStringArrayListExtra("photo");
            }
        }
//        headIsChange = true;
        if (list.size() > 0) {
            ImageLoadedrManager.getInstance().displayNoFilter(this, list.get(0), ivHeader);
        }
        if (listLogo.size() > 0) {
            ImageLoadedrManager.getInstance().displayNoFilter(this, listLogo.get(0), ivLogo);
        }
    }

    private void upLoadInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("address", etAddressDetailsName.getText().toString());
        map.put("avatars", headimg);
        map.put("companyId", companyId);
        map.put("companyname", etEnterpriseName.getText().toString());
        map.put("contact", etName.getText().toString());
        map.put("district", intentionCityId1);
        map.put("districtCn", intentionCity);
        map.put("logo", logoimg);
        map.put("nature", companyType);
        map.put("natureCn", companyTypeCn);
        map.put("scale", scale);
        map.put("scaleCn", scaleCn);
        map.put("sdistrict", intentionCityId2);
        map.put("tdistrict", intentionCityId3);
        map.put("telephone", etPhone.getText().toString());
        map.put("uid", uid);


        Call<SuperBean<UserInfoBean>> upLoadInfo = RestAdapterManager.getApi().perfectEnterprefectInfo(map);
        upLoadInfo.enqueue(new JyCallBack<SuperBean<UserInfoBean>>() {
            @Override
            public void onSuccess(Call<SuperBean<UserInfoBean>> call, Response<SuperBean<UserInfoBean>> response) {
                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    BaseContext.getInstance().setUserInfo(response.body().getData());
                    BaseContext.getInstance().updateUserInfo(response.body().getData());
//                    perfectSuccessDialog();
                    Intent intent = new Intent(PerfectEnterpriseInformation.this, EnterPriseCertificationActivity.class);
                    startActivity(intent);
                    EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.PERFECT_INFO_SUCCESS));
                    finish();
                } else {
                    try {
                        UIUtil.showToast(response.body().getMsg());
                    } catch (Exception e) {
                    }

                }
            }

            @Override
            public void onError(Call<SuperBean<UserInfoBean>> call, Throwable t) {
                DialogUtils.closeDialog();
            }

            @Override
            public void onError(Call<SuperBean<UserInfoBean>> call, Response<SuperBean<UserInfoBean>> response) {
                DialogUtils.closeDialog();
            }
        });
    }


    ///////////////////////////////////////////////////////////////////上传头像///////////////////////////////////////////////////////

    /**
     * 选择图片dialog
     */
    public void photodialog(final int type) {

        View view = View.inflate(PerfectEnterpriseInformation.this, R.layout.dialog_publish_photo, null);
        showdialog1(view);

        final TextView photo = (TextView) view.findViewById(R.id.photo);
        final TextView picture = (TextView) view.findViewById(R.id.picture);
        // 从图库中选择
        photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (photo.getText().toString().contains(getResources().getString(R.string.publish_photo))) {

                    Intent intent = new Intent(PerfectEnterpriseInformation.this, TakeSimpleActivity.class);
                    intent.putExtra("Type",1);
                    if (type == 0) {
                        //头像
                        startActivityForResult(intent, resultCode_header_Photos);
                    } else {
                        //企业logo
                        startActivityForResult(intent, resultCode_logo_Photos);

                    }
                    myDialog.dismiss();
                }

            }
        });
        // 拍照
        picture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (picture.getText().toString().contains(getResources().getString(R.string.publish_picture))) {
                    Intent intent = new Intent(PerfectEnterpriseInformation.this, TakeSimpleActivity.class);
                    intent.putExtra("Type",0);
                    if (type == 0) {
                        //头像
                        startActivityForResult(intent, resultCode_header_Camera);
                    } else {
                        //企业logo
                        startActivityForResult(intent, Constants.resultCode_logo_Camera);

                    }
                    myDialog.dismiss();
                }
            }
        });

    }

    private MyDialog myDialog;

    /**
     * 弹出dialog
     *
     * @param view
     */
    private void showdialog(View view) {

        myDialog = new MyDialog(this, 0, UIUtil.dip2px(this, 200), view, R.style.dialog);
        myDialog.show();
    }

    /**
     * 弹出dialog
     *
     * @param view
     */
    private void showdialog1(View view) {

        myDialog = new MyDialog(this, 0, 0, view, R.style.dialog);
        myDialog.show();
    }

    /**
     * 弹出dialog
     *
     * @param view
     */
    private void showdialog2(View view) {

        myDialog = new MyDialog(this, 0, 0, Gravity.CENTER_VERTICAL, view, R.style.dialog);
        myDialog.setCancelable(false);
        myDialog.show();
    }

    /**
     * 上传头像
     */
    private void upLoadImage() {
        DialogUtils.showDialog(this, "上传中", false);
        List<MultipartBody.Part> parts = UploadFile.filesToMultipartBody(list);
        upLoadImageCall = RestAdapterManager.getApi().uploadFile(parts.get(0));
        upLoadImageCall.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
//                UIUtil.showToast(response.body());
//                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    //上传图片成功
                    headimg = response.body().getData();
                    LogUtils.e(headimg);
//                        UIUtil.showToast("成功" + headimg);
                    upLoadLogoImage();
                } else {
                    UIUtil.showToast("上传头像失败");
                    DialogUtils.closeDialog();
                }
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast("上传头像失败");
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                try {
                    DialogUtils.closeDialog();
                    ErrorMessageUtils.taostErrorMessage(PerfectEnterpriseInformation.this, response.errorBody().string(), "");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 上传企业logo
     */
    private void upLoadLogoImage() {
        DialogUtils.showDialog(this, "上传中", false);
        List<MultipartBody.Part> parts = UploadFile.filesToMultipartBody(listLogo);
        upLoadImageCall = RestAdapterManager.getApi().uploadLogoImage(parts.get(0));
        upLoadImageCall.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
//                UIUtil.showToast(response.body());
//                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    //上传图片成功
                    logoimg = response.body().getData();
                    LogUtils.e(logoimg);
                    upLoadInfo();
                } else {
                    UIUtil.showToast("上传企业logo失败");
                    DialogUtils.closeDialog();
                }
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast("上传企业logo失败");
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                try {
                    DialogUtils.closeDialog();
                    ErrorMessageUtils.taostErrorMessage(PerfectEnterpriseInformation.this, response.errorBody().string(), "");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    //    --------------------------所在城市开始-----------------------------------------------------------------------

    /**
     * 选择城市dialog
     */
    public void cityTagDialog(JobTagBean tagBean) {
        if (tagBean == null) {
            return;
        }

        View view = View.inflate(PerfectEnterpriseInformation.this, R.layout.dialog_city_tag, null);
        showdialog(view);
        final RecyclerView tag1 = (RecyclerView) view.findViewById(R.id.tag1);
        final RecyclerView tag22 = (RecyclerView) view.findViewById(R.id.tag2);
        final RecyclerView tag33 = (RecyclerView) view.findViewById(R.id.tag3);
        TextView cancle = (TextView) view.findViewById(R.id.tv_cancel);
        TextView confirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tv_itle = (TextView) view.findViewById(R.id.tv_itle);
        tv_itle.setText("选择城市");
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cityStep != 3) {
                    UIUtil.showToast("请选择");
                    return;
                }
                setTvCityTag(cacheCityTag);
                myDialog.dismiss();
            }
        });
        tag1.setLayoutManager(new LinearLayoutManager(PerfectEnterpriseInformation.this));
        JobPerfectInfoTagAdapter adapter = new JobPerfectInfoTagAdapter(PerfectEnterpriseInformation.this, tagBean.getData());
        tag1.setAdapter(adapter);
        adapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {

                tag22.setLayoutManager(new LinearLayoutManager(PerfectEnterpriseInformation.this));
                adapter11 = new JobPerfectInfoTagAdapter(PerfectEnterpriseInformation.this);
                tag22.setAdapter(adapter11);
                adapter11.setCommonInterface(new RecyclerViewCommonInterface() {
                    @Override
                    public void onClick(Object bean) {
                        //点击了第二页的
                        tag33.setLayoutManager(new LinearLayoutManager(PerfectEnterpriseInformation.this));
                        adapter22 = new JobPerfectInfoTagAdapter(PerfectEnterpriseInformation.this);
                        tag33.setAdapter(adapter22);
                        adapter22.setCommonInterface(new RecyclerViewCommonInterface() {
                            @Override
                            public void onClick(Object bean) {
                                //点击了第三页
                                cityStep = 3;
                                intentionCityCn3 = ((JobTagBean.DataBean) bean).getCategoryname() + "";
                                intentionCityId3 = ((JobTagBean.DataBean) bean).getId() + "";
                                cacheCityTag = (JobTagBean.DataBean) bean;
                            }
                        });

                        cityStep = 2;
                        intentionCityCn2 = ((JobTagBean.DataBean) bean).getCategoryname() + "";
                        intentionCityId2 = ((JobTagBean.DataBean) bean).getId() + "";
                        getCityData(((JobTagBean.DataBean) bean).getId() + "");
                    }
                });

                //点击了第一页的
                cityStep = 1;
                intentionCityCn1 = ((JobTagBean.DataBean) bean).getCategoryname() + "";
                intentionCityId1 = ((JobTagBean.DataBean) bean).getId() + "";
                getCityData(((JobTagBean.DataBean) bean).getId() + "");
            }
        });

    }

    /**
     * 根据各级id获取城市信息
     *
     * @param id
     */
    private void getCityData(String id) {
        Call<JobTagBean> jobTagBeanCall = RestAdapterManager.getApi().getDistrict(id);
        jobTagBeanCall.enqueue(new JyCallBack<JobTagBean>() {
            @Override
            public void onSuccess(Call<JobTagBean> call, Response<JobTagBean> response) {
                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    if (cityStep == 0) {
                        cityBean = response.body();
                        cityTagDialog(cityBean);
                    } else if (cityStep == 1) {
                        adapter11.ClearData();
                        adapter11.addList(response.body().getData());
                    } else if (cityStep == 2) {
                        adapter22.ClearData();
                        adapter22.addList(response.body().getData());
                    }
                } else {
                    UIUtil.showToast("接口错误");
                }


            }

            @Override
            public void onError(Call<JobTagBean> call, Throwable t) {
                LogUtils.e(t.getMessage());
                DialogUtils.closeDialog();
                UIUtil.showToast("接口错误");
            }

            @Override
            public void onError(Call<JobTagBean> call, Response<JobTagBean> response) {
                DialogUtils.closeDialog();
            }
        });
    }

    private void setTvCityTag(JobTagBean.DataBean cacheTag) {
        if (cacheTag == null) {
            UIUtil.showToast("请选择");
            return;
        }
        intentionCity = intentionCityCn1 + "/" + intentionCityCn2 + "/" + intentionCityCn3;
        tvCity.setText(cacheTag.getCategoryname());
    }
    //    --------------------------所在城市结束-----------------------------------------------------------------------

    //    --------------------------期望职位开始-----------------------------------------------------------------------

    /**
     * 选择tag公告dialog
     */
    public void jobTagDialog(JobTagBean tagBean) {
        if (tagBean == null) {
            return;
        }

        View view = View.inflate(PerfectEnterpriseInformation.this, R.layout.dialog_job_tag, null);
        showdialog(view);
        final RecyclerView tag1 = (RecyclerView) view.findViewById(R.id.tag1);
        final RecyclerView tag2 = (RecyclerView) view.findViewById(R.id.tag2);
        final RecyclerView tag3 = (RecyclerView) view.findViewById(R.id.tag3);
        TextView cancle = (TextView) view.findViewById(R.id.tv_cancel);
        TextView confirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tv_itle = (TextView) view.findViewById(R.id.tv_itle);
        tv_itle.setText("期望职位");
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jobStep != 3) {
                    UIUtil.showToast("请选择");
                    return;
                }
                setJonTag(cacheJobTag);
                myDialog.dismiss();
            }
        });
        tag1.setLayoutManager(new LinearLayoutManager(PerfectEnterpriseInformation.this));
        JobPerfectInfoTagAdapter adapter = new JobPerfectInfoTagAdapter(PerfectEnterpriseInformation.this, tagBean.getData());
        tag1.setAdapter(adapter);
        adapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {

                tag2.setLayoutManager(new LinearLayoutManager(PerfectEnterpriseInformation.this));
                adapter1 = new JobPerfectInfoTagAdapter(PerfectEnterpriseInformation.this);
                tag2.setAdapter(adapter1);
                adapter1.setCommonInterface(new RecyclerViewCommonInterface() {
                    @Override
                    public void onClick(Object bean) {
                        //点击了第二页的
                        tag3.setLayoutManager(new LinearLayoutManager(PerfectEnterpriseInformation.this));
                        adapter2 = new JobPerfectInfoTagAdapter(PerfectEnterpriseInformation.this);
                        tag3.setAdapter(adapter2);
                        adapter2.setCommonInterface(new RecyclerViewCommonInterface() {
                            @Override
                            public void onClick(Object bean) {
                                //点击了第三页
                                jobStep = 3;
                                intentionJobsId3 = ((JobTagBean.DataBean) bean).getId() + "";
                                cacheJobTag = (JobTagBean.DataBean) bean;
                            }
                        });

                        jobStep = 2;
                        intentionJobsId2 = ((JobTagBean.DataBean) bean).getId() + "";
                        getJobData(((JobTagBean.DataBean) bean).getId() + "");
                    }
                });

                //点击了第一页的
                jobStep = 1;
                intentionJobsId1 = ((JobTagBean.DataBean) bean).getId() + "";
                getJobData(((JobTagBean.DataBean) bean).getId() + "");
            }
        });

    }

    /**
     * 根据各级id获取职位信息
     *
     * @param id
     */
    private void getJobData(String id) {
        Call<JobTagBean> jobTagBeanCall = RestAdapterManager.getApi().getJobType(id);
        jobTagBeanCall.enqueue(new JyCallBack<JobTagBean>() {
            @Override
            public void onSuccess(Call<JobTagBean> call, Response<JobTagBean> response) {
                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    if (jobStep == 0) {
                        JobBean = response.body();
                        jobTagDialog(JobBean);
                    } else if (jobStep == 1) {
                        adapter1.ClearData();
                        adapter1.addList(response.body().getData());
                    } else if (jobStep == 2) {
                        adapter2.ClearData();
                        adapter2.addList(response.body().getData());
                    }
                } else {
                    UIUtil.showToast("接口错误");
                }


            }

            @Override
            public void onError(Call<JobTagBean> call, Throwable t) {
                LogUtils.e(t.getMessage());
                DialogUtils.closeDialog();
                UIUtil.showToast("接口错误");
            }

            @Override
            public void onError(Call<JobTagBean> call, Response<JobTagBean> response) {

            }
        });
    }

    private void setJonTag(JobTagBean.DataBean cacheJobTag) {
        if (cacheJobTag == null) {
            DialogUtils.closeDialog();
            UIUtil.showToast("请选择");
            return;
        }
        intentionJobsId = intentionJobsId1 + "." + intentionJobsId2 + "." + intentionJobsId3;
        intentionJobs = cacheJobTag.getCategoryname();
        tvPosition.setText(cacheJobTag.getCategoryname());
    }
//-----------------------------------------------------------------------期望职位结束----------------------------------------------------------

    /**
     * 统一处理各个类型的点击事件
     *
     * @param tag
     */
    private void click(String tag) {
        if (commonTagBean == null) {
            getTagItmes();

        } else {
            switch (tag) {
                case "QS_scale":
                    commonDialog(commonTagBean.getData().getQS_scale());
                    break;
                case "QS_company_type":
                    commonDialog(commonTagBean.getData().getQS_company_type());
                    break;
                case "QS_jobs_nature":
                    commonDialog(commonTagBean.getData().getQS_jobtag());
                    break;
                case "QS_wage":
                    commonDialog(commonTagBean.getData().getQS_wage());
                    break;
            }

        }
    }

    /**
     * 选择tag公告dialog
     */
    public void commonDialog(List<CommonTagItemBean> tagBean) {
        if (tagBean == null || tagBean.size() <= 0) {
            return;
        }

        View view = View.inflate(PerfectEnterpriseInformation.this, R.layout.dialog_common_tag, null);
        showdialog(view);
        RecyclerView tag_recyclerview = (RecyclerView) view.findViewById(R.id.tag_recyclerview);
        TextView cancle = (TextView) view.findViewById(R.id.tv_cancel);
        TextView confirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        switch (clickTag) {
            case "QS_company_type":
                tv_title.setText("企业性质");
                break;
            case "QS_scale":
                tv_title.setText("企业规模");
                break;
//            case "QS_wage":
//                tv_title.setText("期望薪资");
//                break;
        }
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCommitValue(cacheCommonTagBean);
                myDialog.dismiss();
            }
        });
        tag_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        CommonPerfectInfoTagAdapter adapter = new CommonPerfectInfoTagAdapter(this, tagBean);
        tag_recyclerview.setAdapter(adapter);
        adapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {
                cacheCommonTagBean = (CommonTagItemBean) bean;
            }
        });
    }

    /**
     * 点击确定设置相应的数据
     *
     * @param tagBean
     */
    private void setCommitValue(CommonTagItemBean tagBean) {
        if (tagBean == null) {
            UIUtil.showToast("请重新选择");
            return;
        }
        switch (clickTag) {
            case "QS_scale":
                scale = tagBean.getCId() + "";
                scaleCn = tagBean.getCName();
                tvScale.setText(tagBean.getCName());
                break;
            case "QS_company_type":
                companyType = tagBean.getCId() + "";
                companyTypeCn = tagBean.getCName();
                tvNature.setText(tagBean.getCName());
                break;
            case "QS_jobs_nature":
//                tv_job.setText(tagBean.getCName());
                break;
            case "QS_wage":
//                tv_wage.setText(tagBean.getCName());
//                wage = tagBean.getCId() + "";
//                wageCn = tagBean.getCName();
                break;
        }
        cacheCommonTagBean = null;
    }

    /**
     * 根据对应的分类id获取对应的数据
     */

    private void getTagItmes() {
        List<String> list = new ArrayList<>();
        list.add("QS_scale");
        list.add("QS_company_type");
//        list.add("QS_wage");
        GetBaseTagBean tagBean = new GetBaseTagBean();
        tagBean.setAlias(list);
        Call<CommonTagBean> commonTagBeanCall = RestAdapterManager.getApi().getWorkAgeAndResume(tagBean);
        commonTagBeanCall.enqueue(new JyCallBack<CommonTagBean>() {
            @Override
            public void onSuccess(Call<CommonTagBean> call, Response<CommonTagBean> response) {
                LogUtils.e(response.body().getMsg());
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    commonTagBean = response.body();
//                    commonDialog(response.body());
                } else {
                    UIUtil.showToast(response.body().getMsg());
                }
            }

            @Override
            public void onError(Call<CommonTagBean> call, Throwable t) {
                LogUtils.e(t.getMessage());
            }

            @Override
            public void onError(Call<CommonTagBean> call, Response<CommonTagBean> response) {

            }
        });
    }

    @Override
    public void takeSuccess(TResult result) {

    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        return null;
    }
}
