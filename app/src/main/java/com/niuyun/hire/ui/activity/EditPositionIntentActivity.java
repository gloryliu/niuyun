package com.niuyun.hire.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.niuyun.hire.ui.bean.PositionIntentBean;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.MyDialog;
import com.niuyun.hire.view.TitleBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/12.
 */

public class EditPositionIntentActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_view)
    TitleBar title_view;
    @BindView(R.id.tv_job_type)
    TextView tv_job_type;
    @BindView(R.id.rl_job_type)
    RelativeLayout rl_job_type;
    @BindView(R.id.rl_city)
    RelativeLayout rl_city;
    @BindView(R.id.rl_wage)
    RelativeLayout rl_wage;
    @BindView(R.id.et_job)
    EditText et_job;
    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.tv_wage)
    TextView tv_wage;
    @BindView(R.id.bt_save)
    Button bt_save;
    @BindView(R.id.bt_delete)
    Button bt_delete;
    private CommonTagBean commonTagBean;
    private String clickTag;
    private CommonTagItemBean cacheCommonTagBean;


    private String wage;//期望薪资
    private String wageCn;
    private String cId;//期望行业
    private String cName;
    private String intentionCityId;//城市
    private String intentionCity;
    private JobTagBean cityBean;
    private JobTagBean.DataBean cacheCityTag;
    private PositionIntentBean.DataBean dataBean;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_edit_position_intent;
    }

    @Override
    public void initViewsAndEvents() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            dataBean = (PositionIntentBean.DataBean) bundle.getSerializable("positionintent");
            if (dataBean != null) {
                initData();
                bt_delete.setVisibility(View.VISIBLE);
            } else {
                bt_delete.setVisibility(View.GONE);
            }
        }else {
            bt_delete.setVisibility(View.GONE);
        }
        initTitle();
        rl_job_type.setOnClickListener(this);
        rl_city.setOnClickListener(this);
        rl_wage.setOnClickListener(this);
        bt_save.setOnClickListener(this);
        bt_delete.setOnClickListener(this);
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

    private void initData() {
        if (dataBean != null) {
            tv_job_type.setText(dataBean.getTradeCn());
            cId = dataBean.getTrade() + "";
            cName = dataBean.getTradeCn();
            et_job.setText(dataBean.getJobsName());
            tv_city.setText(dataBean.getDistrictCn());
            intentionCityId = dataBean.getDistrict() + "";
            intentionCity = dataBean.getDistrictCn();
            tv_wage.setText(dataBean.getWageCn());
            wage = dataBean.getWage() + "";
            wageCn = dataBean.getWageCn();
        }
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        title_view.setTitle("管理求职意向");
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
        title_view.setImmersive(true);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_job_type:
                //期望行业
                clickTag = "QS_trade";
                if (!UIUtil.isFastDoubleClick()) {
                    click(clickTag);
                }
                break;
            case R.id.rl_city:
                getCityData("0");
                break;
            case R.id.rl_wage:
                //期望薪资
                clickTag = "QS_wage";
                if (!UIUtil.isFastDoubleClick()) {
                    click(clickTag);
                }
                break;
            case R.id.bt_save:
                if (!UIUtil.isFastDoubleClick()) {
                    if (checkData()) {
                        if (dataBean != null && dataBean.getId() > 0) {
                            editDate();
                        } else {
                            commitDate();
                        }

                    }
                }

                break;
            case R.id.bt_delete:
                if (!UIUtil.isFastDoubleClick()) {
                    if (dataBean != null && dataBean.getId() > 0) {

                        deleteDate();
                    }
                }
                break;

        }
    }




    /**
     * 新增求职意向
     */
    private void commitDate() {
        DialogUtils.showDialog(this, "加载中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("jobsName", et_job.getText().toString());
        map.put("uid", BaseContext.getInstance().getUserInfo().uid + "");
        map.put("wage", wage);
        map.put("wageCn", wageCn);
        map.put("trade", cId);
        map.put("tradeCn", cName);
        map.put("district", intentionCityId);
        map.put("districtCn", intentionCity);
        Call<SuperBean<String>> commitPositionIntentCall = RestAdapterManager.getApi().commitPositionIntent(map);
        commitPositionIntentCall.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    UIUtil.showToast(response.body().getMsg());
                    EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.UPDATE_POSITION_INTENT));
                    finish();

                }
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast("接口异常");
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
                UIUtil.showToast("接口异常");
            }
        });
    }

    /**
     * 编辑求职意向
     */
    private void editDate() {
        DialogUtils.showDialog(this, "加载中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("jobsName", et_job.getText().toString());
        map.put("uid", BaseContext.getInstance().getUserInfo().uid + "");
        map.put("wage", wage);
        map.put("wageCn", wageCn);
        map.put("trade", cId);
        map.put("tradeCn", cName);
        map.put("district", intentionCityId);
        map.put("districtCn", intentionCity);
        if (dataBean!=null){
            map.put("id", dataBean.getId() + "");
        }
        Call<SuperBean<String>> commitPositionIntentCall = RestAdapterManager.getApi().editPositionIntent(map);
        commitPositionIntentCall.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    UIUtil.showToast(response.body().getMsg());
                    EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.UPDATE_POSITION_INTENT));
                    finish();

                } else {
                    UIUtil.showToast("修改失败");
                }
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast("接口异常");
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
                UIUtil.showToast("接口异常");
            }
        });
    }

    /**
     * 删除求职意向
     */
    private void deleteDate() {
        DialogUtils.showDialog(this, "加载中...", false);
        Call<SuperBean<String>> commitPositionIntentCall = RestAdapterManager.getApi().deletePositionIntent(dataBean.getId() + "");
        commitPositionIntentCall.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    UIUtil.showToast(response.body().getMsg());
                    EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.UPDATE_POSITION_INTENT));
                    finish();

                }
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast("接口异常");
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
                UIUtil.showToast("接口异常");
            }
        });
    }

    private boolean checkData() {
        if (TextUtils.isEmpty(cId)) {
            UIUtil.showToast("请选择期望行业");
            return false;
        }
        if (TextUtils.isEmpty(et_job.getText().toString())) {
            UIUtil.showToast("请输入期望职位");
            return false;
        }
        if (TextUtils.isEmpty(intentionCityId)) {
            UIUtil.showToast("请选择工作城市");
            return false;
        }
        if (TextUtils.isEmpty(wage)) {
            UIUtil.showToast("请选择期望薪资");
            return false;
        }
        return true;
    }
    //    --------------------------所在城市开始-----------------------------------------------------------------------

    /**
     * 选择城市dialog
     */
    public void cityTagDialog() {
        if (cityBean == null) {
            return;
        }

        View view = View.inflate(EditPositionIntentActivity.this, R.layout.dialog_city_tag, null);
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
                setTvCityTag(cacheCityTag);
                //点击了第一页的
                myDialog.dismiss();
            }
        });
        tag1.setLayoutManager(new LinearLayoutManager(EditPositionIntentActivity.this));
        JobPerfectInfoTagAdapter adapter = new JobPerfectInfoTagAdapter(EditPositionIntentActivity.this, cityBean.getData());
        tag1.setAdapter(adapter);
        adapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {
                cacheCityTag = (JobTagBean.DataBean) bean;
            }
        });

    }

    /**
     * 根据各级id获取城市信息
     *
     * @param id
     */
    private void getCityData(String id) {
        DialogUtils.showDialog(this, "加载中...", false);
        Call<JobTagBean> jobTagBeanCall = RestAdapterManager.getApi().getDistrict(id);
        jobTagBeanCall.enqueue(new JyCallBack<JobTagBean>() {
            @Override
            public void onSuccess(Call<JobTagBean> call, Response<JobTagBean> response) {
                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    cityBean = response.body();
                    cityTagDialog();
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
//        intentionCity = intentionCityCn1 + "/" + intentionCityCn2 + "/" + intentionCityCn3;
        intentionCity = cacheTag.getCategoryname() + "";
        intentionCityId = cacheTag.getId() + "";
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
                case "QS_trade":
                    commonDialog(commonTagBean.getData().getQS_trade());
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

        View view = View.inflate(EditPositionIntentActivity.this, R.layout.dialog_common_tag, null);
        showdialog(view);
        RecyclerView tag_recyclerview = (RecyclerView) view.findViewById(R.id.tag_recyclerview);
        TextView cancle = (TextView) view.findViewById(R.id.tv_cancel);
        TextView confirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        switch (clickTag) {
            case "QS_trade":
                tv_title.setText("期望行业");
                break;
//            case "QS_experience":
//                tv_title.setText("工作经验");
//                break;
            case "QS_wage":
                tv_title.setText("期望薪资");
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
            case "QS_trade":
                cId = tagBean.getCId() + "";
                cName = tagBean.getCName();
                tv_job_type.setText(tagBean.getCName());
                break;
            case "QS_experience":
//                experience = tagBean.getCId() + "";
//                experienceCn = tagBean.getCName();
//                tv_work_age.setText(tagBean.getCName());
                break;
            case "QS_jobs_nature":
//                tv_job.setText(tagBean.getCName());
                break;
            case "QS_wage":
                tv_wage.setText(tagBean.getCName());
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
        list.add("QS_trade");
//        list.add("QS_experience");
        list.add("QS_wage");
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
