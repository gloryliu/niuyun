package com.niuyun.hire.ui.index.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.BaseFragment;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.activity.AttentionListActivity;
import com.niuyun.hire.ui.activity.EditResumeActivity;
import com.niuyun.hire.ui.activity.EnterprisePositionControlActivity;
import com.niuyun.hire.ui.activity.LoginActivity;
import com.niuyun.hire.ui.activity.Settingctivity;
import com.niuyun.hire.ui.polyvLive.activity.PolyvUploadActivity;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.view.CircularImageView;

import butterknife.BindView;

/**
 * 个人页面
 * Created by chen.zhiwei on 2017-6-19.
 */

public class MyFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.iv_head)
    CircularImageView ivHead;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.rl_video_resume)
    RelativeLayout rl_video_resume;
    @BindView(R.id.rl_stars)
    RelativeLayout rl_stars;
    @BindView(R.id.rl_watched)
    RelativeLayout rl_watched;
    @BindView(R.id.rl_setting)
    RelativeLayout rl_setting;
    @BindView(R.id.tv_edit_resume)
    TextView tv_edit_resume;
    @BindView(R.id.tv_state)
    TextView tv_state;
    @BindView(R.id.ll_enterprise)
    LinearLayout ll_enterprise;
    @BindView(R.id.ll_person)
    LinearLayout ll_person;
    @BindView(R.id.rl_video_enterprise)
    RelativeLayout rl_video_enterprise;
    @BindView(R.id.rl_control_position)
    RelativeLayout rl_control_position;
    @BindView(R.id.rl_enterprise_info)
    RelativeLayout rl_enterprise_info;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_mine_layout;
    }

    @Override
    protected void initViewsAndEvents() {
        rl_video_resume.setOnClickListener(this);
        rl_stars.setOnClickListener(this);
        rl_watched.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
        tv_edit_resume.setOnClickListener(this);
        rl_enterprise_info.setOnClickListener(this);
        rl_control_position.setOnClickListener(this);
        rl_video_enterprise.setOnClickListener(this);
    }

    @Override
    protected void loadData() {
        setUserInfo();
    }

    private void setUserInfo() {
        if (BaseContext.getInstance().getUserInfo() != null) {
            if (BaseContext.getInstance().getUserInfo().utype == 1) {
                //企业
                ll_enterprise.setVisibility(View.VISIBLE);
                ll_person.setVisibility(View.GONE);
            } else if (BaseContext.getInstance().getUserInfo().utype == 2) {
                //个人
                ll_enterprise.setVisibility(View.GONE);
                ll_person.setVisibility(View.VISIBLE);
            }
            ImageLoadedrManager.getInstance().display(getActivity(), Constants.COMMON_PERSON_URL + BaseContext.getInstance().getUserInfo().avatars, ivHead);
            tv_user_name.setText(BaseContext.getInstance().getUserInfo().username);
            tv_state.setText(BaseContext.getInstance().getUserInfo().currentCn);
        } else {
//            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    @Override
    protected View isNeedLec() {
        return null;
    }

    @Override
    public boolean isRegistEventBus() {
        return true;
    }

    @Override
    public void onMsgEvent(EventBusCenter eventBusCenter) {
        if (eventBusCenter != null) {
            if (eventBusCenter.getEvenCode() == Constants.LOGIN_SUCCESS || eventBusCenter.getEvenCode() == Constants.LOGIN_FAILURE || eventBusCenter.getEvenCode() == Constants.UPDATE_PERSON) {
                //登陆成功,或者退出
                setUserInfo();
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_setting:
                //设置
                if (BaseContext.getInstance().getUserInfo() == null) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), Settingctivity.class));
                break;
            case R.id.rl_video_resume:
                //视频简历
                Intent intent = new Intent(getActivity(), PolyvUploadActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_stars:
                //我的关注

                Intent intentAttention = new Intent(getActivity(), AttentionListActivity.class);
                startActivity(intentAttention);
                break;
            case R.id.rl_watched:
                //谁看过我
                break;
            case R.id.tv_edit_resume:
                //编辑简历
                if (BaseContext.getInstance().getUserInfo().utype == 1) {
                    //企业
                } else if (BaseContext.getInstance().getUserInfo().utype == 2) {
                    //个人
                }
                Intent intent1 = new Intent(getActivity(), EditResumeActivity.class);
                startActivity(intent1);
                break;

            case R.id.rl_video_enterprise:
                //企业视频
                Intent enterpriseVideointent = new Intent(getActivity(), PolyvUploadActivity.class);
                startActivity(enterpriseVideointent);
                break;
            case R.id.rl_control_position:
                //企业职位管理
                Intent enterprisePositionintent = new Intent(getActivity(), EnterprisePositionControlActivity.class);
                startActivity(enterprisePositionintent);
                break;
            case R.id.rl_enterprise_info:
                //企业信息
                break;


        }
    }

}
