package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.adapter.CommonPerfectInfoTagAdapter;
import com.niuyun.hire.ui.adapter.JobPerfectInfoTagAdapter;
import com.niuyun.hire.ui.adapter.JobPerfectInfoTagAdapter1;
import com.niuyun.hire.ui.adapter.JobPerfectInfoTagAdapter2;
import com.niuyun.hire.ui.bean.CommonTagBean;
import com.niuyun.hire.ui.bean.CommonTagItemBean;
import com.niuyun.hire.ui.bean.GetBaseTagBean;
import com.niuyun.hire.ui.bean.JobTagBean;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.ErrorMessageUtils;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.utils.UploadFile;
import com.niuyun.hire.utils.photoTool.PhotoPicActivity;
import com.niuyun.hire.utils.photoTool.TakingPicturesActivity;
import com.niuyun.hire.view.CircularImageView;
import com.niuyun.hire.view.MyDialog;
import com.niuyun.hire.view.TitleBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-7-31.
 */

public class PerfectEnterpriseInformation extends BaseActivity implements View.OnClickListener {
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
    private Call<SuperBean<String>> upLoadImageCall;
    private String headimg;
    private String logoimg;
    List<String> list = new ArrayList<String>();
    List<String> listLogo = new ArrayList<String>();
    //个人头像
    private static final int resultCode_header_Photos = 10;//跳转到相册
    private static final int resultCode_header_Camera = 11;//跳转到相机

    //企业logo
    private static final int resultCode_logo_Photos = 12;//跳转到相册
    private static final int resultCode_logo_Camera = 13;//跳转到相机

    private int selectedType;//0为头像选择，1位logo选择

    private String intentionJobsId;//期望职位,每一级的id中间用 . 隔开
    private String intentionJobs;

    private String intentionJobsId1;
    private String intentionJobsId2;
    private String intentionJobsId3;

    private int jobStep = 0;
    private JobPerfectInfoTagAdapter1 adapter1;
    private JobPerfectInfoTagAdapter2 adapter2;
    private JobTagBean.DataBean cacheJobTag;

    private CommonTagBean commonTagBean;
    private String clickTag;
    private CommonTagItemBean cacheCommonTagBean;

    private String scale;//企业规模
    private String scaleCn;

    private String companyType;//企业性质
    private String companyTypeCn;


    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_perfect_enterprise_information;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
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
                startActivity(new Intent(PerfectEnterpriseInformation.this, EnterPriseCertificationActivity.class));
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
                    getJobData("0");
                }
                break;
            case R.id.tv_city://所在城市
            case R.id.iv_location_city_more:

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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        list.clear();
        listLogo.clear();

        if (selectedType == 0) {
            if (resultCode == resultCode_header_Camera) {
                //相机返回图片
                Bundle b = data.getExtras();
                String fileName = b.getString("picture");
                list.add(fileName);
            } else if (resultCode == resultCode_header_Photos) {
                // 图库中选择
                if (data == null || "".equals(data)) {
                    return;
                }
                list = data.getExtras().getStringArrayList("photo");
            }
        } else if (selectedType == 1) {
            if (resultCode == resultCode_header_Camera) {
                //相机返回图片
                Bundle b = data.getExtras();
                String fileName = b.getString("picture");
                listLogo.add(fileName);
            } else if (resultCode == resultCode_header_Photos) {
                // 图库中选择
                if (data == null || "".equals(data)) {
                    return;
                }
                listLogo = data.getExtras().getStringArrayList("photo");
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

                    Intent intent = new Intent(PerfectEnterpriseInformation.this, PhotoPicActivity.class);
                    intent.putExtra("max", 1);
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
                    Intent intent = new Intent(PerfectEnterpriseInformation.this, TakingPicturesActivity.class);
                    if (type == 0) {
                        //头像
                        startActivityForResult(intent, resultCode_header_Camera);
                    } else {
                        //企业logo
                        startActivityForResult(intent, resultCode_logo_Camera);

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
        upLoadImageCall = RestAdapterManager.getApi().uploadFile(parts.get(0));
        upLoadImageCall.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
//                UIUtil.showToast(response.body());
//                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    //上传图片成功
                    logoimg = response.body().getData();
                    LogUtils.e(logoimg);
//                    upLoadInfo();
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
                adapter1 = new JobPerfectInfoTagAdapter1(PerfectEnterpriseInformation.this);
                tag2.setAdapter(adapter1);
                adapter1.setCommonInterface(new RecyclerViewCommonInterface() {
                    @Override
                    public void onClick(Object bean) {
                        //点击了第二页的
                        tag3.setLayoutManager(new LinearLayoutManager(PerfectEnterpriseInformation.this));
                        adapter2 = new JobPerfectInfoTagAdapter2(PerfectEnterpriseInformation.this);
                        tag3.setAdapter(adapter2);
                        adapter2.setCommonInterface(new RecyclerViewCommonInterface() {
                            @Override
                            public void onClick(Object bean) {
                                //点击了第三页
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

    private void getJobData(String id) {
        Call<JobTagBean> jobTagBeanCall = RestAdapterManager.getApi().getJobType(id);
        jobTagBeanCall.enqueue(new JyCallBack<JobTagBean>() {
            @Override
            public void onSuccess(Call<JobTagBean> call, Response<JobTagBean> response) {
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    if (jobStep == 0) {
                        jobTagDialog(response.body());
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
                UIUtil.showToast("接口错误");
            }

            @Override
            public void onError(Call<JobTagBean> call, Response<JobTagBean> response) {

            }
        });
    }

    private void setJonTag(JobTagBean.DataBean cacheJobTag) {
        if (cacheJobTag == null) {
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
}
