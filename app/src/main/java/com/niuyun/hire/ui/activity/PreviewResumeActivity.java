package com.niuyun.hire.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.adapter.PreViewResumeIntentListAdapter;
import com.niuyun.hire.ui.adapter.PreviewResumeEducationAdapter;
import com.niuyun.hire.ui.adapter.PreviewResumeExperienceAdapter;
import com.niuyun.hire.ui.bean.PreviewResumeBean;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.view.CircularImageView;
import com.niuyun.hire.view.TitleBar;

import butterknife.BindView;

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
    private PreviewResumeBean previewResumeBean;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_preciew_resume;
    }

    @Override
    public void initViewsAndEvents() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            previewResumeBean = (PreviewResumeBean) bundle.getSerializable("resumeBean");
        }
        initTitle();
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

        titleView.setTitle("预览简历");
        titleView.setTitleColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        titleView.setImmersive(true);
    }

    private void initData() {
        if (previewResumeBean != null && previewResumeBean.getData() != null) {
            ImageLoadedrManager.getInstance().display(this, Constants.COMMON_PERSON_URL + previewResumeBean.getData().getAvatars(), iv_head);
            tv_name.setText(previewResumeBean.getData().getRealname());
            if (!TextUtils.isEmpty(previewResumeBean.getData().getSexCn()) && previewResumeBean.getData().getSexCn().equals("男")) {
                iv_sex.setImageResource(R.mipmap.ic_sex_man_normal);
            } else if (!TextUtils.isEmpty(previewResumeBean.getData().getSexCn()) && previewResumeBean.getData().getSexCn().equals("女")) {
                iv_sex.setImageResource(R.mipmap.ic_sex_woman_normal);
            }
            tv_content.setText(previewResumeBean.getData().getResidence() + "|" + previewResumeBean.getData().getExperienceCn() + "|" + previewResumeBean.getData().getEducationCn());
            tv_education.setText(previewResumeBean.getData().getEducationCn());
            tv_work_age.setText(previewResumeBean.getData().getExperienceCn());
            tv_birthday.setText(previewResumeBean.getData().getBirthdate());
            tv_phone.setText(previewResumeBean.getData().getTelephone());
            tv_email.setText(previewResumeBean.getData().getEmail());
            tv_evaluation.setText(previewResumeBean.getData().getSpecialty());


//            ns_scroll.setNestedScrollingEnabled(false);
            rv_intent_position.setLayoutManager(new LinearLayoutManager(this));
            PreViewResumeIntentListAdapter intentAdapter=new PreViewResumeIntentListAdapter(this,previewResumeBean.getData().getIntentions());
            rv_intent_position.setAdapter(intentAdapter);

            rv_work_experience.setLayoutManager(new LinearLayoutManager(this));
            PreviewResumeExperienceAdapter experienceAdapter = new PreviewResumeExperienceAdapter(this, previewResumeBean.getData().getResumeWork());
            rv_work_experience.setAdapter(experienceAdapter);


            rv_education.setLayoutManager(new LinearLayoutManager(this));
            PreviewResumeEducationAdapter educationAdapter = new PreviewResumeEducationAdapter(this, previewResumeBean.getData().getResumeEducation());
            rv_education.setAdapter(educationAdapter);


        }

    }
}
