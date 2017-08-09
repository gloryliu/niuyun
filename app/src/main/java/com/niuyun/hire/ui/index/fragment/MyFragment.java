package com.niuyun.hire.ui.index.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.BaseFragment;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.activity.EditResumeActivity;
import com.niuyun.hire.ui.activity.LoginActivity;
import com.niuyun.hire.ui.polyvLive.activity.PolyvUploadActivity;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.view.CircularImageView;

import org.greenrobot.eventbus.EventBus;

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
    }

    @Override
    protected void loadData() {
        setUserInfo();
    }

    private void setUserInfo() {
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
            if (eventBusCenter.getEvenCode() == Constants.LOGIN_SUCCESS || eventBusCenter.getEvenCode() == Constants.LOGIN_FAILURE) {
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
                BaseContext.getInstance().Exit();

                //设置
//                if (BaseContext.getInstance().getUserInfo() == null) {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                    return;
//                }
//                startActivity(new Intent(getActivity(), PersonInformationActivity.class));
                break;
            case R.id.rl_video_resume:
                //视频简历
//                Intent intent = new Intent(getActivity(), CompanyDetailsActivity.class);
                Intent intent = new Intent(getActivity(), PolyvUploadActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_stars:
                //我的关注
                break;
            case R.id.rl_watched:
                //谁看过我
                break;
            case R.id.tv_edit_resume:
                //编辑简历
                Intent intent1 = new Intent(getActivity(), EditResumeActivity.class);
                startActivity(intent1);
                break;
        }
    }

    private void showExitDialog() {
        DialogUtils.showOrderCancelMsg(getActivity(), "确定要退出登录吗？", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getTag().equals("确定")) {
                    BaseContext.getInstance().Exit();
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                    EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.LOGIN_FAILURE));
                }

            }

//            @Override
//            public void callBack() {//退出登录
//
//            }
        });
    }
}
