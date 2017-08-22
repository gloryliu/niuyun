package com.niuyun.hire.ui.activity;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.adapter.CommonPerfectInfoTagAdapter;
import com.niuyun.hire.ui.adapter.JobPerfectInfoTagAdapter;
import com.niuyun.hire.ui.bean.CommonTagBean;
import com.niuyun.hire.ui.bean.CommonTagItemBean;
import com.niuyun.hire.ui.bean.GetBaseTagBean;
import com.niuyun.hire.ui.bean.JobTagBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.MyDialog;
import com.niuyun.hire.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-8-22.
 */

public class EnterprisePublishPositionActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_view)
    TitleBar titleView;
    @BindView(R.id.et_position_type)
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
    private String cId;//期望行业
    private String cName;
    private String intentionCityId;//城市
    private String intentionCity;
    private JobTagBean cityBean;
    private JobTagBean.DataBean cacheCityTag;
    @Override
    public int getContentViewLayoutId() {
        return R.layout.enterprise_activity_publish_position;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();

        etPositionType.setOnClickListener(this);
        etPositionNature.setOnClickListener(this);
        etCity.setOnClickListener(this);
        etEducation.setOnClickListener(this);
        etExperience.setOnClickListener(this);
        etWage.setOnClickListener(this);
        etlocation.setOnClickListener(this);
        btPublish.setOnClickListener(this);


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
//                UpPrepare();
            }
        });
        titleView.setImmersive(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_position_type:
                break;
        }
    }
    //    --------------------------所在城市开始-----------------------------------------------------------------------

    /**
     * 选择城市dialog
     */
    public void cityTagDialog() {
        if (cityBean == null) {
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
        tag1.setLayoutManager(new LinearLayoutManager(EnterprisePublishPositionActivity.this));
        JobPerfectInfoTagAdapter adapter = new JobPerfectInfoTagAdapter(EnterprisePublishPositionActivity.this, cityBean.getData());
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
        etCity.setText(cacheTag.getCategoryname());
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

        View view = View.inflate(EnterprisePublishPositionActivity.this, R.layout.dialog_common_tag, null);
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
//                tv_job_type.setText(tagBean.getCName());
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
        list.add("QS_trade");
        list.add("QS_experience");
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
