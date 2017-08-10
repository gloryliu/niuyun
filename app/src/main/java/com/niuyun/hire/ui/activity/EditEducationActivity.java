package com.niuyun.hire.ui.activity;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.niuyun.hire.ui.adapter.CommonPerfectInfoTagAdapter;
import com.niuyun.hire.ui.bean.CommonTagBean;
import com.niuyun.hire.ui.bean.CommonTagItemBean;
import com.niuyun.hire.ui.bean.GetBaseTagBean;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.utils.timepicker.TimePickerView;
import com.niuyun.hire.view.MyDialog;
import com.niuyun.hire.view.TitleBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-8-9.
 */

public class EditEducationActivity extends BaseActivity implements View.OnClickListener {
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
    @BindView(R.id.et_school_name)
    EditText et_school_name;
    @BindView(R.id.et_profession_name)
    EditText et_profession_name;
    @BindView(R.id.et_education)
    TextView et_education;
    private CommonTagBean commonTagBean;
    private CommonTagItemBean cacheCommonTagBean;
    private String education;//学历
    private String educationCn;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_edit_education;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        et_begin_time.setOnClickListener(this);
        iv_begin_time_more.setOnClickListener(this);
        et_end_time.setOnClickListener(this);
        iv_end_time_more.setOnClickListener(this);
        bt_save.setOnClickListener(this);
        et_education.setOnClickListener(this);
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

        titleView.setTitle("教育经历");
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
            case R.id.et_education:
                if (!UIUtil.isFastDoubleClick()) {
                    if (commonTagBean != null) {
                        commonDialog(commonTagBean.getData().getQS_education());
                    } else {
                        UIUtil.showToast("获取数据...");
                        getTagItmes();
                    }

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
     * 选择tag公告dialog
     */
    public void commonDialog(List<CommonTagItemBean> tagBean) {
        if (tagBean == null || tagBean.size() <= 0) {
            return;
        }

        View view = View.inflate(EditEducationActivity.this, R.layout.dialog_common_tag, null);
        showdialog(view);
        RecyclerView tag_recyclerview = (RecyclerView) view.findViewById(R.id.tag_recyclerview);
        TextView cancle = (TextView) view.findViewById(R.id.tv_cancel);
        TextView confirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText("最高学历");
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cacheCommonTagBean != null) {
                    education = cacheCommonTagBean.getCId() + "";
                    educationCn = cacheCommonTagBean.getCName();
                    et_education.setText(educationCn);
                    cacheCommonTagBean = null;
                }
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

    private void upLoadInfo() {
        DialogUtils.showDialog(EditEducationActivity.this, "上传中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("education", education);
        map.put("educationCn", educationCn);
        map.put("endmonth", et_end_time.getText().toString().split("-")[1] + "");
        map.put("endyear", et_end_time.getText().toString().split("-")[0] + "");
        map.put("school", et_school_name.getText().toString());
        map.put("speciality", et_profession_name.getText().toString());//专业
        map.put("startmonth", et_begin_time.getText().toString().split("-")[1] + "");
        map.put("startyear", et_begin_time.getText().toString().split("-")[0] + "");
        if (BaseContext.getInstance().getUserInfo() != null) {
            map.put("uid", BaseContext.getInstance().getUserInfo().uid + "");
            map.put("pid", BaseContext.getInstance().getUserInfo().resumeId + "");//简历id
        }

        Call<SuperBean<String>> addWorkExperienceCall = RestAdapterManager.getApi().addEducation(map);
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

    private boolean checkData() {
        if (TextUtils.isEmpty(et_school_name.getText().toString())) {
            UIUtil.showToast("学校名称不能为空");
            return false;
        }
        if (TextUtils.isEmpty(et_profession_name.getText().toString())) {
            UIUtil.showToast("专业不能为空");
            return false;
        }
        if (TextUtils.isEmpty(et_education.getText().toString())) {
            UIUtil.showToast("学历不能为空");
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
        return true;
    }

    /**
     * 根据对应的分类id获取对应的数据
     */

    private void getTagItmes() {
        List<String> list = new ArrayList<>();
        list.add("QS_education");
//        list.add("QS_experience");
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
