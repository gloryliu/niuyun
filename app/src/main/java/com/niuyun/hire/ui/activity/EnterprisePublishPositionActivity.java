package com.niuyun.hire.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.niuyun.hire.ui.bean.EnterprisePublishedPositionBean;
import com.niuyun.hire.ui.bean.GetBaseTagBean;
import com.niuyun.hire.ui.bean.JobTagBean;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.ErrorMessageUtils;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.MyDialog;
import com.niuyun.hire.view.TitleBar;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

import static com.niuyun.hire.R.id.et_position_type;

/**
 * Created by chen.zhiwei on 2017-8-22.
 */

public class EnterprisePublishPositionActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_view)
    TitleBar titleView;
    @BindView(et_position_type)
    TextView etPositionType;
    @BindView(R.id.et_position_name)
    EditText etPositionName;
    @BindView(R.id.et_position_nature)
    TextView etPositionNature;
        @BindView(R.id.et_city)
        TextView etCity;
    @BindView(R.id.et_education)
    TextView etEducation;
    @BindView(R.id.et_experience)
    TextView etExperience;
    @BindView(R.id.et_wage)
    TextView etWage;
    @BindView(R.id.etlocation)
    TextView etlocation;
    @BindView(R.id.et_describe)
    EditText etDescribe;
    @BindView(R.id.bt_publish)
    Button btPublish;
    private CommonTagBean commonTagBean;
    private String clickTag;
    private CommonTagItemBean cacheCommonTagBean;


    private String wage;//期望薪资
    private String wageCn;
    private String experience;
    private String experienceCn;
    private String nature;
    private String natureCn;
    private String cId;
    private String cName;
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


    private String intentionJobsId;//职业类型,每一级的id中间用 . 隔开
    private String intentionJobs;

    private String intentionJobsId1;
    private String intentionJobsId2;
    private String intentionJobsId3;


    private int jobStep = 0;

    private JobPerfectInfoTagAdapter adapter1;
    private JobPerfectInfoTagAdapter adapter2;
    private JobTagBean.DataBean cacheJobTag;
    private EnterprisePublishedPositionBean.DataBeanX.DataBean editBean;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.enterprise_activity_publish_position;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            editBean = (EnterprisePublishedPositionBean.DataBeanX.DataBean) bundle.getSerializable("bean");
        }
        if (editBean != null) {
            setDefaultData();
        } else {
        }


        etPositionType.setOnClickListener(this);
        etPositionNature.setOnClickListener(this);
        etEducation.setOnClickListener(this);
        etExperience.setOnClickListener(this);
        etWage.setOnClickListener(this);
        etlocation.setOnClickListener(this);
        btPublish.setOnClickListener(this);
        btPublish.setOnClickListener(this);
        etCity.setOnClickListener(this);


    }

    @Override
    public void loadData() {

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

        titleView.setTitle("发布职位");
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
        titleView.addAction(new TitleBar.TextAction("保存") {
            @Override
            public void performAction(View view) {
                if (checkData()) {
                    uploadInfo();
                }
            }
        });
        titleView.setImmersive(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case et_position_type:
                //职位类别，三级地址
                if (!UIUtil.isFastDoubleClick()) {
                    jobStep = 0;
                    getJobData("0");
                }
                break;
            case R.id.et_position_nature:
                //工作性质
                clickTag = "QS_jobs_nature";
                click(clickTag);
                break;
            case R.id.et_education:
                //学历
                clickTag = "QS_education";
                click(clickTag);
                break;
            case R.id.et_experience:
                //经验
                clickTag = "QS_experience";
                click(clickTag);
                break;
            case R.id.et_wage:
                //薪资
                clickTag = "QS_wage";
                click(clickTag);
                break;
            case R.id.etlocation:
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
            case R.id.et_city:
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
            case R.id.bt_publish:
                if (checkData()) {
                    uploadInfo();
                }
                break;
        }
    }

    /**
     * 设置编辑默认值
     */
    private void setDefaultData() {
        etPositionType.setText(editBean.getCategoryCn());
        intentionJobsId1 = editBean.getTopclass() + "";
        intentionJobsId2 = editBean.getCategory() + "";
        intentionJobsId3 = editBean.getSubclass() + "";
        intentionJobs = editBean.getCategoryCn();


        etPositionName.setText(editBean.getJobsName());
        etDescribe.setText(editBean.getContents());


        etCity.setText(editBean.getDistrictCn());
        intentionCity = editBean.getDistrictCn();
        intentionCityId1 = editBean.getDistrict() + "";
        intentionCityId2 = editBean.getSdistrict() + "";
        intentionCityId3 = editBean.getTdistrict() + "";


        cId = editBean.getEducation() + "";
        cName = editBean.getEducationCn();
        etEducation.setText(cName);


        experience = editBean.getExperience() + "";
        experienceCn = editBean.getExperienceCn();
        etExperience.setText(experienceCn);


        nature = editBean.getNature() + "";
        natureCn = editBean.getNatureCn();
        etPositionNature.setText(natureCn);


        wage = editBean.getMinwage() + "";
        wageCn = editBean.getNatureCn();
        etWage.setText(wageCn);

    }

    private boolean checkData() {
        if (TextUtils.isEmpty(intentionJobs)) {
            UIUtil.showToast("职位类别不能为空");
            return false;
        }
        if (TextUtils.isEmpty(etPositionName.getText().toString())) {
            UIUtil.showToast("职位名称不能为空");
            return false;
        }
        if (TextUtils.isEmpty(nature)) {
            UIUtil.showToast("工作性质不能为空");
            return false;
        }
        if (TextUtils.isEmpty(cId)) {
            UIUtil.showToast("学历要求不能为空");
            return false;
        }
        if (TextUtils.isEmpty(experience)) {
            UIUtil.showToast("经验要求不能为空");
            return false;
        }
        if (TextUtils.isEmpty(wage)) {
            UIUtil.showToast("薪资范围不能为空");
            return false;
        }
        if (TextUtils.isEmpty(intentionCity)) {
            UIUtil.showToast("工作地址不能为空");
            return false;
        }
        if (TextUtils.isEmpty(etDescribe.getText().toString())) {
            UIUtil.showToast("职位描述不能为空");
            return false;
        }
        return true;
    }

    private void uploadInfo() {
        DialogUtils.showDialog(EnterprisePublishPositionActivity.this, "", false);
        Map<String, String> map = new HashMap<>();
        map.put("jobsName", etPositionName.getText().toString());
        map.put("contents", etDescribe.getText().toString());

        map.put("topclass", intentionJobsId1);
        map.put("category", intentionJobsId2);
        map.put("subclass", intentionJobsId3);
        map.put("categoryCn", intentionJobs);


        map.put("district", intentionCityId1);
        map.put("sdistrict", intentionCityId2);
        map.put("tdistrict", intentionCityId3);
        map.put("districtCn", intentionCity);


        map.put("education", cId);
        map.put("educationCn", cName);
        map.put("experience", experience);
        map.put("experienceCn", experienceCn);

        map.put("nature", nature);
        map.put("natureCn", natureCn);
        map.put("wage", wage);
        map.put("wageCn", wageCn);
        map.put("uid", BaseContext.getInstance().getUserInfo().uid + "");
        Call<SuperBean<String>> publishPositionCall;
        if (editBean != null) {
            map.put("id", editBean.getId() + "");
            publishPositionCall = RestAdapterManager.getApi().editPosition(map);
        } else {

            publishPositionCall = RestAdapterManager.getApi().publishPosition(map);
        }
        publishPositionCall.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
                try {
                    UIUtil.showToast(response.body().getMsg());
                } catch (Exception w) {
                }
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.UPDATE_PUBLISHED_POSITION));
                    finish();
                }

            }

            @Override
            public void onError(Call<SuperBean<String>> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast("接口异常");
                LogUtils.e(t.getMessage());
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
                try {
                    ErrorMessageUtils.taostErrorMessage(EnterprisePublishPositionActivity.this, response.errorBody().string(), "");
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

        View view = View.inflate(EnterprisePublishPositionActivity.this, R.layout.dialog_city_tag, null);
        showdialog(view);
        final RecyclerView tag1 = (RecyclerView) view.findViewById(R.id.tag1);
        final RecyclerView tag22 = (RecyclerView) view.findViewById(R.id.tag2);
        final RecyclerView tag33 = (RecyclerView) view.findViewById(R.id.tag3);
        TextView cancle = (TextView) view.findViewById(R.id.tv_cancel);
        TextView confirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tv_itle = (TextView) view.findViewById(R.id.tv_itle);
        tv_itle.setText("工作地址");
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
        tag1.setLayoutManager(new LinearLayoutManager(EnterprisePublishPositionActivity.this));
        JobPerfectInfoTagAdapter adapter = new JobPerfectInfoTagAdapter(EnterprisePublishPositionActivity.this, tagBean.getData());
        tag1.setAdapter(adapter);
        adapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {

                tag22.setLayoutManager(new LinearLayoutManager(EnterprisePublishPositionActivity.this));
                adapter11 = new JobPerfectInfoTagAdapter(EnterprisePublishPositionActivity.this);
                tag22.setAdapter(adapter11);
                adapter11.setCommonInterface(new RecyclerViewCommonInterface() {
                    @Override
                    public void onClick(Object bean) {
                        //点击了第二页的
                        tag33.setLayoutManager(new LinearLayoutManager(EnterprisePublishPositionActivity.this));
                        adapter22 = new JobPerfectInfoTagAdapter(EnterprisePublishPositionActivity.this);
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
        etCity.setText(cacheTag.getCategoryname());
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

        View view = View.inflate(EnterprisePublishPositionActivity.this, R.layout.dialog_job_tag, null);
        showdialog(view);
        final RecyclerView tag1 = (RecyclerView) view.findViewById(R.id.tag1);
        final RecyclerView tag2 = (RecyclerView) view.findViewById(R.id.tag2);
        final RecyclerView tag3 = (RecyclerView) view.findViewById(R.id.tag3);
        TextView cancle = (TextView) view.findViewById(R.id.tv_cancel);
        TextView confirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tv_itle = (TextView) view.findViewById(R.id.tv_itle);
        tv_itle.setText("职位类别");
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
        tag1.setLayoutManager(new LinearLayoutManager(EnterprisePublishPositionActivity.this));
        JobPerfectInfoTagAdapter adapter = new JobPerfectInfoTagAdapter(EnterprisePublishPositionActivity.this, tagBean.getData());
        tag1.setAdapter(adapter);
        adapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {

                tag2.setLayoutManager(new LinearLayoutManager(EnterprisePublishPositionActivity.this));
                adapter1 = new JobPerfectInfoTagAdapter(EnterprisePublishPositionActivity.this);
                tag2.setAdapter(adapter1);
                adapter1.setCommonInterface(new RecyclerViewCommonInterface() {
                    @Override
                    public void onClick(Object bean) {
                        //点击了第二页的
                        tag3.setLayoutManager(new LinearLayoutManager(EnterprisePublishPositionActivity.this));
                        adapter2 = new JobPerfectInfoTagAdapter(EnterprisePublishPositionActivity.this);
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
        etPositionType.setText(cacheJobTag.getCategoryname());
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
                case "QS_education":
                    commonDialog(commonTagBean.getData().getQS_education());
                    break;
                case "QS_wage":
                    commonDialog(commonTagBean.getData().getQS_wage());
                    break;
                case "QS_jobs_nature":
                    commonDialog(commonTagBean.getData().getQS_jobs_nature());
                    break;
                case "QS_experience":
                    commonDialog(commonTagBean.getData().getQS_experience());
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

        View view = View.inflate(EnterprisePublishPositionActivity.this, R.layout.dialog_common_tag, null);
        showdialog(view);
        RecyclerView tag_recyclerview = (RecyclerView) view.findViewById(R.id.tag_recyclerview);
        TextView cancle = (TextView) view.findViewById(R.id.tv_cancel);
        TextView confirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        switch (clickTag) {
            case "QS_jobs_nature":
                tv_title.setText("工作性质");
                break;
            case "QS_experience":
                tv_title.setText("经验要求");
                break;
            case "QS_wage":
                tv_title.setText("薪资范围");
                break;
            case "QS_education":
                tv_title.setText("学历要求");
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
                cId = tagBean.getCId() + "";
                cName = tagBean.getCName();
                etEducation.setText(tagBean.getCName());
                break;
            case "QS_experience":
                experience = tagBean.getCId() + "";
                experienceCn = tagBean.getCName();
                etExperience.setText(tagBean.getCName());
                break;
            case "QS_jobs_nature":
                nature = tagBean.getCId() + "";
                natureCn = tagBean.getCName();
                etPositionNature.setText(tagBean.getCName());
                break;
            case "QS_wage":
                etWage.setText(tagBean.getCName());
                wage = tagBean.getCId() + "";
                wageCn = tagBean.getCName();
                break;
        }
        cacheCommonTagBean = null;
    }

    /**
     * 根据对应的分类id获取对应的数据
     */

    private void getTagItmes() {
        List<String> list = new ArrayList<>();
//        list.add("QS_trade");
        list.add("QS_experience");
        list.add("QS_wage");
        list.add("QS_jobs_nature");
        list.add("QS_education");
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
}
