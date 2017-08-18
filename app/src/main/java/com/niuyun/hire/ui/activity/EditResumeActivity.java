package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.adapter.EditResumeEducationAdapter;
import com.niuyun.hire.ui.adapter.EditResumeWorkExperienceAdapter;
import com.niuyun.hire.ui.bean.PreviewResumeBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.ui.utils.LoginUtils;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.CircularImageView;
import com.niuyun.hire.view.ColorfulRingProgressView;
import com.niuyun.hire.view.TitleBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-7-26.
 */

public class EditResumeActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_view)
    TitleBar titleView;
    @BindView(R.id.iv_add_experience)
    ImageView iv_add_experience;//添加工作经历
    @BindView(R.id.iv_add_education)
    ImageView iv_add_education;//添加教育经历
    @BindView(R.id.rl_self_evaluation)
    RelativeLayout rl_self_evaluation;
    @BindView(R.id.crpv_progress)
    ColorfulRingProgressView crpv_progress;
    @BindView(R.id.tvPercent)
    TextView tvPercent;
    @BindView(R.id.iv_head)
    CircularImageView iv_head;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.tv_intent)
    TextView tv_intent;
    @BindView(R.id.rv_work_experience)
    RecyclerView rv_work_experience;
    @BindView(R.id.rv_education)
    RecyclerView rv_education;
    @BindView(R.id.ns_content)
    NestedScrollView ns_content;
    @BindView(R.id.bt_preview)
    Button bt_preview;
    @BindView(R.id.tv_edit_resume)
    TextView tv_edit_resume;
    @BindView(R.id.rl_control_resume_intent)
    RelativeLayout rl_control_resume_intent;
    private Call<PreviewResumeBean> previewResumeCall;
    private PreviewResumeBean previewResumeBean;
    private EditResumeWorkExperienceAdapter workExperienceAdapter;
    private EditResumeEducationAdapter workEducationAdapter;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_edit_resume_layout;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        iv_add_experience.setOnClickListener(this);
        iv_add_education.setOnClickListener(this);
        rl_self_evaluation.setOnClickListener(this);
        bt_preview.setOnClickListener(this);
        rl_control_resume_intent.setOnClickListener(this);
        tv_edit_resume.setOnClickListener(this);
        init();
    }

    @Override
    public void loadData() {
        getResume();
    }

    @Override
    public boolean isRegistEventBus() {
        return true;
    }

    @Override
    public void onMsgEvent(EventBusCenter eventBusCenter) {
        if (eventBusCenter != null) {
            if (eventBusCenter.getEvenCode() == Constants.UPDATE_LIVE_RESUME) {
                //更新简历
                getResume();
                LoginUtils.getUserByUid();
                EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.UPDATE_PERSON));
            }
        }
    }

    @Override
    protected View isNeedLec() {
        return null;
    }

    private void initTitle() {

        titleView.setTitle("在线简历");
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
        titleView.addAction(new TitleBar.TextAction("预览") {
            @Override
            public void performAction(View view) {
//预览
                goPreview();
            }
        });
        titleView.setImmersive(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_experience:
                startActivity(new Intent(this, EditWorkExperienceActivity.class));
                break;
            case R.id.iv_add_education:
                startActivity(new Intent(this, EditEducationActivity.class));
                break;
            case R.id.rl_self_evaluation:
                startActivity(new Intent(this, EditEvaluationSelfActivity.class));
                break;
            case R.id.bt_preview:
                goPreview();
                break;
            case R.id.tv_edit_resume:
                startActivity(new Intent(this, PersonInformationActivity.class));
                break;
            case R.id.rl_control_resume_intent:
                startActivity(new Intent(this, ControlPositionIntentActivity.class));
                break;

        }
    }

    private void goPreview() {
        if (previewResumeBean != null && previewResumeBean.getData() != null) {
            Intent intent = new Intent(this, PreviewResumeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("resumeBean", previewResumeBean);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    private void init() {
//        ns_content.setNestedScrollingEnabled(false);
        rv_work_experience.setLayoutManager(new LinearLayoutManager(this));
        workExperienceAdapter = new EditResumeWorkExperienceAdapter(this);
        rv_work_experience.setAdapter(workExperienceAdapter);
        workExperienceAdapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {
//工作经历
            }
        });


        rv_education.setLayoutManager(new LinearLayoutManager(this));
        workEducationAdapter = new EditResumeEducationAdapter(this);
        rv_education.setAdapter(workEducationAdapter);
        workEducationAdapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {
//教育经历
            }
        });
    }

    /**
     * 设置数据
     */
    private void setDate() {
        if (previewResumeBean != null && previewResumeBean.getData() != null) {
            crpv_progress.setPercent((float) previewResumeBean.getData().getCompletePercent());
            tvPercent.setText(previewResumeBean.getData().getCompletePercent() + "");
            try {
                ImageLoadedrManager.getInstance().display(this, Constants.COMMON_PERSON_URL + previewResumeBean.getData().getAvatars(), iv_head);
            } catch (Exception e) {
            }

            tv_user_name.setText(previewResumeBean.getData().getRealname() + "|" + previewResumeBean.getData().getSexCn());
            tv_intent.setText(BaseContext.getInstance().getUserInfo().currentCn);
            workExperienceAdapter.ClearData();
            workExperienceAdapter.addList(previewResumeBean.getData().getResumeWork());
            workEducationAdapter.ClearData();
            workEducationAdapter.addList(previewResumeBean.getData().getResumeEducation());

        }
    }

    /**
     * 获取预览简历数据
     */
    private void getResume() {
        DialogUtils.showDialog(this, "加载中...", false);
        previewResumeCall = RestAdapterManager.getApi().previewResume(BaseContext.getInstance().getUserInfo().uid + "");
        previewResumeCall.enqueue(new JyCallBack<PreviewResumeBean>() {
            @Override
            public void onSuccess(Call<PreviewResumeBean> call, Response<PreviewResumeBean> response) {
                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    previewResumeBean = response.body();
                    setDate();

                } else {
                    UIUtil.showToast("接口异常");
                }
            }

            @Override
            public void onError(Call<PreviewResumeBean> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast("接口异常");
            }

            @Override
            public void onError(Call<PreviewResumeBean> call, Response<PreviewResumeBean> response) {
                DialogUtils.closeDialog();
                UIUtil.showToast("接口异常");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (previewResumeCall != null) {
            previewResumeCall.cancel();
        }
    }
}
