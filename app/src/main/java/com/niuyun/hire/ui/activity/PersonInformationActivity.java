package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.niuyun.hire.ui.bean.PersonBaseInfo;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.ErrorMessageUtils;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.utils.UploadFile;
import com.niuyun.hire.utils.photoutils.TakeSimpleActivity;
import com.niuyun.hire.utils.timepicker.TimePickerView;
import com.niuyun.hire.view.CircularImageView;
import com.niuyun.hire.view.MyDialog;
import com.niuyun.hire.view.TitleBar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-6-22.
 */

public class PersonInformationActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_view)
    TitleBar title_view;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.tv_birthday)
    TextView tv_birthday;
    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.tv_education)
    TextView tv_education;
    @BindView(R.id.tv_work_time)
    TextView tv_work_time;
    @BindView(R.id.bt_commit)
    Button bt_commit;
    @BindView(R.id.iv_head)
    CircularImageView iv_head;
    private Call<SuperBean<String>> upLoadImageCall;
    private Call<String> upLoadInfoCall;
    private String headimg;
    private String name;
    private String sex;
    private String birthday;

    List<String> list = new ArrayList<String>();

    private static final int resultCode_Photos = 10;//跳转到相册
    private static final int resultCode_Camera = 11;//跳转到相机

    private PersonBaseInfo baseInfo;
    private CommonTagBean commonTagBean;
    private String clickTag;
    private CommonTagItemBean cacheCommonTagBean;
    private String education;//学历
    private String educationCn;

    private String experience;//经验
    private String experienceCn;


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

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_person_information;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        bt_commit.setOnClickListener(this);
        tv_sex.setOnClickListener(this);
        tv_birthday.setOnClickListener(this);
        iv_head.setOnClickListener(this);
        tv_city.setOnClickListener(this);
        tv_work_time.setOnClickListener(this);
        tv_education.setOnClickListener(this);
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                name = et_name.getText().toString();
            }
        });
    }

    @Override
    public void loadData() {
        getBaseInfo();
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

    private void upLoadInfo() {

//        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(sex) && TextUtils.isEmpty(birthday) && TextUtils.isEmpty(headimg)) {
//            UIUtil.showToast("信息没有更改");
//            return;
//        }
        DialogUtils.showDialog(this, "上传中", false);
        Map<String, String> map = new HashMap<>();
        map.put("uid", BaseContext.getInstance().getUserInfo().uid + "");
        map.put("realname", et_name.getText().toString());
        if (!TextUtils.isEmpty(sex)) {
            if ("男".equals(sex)) {
                map.put("sex", "1");
            } else {
                map.put("sex", "0");
            }
            map.put("sexCn", sex);

        } else {
            map.put("sex", baseInfo.getData().getSex() + "");
            map.put("sexCn", baseInfo.getData().getSexCn());
        }
        if (!TextUtils.isEmpty(birthday)) {

            map.put("birthday", birthday);
        } else {
            map.put("birthday", baseInfo.getData().getBirthday());
        }
        if (!TextUtils.isEmpty(headimg)) {
            map.put("avatars", headimg);
        } else {
            map.put("avatars", baseInfo.getData().getAvatars());
        }
        if (!TextUtils.isEmpty(education)) {
            map.put("education", education);
            map.put("educationCn", educationCn);
        } else {
            map.put("education", baseInfo.getData().getEducation()+"");
            map.put("educationCn", baseInfo.getData().getEducationCn());
        }
        if (!TextUtils.isEmpty(experience)) {
            map.put("experience", experience);
            map.put("experienceCn", experienceCn);
        } else {
            map.put("experience", baseInfo.getData().getExperience()+"");
            map.put("experienceCn", baseInfo.getData().getExperienceCn());
        }
        if (!TextUtils.isEmpty(intentionCity)) {
            map.put("residence", intentionCity);
        } else {
            map.put("residence", baseInfo.getData().getResidence());
        }
        map.put("email", et_email.getText().toString());
        map.put("phone", et_phone.getText().toString());


        upLoadInfoCall = RestAdapterManager.getApi().upLoadInfo(map);
        upLoadInfoCall.enqueue(new JyCallBack<String>() {
            @Override
            public void onSuccess(Call<String> call, Response<String> response) {
                DialogUtils.closeDialog();
                if (response != null && response.body() != null) {
                    if (response.body().contains("1000")) {
                        UIUtil.showToast("修改成功");
//                        EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.LOGIN_SUCCESS));
                    } else UIUtil.showToast("修改失败");
                } else {
                    UIUtil.showToast("修改失败");
                }


            }

            @Override
            public void onError(Call<String> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast("修改失败");
            }

            @Override
            public void onError(Call<String> call, Response<String> response) {
                UIUtil.showToast("修改失败");
                DialogUtils.closeDialog();
            }
        });
    }

    private void upLoadImage() {
        DialogUtils.showDialog(this, "上传中", false);
        List<MultipartBody.Part> parts = UploadFile.filesToMultipartBody(list);
        upLoadImageCall = RestAdapterManager.getApi().uploadFile(parts.get(0));
        upLoadImageCall.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
//                UIUtil.showToast(response.body());
                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    if (!TextUtils.isEmpty(response.body().getData())) {
                        //上传图片成功
                        headimg = response.body().getData();
                        upLoadInfo();
                    }
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
                    ErrorMessageUtils.taostErrorMessage(PersonInformationActivity.this, response.errorBody().string(), "");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        list.clear();
        if (resultCode == resultCode_Camera) {
            //相机返回图片
            Bundle b = data.getExtras();
            String fileName = b.getString("picture");
            list.add(fileName);
        } else if (resultCode == resultCode_Photos) {
            // 图库中选择
            if (data == null || "".equals(data)) {
                return;
            }
            list = data.getExtras().getStringArrayList("photo");
            LogUtils.e("image路径--" + list.get(0));
        }
//        headIsChange = true;
        ImageLoadedrManager.getInstance().displayNoFilter(this, list.get(0), iv_head);
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        title_view.setTitle("个人信息");
        title_view.setTitleColor(Color.WHITE);
        title_view.setLeftImageResource(R.mipmap.ic_title_back);
        title_view.setLeftText("返回");
        title_view.setLeftTextColor(Color.WHITE);
        title_view.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title_view.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        title_view.setActionTextColor(Color.WHITE);
        title_view.addAction(new TitleBar.TextAction("保存") {
            @Override
            public void performAction(View view) {
                if (list.size() > 0) {
                    if (!TextUtils.isEmpty(headimg)) {
                        upLoadInfo();
                    } else
                        upLoadImage();
                } else {
                    upLoadInfo();
                }
            }
        });
        title_view.setImmersive(true);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_commit:
                //保存
                if (list.size() > 0) {
                    if (!TextUtils.isEmpty(headimg)) {
                        upLoadInfo();
                    } else
                        upLoadImage();
                } else {
                    upLoadInfo();
                }
                break;
            case R.id.tv_sex:
//性别
                sexDialog();
                break;
            case R.id.tv_birthday:
                //出生年月日
                DialogUtils.showTimePcikerDialog(this, new TimePickerView.OnTimeSelectListener() {

                    @Override
                    public void onTimeSelect(Date date) {
                        if (date != null) {
                            birthday = getTime(date);
                            tv_birthday.setText(birthday);
                        }
                    }
                });
                break;
            case R.id.iv_head:
                photodialog();
                break;
            case R.id.tv_education:
                //学历
                clickTag = "QS_education";
                if (!UIUtil.isFastDoubleClick()) {

                    click(clickTag);
                }
                break;
            case R.id.tv_work_time:
                //学历
                clickTag = "QS_experience";
                if (!UIUtil.isFastDoubleClick()) {

                    click(clickTag);
                }
                break;
            case R.id.tv_city:
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
        }
    }
//    --------------------------所在城市开始-----------------------------------------------------------------------

    /**
     * 选择城市dialog
     */
    public void cityTagDialog(JobTagBean tagBean) {
        if (tagBean == null) {
            return;
        }

        View view = View.inflate(PersonInformationActivity.this, R.layout.dialog_city_tag, null);
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
        tag1.setLayoutManager(new LinearLayoutManager(PersonInformationActivity.this));
        JobPerfectInfoTagAdapter adapter = new JobPerfectInfoTagAdapter(PersonInformationActivity.this, tagBean.getData());
        tag1.setAdapter(adapter);
        adapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {

                tag22.setLayoutManager(new LinearLayoutManager(PersonInformationActivity.this));
                adapter11 = new JobPerfectInfoTagAdapter(PersonInformationActivity.this);
                tag22.setAdapter(adapter11);
                adapter11.setCommonInterface(new RecyclerViewCommonInterface() {
                    @Override
                    public void onClick(Object bean) {
                        //点击了第二页的
                        tag33.setLayoutManager(new LinearLayoutManager(PersonInformationActivity.this));
                        adapter22 = new JobPerfectInfoTagAdapter(PersonInformationActivity.this);
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
        tv_city.setText(cacheTag.getCategoryname());
    }
    //    --------------------------所在城市结束-----------------------------------------------------------------------

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
                case "QS_education":
                    commonDialog(commonTagBean.getData().getQS_education());
                    break;
                case "QS_experience":
                    commonDialog(commonTagBean.getData().getQS_experience());
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

        View view = View.inflate(PersonInformationActivity.this, R.layout.dialog_common_tag, null);
        showdialog(view);
        RecyclerView tag_recyclerview = (RecyclerView) view.findViewById(R.id.tag_recyclerview);
        TextView cancle = (TextView) view.findViewById(R.id.tv_cancel);
        TextView confirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        switch (clickTag) {
            case "QS_education":
                tv_title.setText("最高学历");
                break;
            case "QS_experience":
                tv_title.setText("工作经验");
                break;
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
            case "QS_education":
                education = tagBean.getCId() + "";
                educationCn = tagBean.getCName();
                tv_education.setText(tagBean.getCName());
                break;
            case "QS_experience":
                experience = tagBean.getCId() + "";
                experienceCn = tagBean.getCName();
                tv_work_time.setText(tagBean.getCName());
                break;
            case "QS_jobs_nature":
//                tv_job.setText(tagBean.getCName());
                break;
        }
        cacheCommonTagBean = null;
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 选择图片dialog
     */
    public void photodialog() {

        View view = View.inflate(PersonInformationActivity.this, R.layout.dialog_publish_photo, null);
        showdialog(view);

        final TextView photo = (TextView) view.findViewById(R.id.photo);
        final TextView picture = (TextView) view.findViewById(R.id.picture);
        // 从图库中选择
        photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (photo.getText().toString().contains(getResources().getString(R.string.publish_photo))) {

                    Intent intent = new Intent(PersonInformationActivity.this, TakeSimpleActivity.class);
                    intent.putExtra("Type", 1);
                    startActivityForResult(intent, resultCode_Photos);
                    myDialog.dismiss();
                }

            }
        });
        // 拍照
        picture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (picture.getText().toString().contains(getResources().getString(R.string.publish_picture))) {
                    Intent intent = new Intent(PersonInformationActivity.this, TakeSimpleActivity.class);
                    intent.putExtra("Type", 0);
                    startActivityForResult(intent, resultCode_Camera);
                    myDialog.dismiss();
                }
            }
        });

    }

    /**
     * 性别dialog
     */
    public void sexDialog() {

        View view = View.inflate(PersonInformationActivity.this, R.layout.dialog_publish_photo, null);
        showdialog(view);

        final TextView photo = (TextView) view.findViewById(R.id.photo);
        final TextView picture = (TextView) view.findViewById(R.id.picture);
        photo.setText("男");
        picture.setText("女");
        // 从图库中选择
        photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setSexValue("男");
                myDialog.dismiss();

            }
        });
        // 拍照
        picture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setSexValue("女");
                myDialog.dismiss();
            }
        });

    }

    private void setSexValue(String sex) {

        if (!TextUtils.isEmpty(sex)) {
            this.sex = ("男".equals(sex) ? "1" : "0");
            tv_sex.setText(sex);
        }
    }

    private MyDialog myDialog;

    /**
     * 弹出dialog
     *
     * @param view
     */
    private void showdialog(View view) {

        myDialog = new MyDialog(this, 0, 0, view, R.style.dialog);
        myDialog.show();
    }

    /**
     * 获取个人基本信息
     */
    private void getBaseInfo() {
        Call<PersonBaseInfo> getPersonInfoCall = RestAdapterManager.getApi().getPersonInfo(BaseContext.getInstance().getUserInfo().uid + "");
        getPersonInfoCall.enqueue(new JyCallBack<PersonBaseInfo>() {
            @Override
            public void onSuccess(Call<PersonBaseInfo> call, Response<PersonBaseInfo> response) {
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    baseInfo = response.body();
                    setData();
                } else {
                    UIUtil.showToast("接口错误");
                }
            }

            @Override
            public void onError(Call<PersonBaseInfo> call, Throwable t) {
                UIUtil.showToast("接口错误");
            }

            @Override
            public void onError(Call<PersonBaseInfo> call, Response<PersonBaseInfo> response) {
                UIUtil.showToast("接口错误");
            }
        });
    }

    /**
     * 设置数据
     */
    private void setData() {
        ImageLoadedrManager.getInstance().display(this, Constants.COMMON_PERSON_URL + baseInfo.getData().getAvatars(), iv_head);
        et_name.setText(baseInfo.getData().getRealname());
        tv_sex.setText(baseInfo.getData().getSexCn());
        tv_birthday.setText(baseInfo.getData().getBirthday());
        tv_city.setText(baseInfo.getData().getResidence());
        tv_education.setText(baseInfo.getData().getEducationCn());
        tv_work_time.setText(baseInfo.getData().getExperienceCn());
        et_phone.setText(baseInfo.getData().getPhone());
        et_email.setText(baseInfo.getData().getEmail());
    }

    /**
     * 根据对应的分类id获取对应的数据
     */

    private void getTagItmes() {
        List<String> list = new ArrayList<>();
        list.add("QS_education");
        list.add("QS_experience");
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
