package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.bean.PreviewResumeBean;
import com.niuyun.hire.ui.chat.Constant;
import com.niuyun.hire.ui.chat.ui.ChatActivity;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.CircularImageView;
import com.niuyun.hire.view.TitleBar;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-9-5.
 */

public class AroundPersonActivity extends BaseActivity {
    @BindView(R.id.title_view)
    TitleBar titleView;
    @BindView(R.id.bt_talk)
    Button bt_talk;
    @BindView(R.id.iv_head)
    CircularImageView iv_head;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.tv_current_company_name)
    TextView tv_current_company_name;
    @BindView(R.id.tv_evaluation)
    TextView tv_evaluation;
    private PreviewResumeBean previewResumeBean;
    private Call<PreviewResumeBean> previewResumeCall;
    private String uid;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_around_person;
    }

    @Override
    public void initViewsAndEvents() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            uid = bundle.getString("uid");
        }
        initTitle();
        bt_talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BaseContext.getInstance().getUserInfo() == null) {
                    startActivity(new Intent(AroundPersonActivity.this, LoginActivity.class));
                } else {
                    if (previewResumeBean != null) {
                        Intent intent = new Intent(AroundPersonActivity.this, ChatActivity.class);
                        intent.putExtra(Constant.EXTRA_USER_ID, "niuyunApp" + uid);
                        startActivity(intent);
                    }

                }
            }
        });
    }

    @Override
    public void loadData() {
        getResume();
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
        if (previewResumeBean != null && previewResumeBean.getData() != null) {
            ImageLoadedrManager.getInstance().display(this, Constants.COMMON_PERSON_URL + previewResumeBean.getData().getAvatars(), iv_head, R.mipmap.ic_default_head);
            tv_name.setText(previewResumeBean.getData().getRealname() + "|" + previewResumeBean.getData().getSexCn());
            tv_content.setText(previewResumeBean.getData().getResidence() + "|" + previewResumeBean.getData().getExperienceCn() + "|" + previewResumeBean.getData().getEducationCn());
//            tv_current_company_name.setText(previewResumeBean.getData().get);
            tv_evaluation.setText(previewResumeBean.getData().getSpecialty());

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

    private void initTitle() {
        titleView.setTitle("附近");
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
        titleView.setImmersive(true);

    }
}
