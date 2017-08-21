package com.niuyun.hire.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.bean.PreviewResumeBean;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.utils.timepicker.TimePickerView;
import com.niuyun.hire.view.TitleBar;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-8-9.
 */

public class EditWorkExperienceActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_view)
    TitleBar titleView;
    @BindView(R.id.et_begin_time)
    TextView et_begin_time;
    @BindView(R.id.iv_begin_time_more)
    ImageView iv_begin_time_more;
    @BindView(R.id.et_end_time)
    TextView et_end_time;
    @BindView(R.id.iv_end_time_more)
    ImageView iv_end_time_more;
    @BindView(R.id.bt_save)
    Button bt_save;
    @BindView(R.id.et_company_name)
    EditText et_company_name;
    @BindView(R.id.et_describe)
    EditText et_describe;
    @BindView(R.id.et_position_name)
    EditText et_position_name;
    @BindView(R.id.bt_delete)
    Button bt_delete;
    private PreviewResumeBean.DataBean.ResumeWorkBean editBean;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_edit_work_experience;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            editBean = (PreviewResumeBean.DataBean.ResumeWorkBean) bundle.getSerializable("bean");
        }
        if (editBean != null) {
            bt_delete.setVisibility(View.VISIBLE);
            setDefaultData();
        } else {
            bt_delete.setVisibility(View.GONE);
        }
        et_begin_time.setOnClickListener(this);
        iv_begin_time_more.setOnClickListener(this);
        et_end_time.setOnClickListener(this);
        iv_end_time_more.setOnClickListener(this);
        bt_save.setOnClickListener(this);
        bt_delete.setOnClickListener(this);
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

        titleView.setTitle("工作经历");
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
                UpPrepare();
            }
        });
        titleView.setImmersive(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_begin_time:
            case R.id.iv_begin_time_more:
                DialogUtils.showTimePcikerDialog(this, TimePickerView.Type.YEAR_MONTH, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date) {
                        et_begin_time.setText(UIUtil.getTime(date, "yyyy-MM"));
                    }
                });
                break;
            case R.id.et_end_time:
            case R.id.iv_end_time_more:
                DialogUtils.showTimePcikerDialog(this, TimePickerView.Type.YEAR_MONTH, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date) {
                        et_end_time.setText(UIUtil.getTime(date, "yyyy-MM"));
                    }
                });
                break;
            case R.id.bt_save:
                UpPrepare();
                break;
            case R.id.bt_delete:
                if (editBean != null) {
                    deleteExperience();
                }
                break;
        }
    }

    private void UpPrepare() {
        if (!UIUtil.isFastDoubleClick()) {
            if (checkData()) {
                upLoadInfo();
            }
        }

    }

    /**
     * 编辑模式设置数据
     */
    private void setDefaultData() {
        if (editBean != null) {
            et_describe.setText(editBean.getAchievements());
            if (!TextUtils.isEmpty(editBean.getCompanyname())) {
                et_company_name.setText(editBean.getCompanyname());
            }
            et_position_name.setText(editBean.getJobs());
            et_end_time.setText(editBean.getEndyear() + "_" + editBean.getEndmonth());
            et_begin_time.setText(editBean.getStartyear() + "_" + editBean.getStartmonth());
        }
    }

    private void upLoadInfo() {
        DialogUtils.showDialog(EditWorkExperienceActivity.this, "上传中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("achievements", et_describe.getText().toString());
        map.put("companyname", et_company_name.getText().toString());
        map.put("endmonth", et_end_time.getText().toString().split("-")[1] + "");
        map.put("endyear", et_end_time.getText().toString().split("-")[0] + "");
        map.put("jobs", et_position_name.getText().toString());
        map.put("startmonth", et_begin_time.getText().toString().split("-")[1] + "");
        map.put("startyear", et_begin_time.getText().toString().split("-")[0] + "");
        if (BaseContext.getInstance().getUserInfo() != null) {
            map.put("uid", BaseContext.getInstance().getUserInfo().uid + "");
            map.put("pid", BaseContext.getInstance().getUserInfo().resumeId + "");//简历id
        }
        Call<SuperBean<String>> addWorkExperienceCall;
        if (editBean != null) {
            map.put("id", editBean.getId() + "");//id
            addWorkExperienceCall = RestAdapterManager.getApi().editWorkExperience(map);
        } else {

            addWorkExperienceCall = RestAdapterManager.getApi().addWorkExperience(map);
        }
        addWorkExperienceCall.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                try {
                    UIUtil.showToast(response.body().getMsg());
                } catch (Exception e) {
                }
                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.UPDATE_LIVE_RESUME));
                    finish();
                }
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast("接口错误");
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
                UIUtil.showToast("接口错误");
            }
        });
    }

    private void deleteExperience() {
        DialogUtils.showDialog(EditWorkExperienceActivity.this, "删除中...", false);

        Call<SuperBean<String>> deleteWorkExperienceCall = RestAdapterManager.getApi().deleteWorkExperience(editBean.getId() + "");
        deleteWorkExperienceCall.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                try {
                    UIUtil.showToast(response.body().getMsg());
                } catch (Exception e) {
                }
                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.UPDATE_LIVE_RESUME));
                    finish();
                }
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast("接口错误");
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
                UIUtil.showToast("接口错误");
            }
        });
    }

    private boolean checkData() {
        if (TextUtils.isEmpty(et_company_name.getText().toString())) {
            UIUtil.showToast("公司名称不能为空");
            return false;
        }
        if (TextUtils.isEmpty(et_position_name.getText().toString())) {
            UIUtil.showToast("职位不能为空");
            return false;
        }
        if (TextUtils.isEmpty(et_begin_time.getText().toString())) {
            UIUtil.showToast("开始时间不能为空");
            return false;
        }
        if (TextUtils.isEmpty(et_end_time.getText().toString())) {
            UIUtil.showToast("结束时间不能为空");
            return false;
        }
        if (TextUtils.isEmpty(et_describe.getText().toString())) {
            UIUtil.showToast("描述不能为空");
            return false;
        }
        return true;
    }
}
