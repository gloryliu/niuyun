package com.niuyun.hire.ui.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.adapter.PreViewResumeIntentListAdapter;
import com.niuyun.hire.ui.adapter.PreviewResumeEducationAdapter;
import com.niuyun.hire.ui.adapter.PreviewResumeExperienceAdapter;
import com.niuyun.hire.ui.bean.PreviewResumeBean;
import com.niuyun.hire.ui.chat.ui.ChatActivity;
import com.niuyun.hire.ui.live.common.widget.CommonLivePlayerView;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.CircularImageView;
import com.niuyun.hire.view.TitleBar;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-8-10.
 */

public class PreviewResumeActivity extends BaseActivity {
    @BindView(R.id.title_view)
    TitleBar titleView;
    @BindView(R.id.iv_head)
    CircularImageView iv_head;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.tv_education)
    TextView tv_education;
    @BindView(R.id.tv_work_age)
    TextView tv_work_age;
    @BindView(R.id.tv_birthday)
    TextView tv_birthday;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_email)
    TextView tv_email;
    @BindView(R.id.tv_evaluation)
    TextView tv_evaluation;
    @BindView(R.id.iv_sex)
    ImageView iv_sex;
    @BindView(R.id.rv_intent_position)
    RecyclerView rv_intent_position;
    @BindView(R.id.rv_work_experience)
    RecyclerView rv_work_experience;
    @BindView(R.id.rv_education)
    RecyclerView rv_education;
    @BindView(R.id.ns_scroll)
    NestedScrollView ns_scroll;
    @BindView(R.id.ll_polyv)
    LinearLayout ll_polyv;
    @BindView(R.id.ll_base_info)
    LinearLayout ll_base_info;
    @BindView(R.id.pv_play)
    CommonLivePlayerView pv_play;
    @BindView(R.id.bt_talk)
    Button bt_talk;
    private PreviewResumeBean previewResumeBean;
    private Call<PreviewResumeBean> previewResumeCall;
    private String uid;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_preciew_resume;
    }

    @Override
    public void initViewsAndEvents() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            previewResumeBean = (PreviewResumeBean) bundle.getSerializable("resumeBean");
            uid = bundle.getString("uid");
        }
        initTitle();
        bt_talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BaseContext.getInstance().getUserInfo() == null) {
                    startActivity(new Intent(PreviewResumeActivity.this, LoginActivity.class));
                } else {
                    if (previewResumeBean != null) {
                        Intent intent = new Intent(PreviewResumeActivity.this, ChatActivity.class);
                        intent.putExtra(Constants.EXTRA_USER_ID, "niuyunApp" + uid);
                        startActivity(intent);
                    }

                }
            }
        });
    }

    @Override
    public void loadData() {
        initData();
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
        if (TextUtils.isEmpty(uid)) {
            titleView.setTitle("预览简历");
            titleView.setTitleColor(getResources().getColor(R.color.color_333333));
            titleView.setBackgroundColor(Color.WHITE);
            titleView.setImmersive(true);
        } else {
            titleView.setTitle("候选人简历");
            titleView.setTitleColor(Color.WHITE);
            titleView.setBackgroundColor(getResources().getColor(R.color.color_ea0000));
            titleView.setImmersive(true);
        }

//        setBarTintColor(Color.WHITE);

    }

    private void initData() {
        if (previewResumeBean != null && previewResumeBean.getData() != null) {
            ImageLoadedrManager.getInstance().display(this, Constants.COMMON_PERSON_URL + previewResumeBean.getData().getAvatars(), iv_head,R.mipmap.ic_default_head);
            tv_name.setText(previewResumeBean.getData().getRealname());
            if (!TextUtils.isEmpty(previewResumeBean.getData().getSexCn()) && previewResumeBean.getData().getSexCn().equals("男")) {
                iv_sex.setImageResource(R.mipmap.ic_sex_man_normal);
            } else if (!TextUtils.isEmpty(previewResumeBean.getData().getSexCn()) && previewResumeBean.getData().getSexCn().equals("女")) {
                iv_sex.setImageResource(R.mipmap.ic_sex_woman_normal);
            }
            tv_content.setText(previewResumeBean.getData().getResidence() + "|" + previewResumeBean.getData().getExperienceCn() + "|" + previewResumeBean.getData().getEducationCn());
            tv_evaluation.setText(previewResumeBean.getData().getSpecialty());
            //
            if (!TextUtils.isEmpty(uid)) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                pv_play.setTransaction(ft);
                pv_play.setmVideoPath(previewResumeBean.getData().getVideo());
                ll_base_info.setVisibility(View.GONE);
                ll_polyv.setVisibility(View.VISIBLE);
                bt_talk.setVisibility(View.VISIBLE);
            } else {
                tv_education.setText(previewResumeBean.getData().getEducationCn());
                tv_work_age.setText(previewResumeBean.getData().getExperienceCn());
                tv_birthday.setText(previewResumeBean.getData().getBirthdate());
                tv_phone.setText(previewResumeBean.getData().getTelephone());
                tv_email.setText(previewResumeBean.getData().getEmail());
                ll_base_info.setVisibility(View.VISIBLE);
                ll_polyv.setVisibility(View.GONE);
                bt_talk.setVisibility(View.GONE);
            }


//            ns_scroll.setNestedScrollingEnabled(false);
            if (previewResumeBean.getData().getIntentions() != null) {
                rv_intent_position.setLayoutManager(new LinearLayoutManager(this));
                PreViewResumeIntentListAdapter intentAdapter = new PreViewResumeIntentListAdapter(this, previewResumeBean.getData().getIntentions());
                rv_intent_position.setAdapter(intentAdapter);
            }
            if (previewResumeBean.getData().getResumeWork() != null) {
                rv_work_experience.setLayoutManager(new LinearLayoutManager(this));
                PreviewResumeExperienceAdapter experienceAdapter = new PreviewResumeExperienceAdapter(this, previewResumeBean.getData().getResumeWork());
                rv_work_experience.setAdapter(experienceAdapter);
            }


            if (previewResumeBean.getData().getResumeEducation() != null) {
                rv_education.setLayoutManager(new LinearLayoutManager(this));
                PreviewResumeEducationAdapter educationAdapter = new PreviewResumeEducationAdapter(this, previewResumeBean.getData().getResumeEducation());
                rv_education.setAdapter(educationAdapter);
            }


        } else {
            if (TextUtils.isEmpty(uid)) {
                UIUtil.showToast("获取简历id失败");
                finish();
            } else {
                getResume();
            }
        }

    }

    /**
     * 获取预览简历数据
     */
    private void getResume() {
        DialogUtils.showDialog(this, "加载中...", false);
        if (BaseContext.getInstance().getUserInfo() != null) {

            previewResumeCall = RestAdapterManager.getApi().previewResume(uid, BaseContext.getInstance().getUserInfo().companyId + "");
        } else {
            previewResumeCall = RestAdapterManager.getApi().previewResume(uid, "0");
        }
        previewResumeCall.enqueue(new JyCallBack<PreviewResumeBean>() {
            @Override
            public void onSuccess(Call<PreviewResumeBean> call, Response<PreviewResumeBean> response) {
                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    previewResumeBean = response.body();
                    initData();

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
    protected void onResume() {
        super.onResume();
        pv_play.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pv_play.onPause();
    }
    @Override
    protected void onDestroy() {
        if (pv_play!=null){
            pv_play.onDestroy();
        }
        super.onDestroy();
    }
}
