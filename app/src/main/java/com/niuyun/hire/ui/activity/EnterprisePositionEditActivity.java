package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.easeui.ui.EaseBaiduMapActivity;
import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.bean.EnterprisePublishedPositionBean;
import com.niuyun.hire.ui.bean.JobDetailsBean;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.polyvLive.activity.PolyvPlayerView;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.TitleBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-7-26.
 */

public class EnterprisePositionEditActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_view)
    TitleBar titleView;
    @BindView(R.id.tv_company_name1)
    TextView tv_company_name1;//公司名称
    @BindView(R.id.iv_company)
    ImageView iv_company;//公司图标
    @BindView(R.id.tv_position_name)
    TextView tv_position_name;//职位名称
    @BindView(R.id.tv_position_price)
    TextView tv_position_price;//薪资
    @BindView(R.id.tv_company_name)
    TextView tv_company_name;//公司名称
    @BindView(R.id.tv_location)
    TextView tv_location;//公司地址
    @BindView(R.id.tv_work_age)
    TextView tv_work_age;//工作年限
    @BindView(R.id.tv_education)
    TextView tv_education;//学历
    @BindView(R.id.tv_company_scale)
    TextView tv_company_scale;//公司规模
    @BindView(R.id.tv_company_type)
    TextView tv_company_type;//公司类型
    @BindView(R.id.tv_work_responsibilities)
    TextView tv_work_responsibilities;//工作职责
    @BindView(R.id.tv_company_location)
    TextView tv_company_location;//公司地址
    @BindView(R.id.tv_responsibility_more)
    TextView tv_responsibility_more;//更多
    @BindView(R.id.ll_company)
    LinearLayout ll_company;
    @BindView(R.id.bt_edit)
    Button bt_edit;
    @BindView(R.id.bt_delete)
    Button bt_delete;
    @BindView(R.id.pv_play)
    PolyvPlayerView pv_play;
    @BindView(R.id.ll_video)
    LinearLayout ll_video;
    @BindView(R.id.rl_location)
    RelativeLayout rl_location;
    private String companyId;//公司id
    private String id;//职位id
    private String uid;//职位id
    //    private ImageView mCollectView;
    Call<JobDetailsBean> jobDetailsBeanCall;
    Call<SuperBean<String>> deleteCall;
    //    private JobDetailsBean bean;
    private EnterprisePublishedPositionBean.DataBeanX.DataBean databean;

    @Override
    public void enterPictureInPictureMode() {
        super.enterPictureInPictureMode();
    }

    @Override
    public int getContentViewLayoutId() {
        return R.layout.enterprise_activity_position_edit_layout;
    }

    @Override
    public void initViewsAndEvents() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            databean = (EnterprisePublishedPositionBean.DataBeanX.DataBean) bundle.getSerializable("bean");
            if (databean != null) {
                companyId = databean.getCompanyId() + "";
                id = databean.getId() + "";
                uid = databean.getUid() + "";
            }

        }
        initTitle();
        ll_company.setOnClickListener(this);
        bt_edit.setOnClickListener(this);
        bt_delete.setOnClickListener(this);
        rl_location.setOnClickListener(this);
    }

    @Override
    public void loadData() {
//        getJobDetails();
        setData(databean);
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

    private void setData(EnterprisePublishedPositionBean.DataBeanX.DataBean bean) {
        if (bean != null) {
            tv_position_name.setText(bean.getJobsName());
            tv_company_name1.setVisibility(View.GONE);
            ll_video.setVisibility(View.GONE);
            tv_position_price.setText(bean.getMinwage() / 1000 + "k-" + bean.getMaxwage() / 1000 + "k");
            tv_location.setText(bean.getDistrictCn());
            tv_work_age.setText(bean.getExperienceCn());
            tv_education.setText(bean.getEducationCn());

            //工作职责
            if (!TextUtils.isEmpty(bean.getContents())) {
                tv_work_responsibilities.setText(bean.getContents());
                tv_work_responsibilities.post(new Runnable() {
                    @Override
                    public void run() {
                        int lineCount = tv_work_responsibilities.getLineCount();
                        if (lineCount > 5) {
                            tv_responsibility_more.setVisibility(View.VISIBLE);
                            tv_work_responsibilities.setLines(5);
                        } else {
                            tv_responsibility_more.setVisibility(View.GONE);
                            tv_work_responsibilities.setLines(lineCount);
                        }
                    }
                });

            }
            tv_responsibility_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tv_responsibility_more.getText().equals("展开全部")) {
                        tv_work_responsibilities.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_work_responsibilities.setLines(tv_work_responsibilities.getLineCount());
                            }
                        });

                        tv_responsibility_more.setText("收起全部");
                    } else {
                        tv_responsibility_more.setText("展开全部");
                        tv_work_responsibilities.setLines(5);
                    }
                }
            });
            tv_company_location.setText(bean.getAddress());


        }
    }


    private void initTitle() {

        titleView.setTitle("职位管理");
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
        titleView.setImmersive(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_company:
                if (databean != null) {
                    Intent intent = new Intent(this, CompanyDetailsActivity.class);
                    intent.putExtra("id", databean.getCompanyId() + "");
                    startActivity(intent);
                }

                break;
            case R.id.bt_edit:
                if (databean != null) {
                    Intent intent = new Intent(this, EnterprisePublishPositionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean", databean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                break;
            case R.id.bt_delete:
                if (databean != null && databean.getId() > 0) {
                    deletePosition();
                }
                break;
            case R.id.rl_location:
                if (databean != null && databean.getMapX() > 0) {
                    Intent intent = new Intent(this, EaseBaiduMapActivity.class);
                    if (!TextUtils.isEmpty(databean.getAddress())) {
                        intent.putExtra("address", databean.getAddress());
                    }
                    if (databean.getMapX() > 0) {
                        intent.putExtra("latitude", databean.getMapX());
                    }
                    if (databean.getMapY() > 0) {
                        intent.putExtra("longitude", databean.getMapY());
                    }
                    startActivity(intent);
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (jobDetailsBeanCall != null) {
            jobDetailsBeanCall.cancel();
        }
        if (deleteCall != null) {
            deleteCall.cancel();
        }
        super.onDestroy();
    }

    private void deletePosition() {
        DialogUtils.showDialog(this, "", false);
        deleteCall = RestAdapterManager.getApi().deleteEnterprisePosition(databean.getId() + "");
        deleteCall.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
                if (response.body() != null && response.body().getCode() == Constants.successCode) {
                    UIUtil.showToast(response.body().getMsg());
                    EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.UPDATE_PUBLISHED_POSITION));
                    finish();
                } else {
                    UIUtil.showToast("接口异常");
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
}
