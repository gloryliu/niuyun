package com.niuyun.hire.ui.index.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.BaseFragment;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.activity.AboutActivity;
import com.niuyun.hire.ui.activity.AccountSafetyActivity;
import com.niuyun.hire.ui.activity.CommitRealNameActivity;
import com.niuyun.hire.ui.activity.LoginActivity;
import com.niuyun.hire.ui.activity.MyMemberRankActivity;
import com.niuyun.hire.ui.activity.OrderListActivity;
import com.niuyun.hire.ui.activity.PersonInformationActivity;
import com.niuyun.hire.ui.activity.ShoppingAddressActivity;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.view.CircularImageView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by chen.zhiwei on 2017-6-19.
 */

public class MyFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.iv_head)
    CircularImageView ivHead;
    @BindView(R.id.tv_member)
    TextView tvMember;
    @BindView(R.id.iv_image_order)
    ImageView ivImageOrder;
    @BindView(R.id.rl_order)
    RelativeLayout rlOrder;
    @BindView(R.id.iv_safe)
    ImageView ivSafe;
    @BindView(R.id.rl_safety)
    RelativeLayout rlSafety;
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    @BindView(R.id.rl_location)
    RelativeLayout rlLocation;
    @BindView(R.id.iv_mumber)
    ImageView ivMumber;
    @BindView(R.id.rl_member)
    RelativeLayout rlMember;
    @BindView(R.id.iv_real_name)
    ImageView ivRealName;
    @BindView(R.id.rl_real_name)
    RelativeLayout rlRealName;
    @BindView(R.id.iv_abount)
    ImageView ivAbount;
    @BindView(R.id.rl_about)
    RelativeLayout rlAbout;
    @BindView(R.id.tv_setting)
    TextView tv_setting;
    @BindView(R.id.rl_quit_login)
    RelativeLayout rl_quit_login;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.rb_rank)
    RatingBar rb_rank;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_mine_layout;
    }

    @Override
    protected void initViewsAndEvents() {
        rlOrder.setOnClickListener(this);
        rlSafety.setOnClickListener(this);
        rlLocation.setOnClickListener(this);
        rlMember.setOnClickListener(this);
        rlRealName.setOnClickListener(this);
        rlAbout.setOnClickListener(this);
        tv_setting.setOnClickListener(this);
        rl_quit_login.setOnClickListener(this);
    }

    @Override
    protected void loadData() {
        setUserInfo();
    }

    private void setUserInfo() {
        if (BaseContext.getInstance().getUserInfo() != null) {
            rl_quit_login.setVisibility(View.VISIBLE);
            tv_user_name.setText(BaseContext.getInstance().getUserInfo().nickname);
            if (BaseContext.getInstance().getUserInfo().vipgrade > 0) {
                rb_rank.setNumStars(BaseContext.getInstance().getUserInfo().vipgrade);
                rb_rank.setVisibility(View.VISIBLE);
            } else {
                rb_rank.setVisibility(View.GONE);
            }
            ImageLoadedrManager.getInstance().display(getActivity(), BaseContext.getInstance().getUserInfo().headimg, ivHead, R.mipmap.ic_head_default, R.mipmap.ic_head_default);
        } else {
            rl_quit_login.setVisibility(View.GONE);
            tv_user_name.setText("");
            rb_rank.setVisibility(View.GONE);
//            ImageLoadedrManager.getInstance().display(getActivity(), "", ivHead, R.mipmap.ic_head_default, R.mipmap.ic_head_default);
            ivHead.setImageResource(R.mipmap.ic_head_default);
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
            case R.id.rl_order:
                //订单
                if (BaseContext.getInstance().getUserInfo() == null) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), OrderListActivity.class));
                break;
            case R.id.rl_safety:
                //账户安全
                if (BaseContext.getInstance().getUserInfo() == null) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), AccountSafetyActivity.class));
                break;
            case R.id.rl_location:
                //收货地址
                if (BaseContext.getInstance().getUserInfo() == null) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), ShoppingAddressActivity.class));

                break;
            case R.id.rl_member:
                //会员
                if (BaseContext.getInstance().getUserInfo() == null) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), MyMemberRankActivity.class));
                break;
            case R.id.rl_real_name:
                //实名制
                if (BaseContext.getInstance().getUserInfo() == null) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), CommitRealNameActivity.class));
                break;
            case R.id.rl_about:
                //关于
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.tv_setting:
                //设置
                if (BaseContext.getInstance().getUserInfo() == null) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), PersonInformationActivity.class));
                break;
            case R.id.rl_quit_login:
                //退出登录
                if (BaseContext.getInstance().getUserInfo() == null) {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                showExitDialog();
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
