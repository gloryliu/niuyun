package com.niuyun.hire.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.niuyun.hire.R;
import com.niuyun.hire.view.CustomProgressDialog;
import com.niuyun.hire.view.EmptyLayout;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by sun.luwei on 2016/11/28.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @BindString(R.string.load_ing)
    String load_ing;
    private EmptyLayout emptyLayout;
    private CustomProgressDialog progressDialog;

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContentViewLayoutId() != 0) {
            setContentView(getContentViewLayoutId());
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        if (null != isNeedLec()) {//需要lec
            intEmptyLyaout();
        }
        if (isRegistEventBus()) {
            EventBus.getDefault().register(this);
        }
        initViewsAndEvents();
        loadData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.color_ff6900);

    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRegistEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        AppManager.getAppManager().finishActivity(this);
    }

    public abstract int getContentViewLayoutId();

    public abstract void initViewsAndEvents();

    public abstract void loadData();

    /**
     * @return 是否需要注册EventBus
     */
    public abstract boolean isRegistEventBus();

    /**
     * @param eventBusCenter EventBus 通知事件
     */
    public abstract void onMsgEvent(EventBusCenter eventBusCenter);

    /***
     * 是否需要LCE 内容
     */
    protected abstract View isNeedLec();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusCenter eventBusCenter) {
        onMsgEvent(eventBusCenter);
    }

    /**
     * 初始化 EmptyLyaout
     */
    private void intEmptyLyaout() {
        if (null == emptyLayout) {
            emptyLayout = new EmptyLayout(this);
            emptyLayout.setView(isNeedLec());
        }
    }

    /**
     * 显示加载进度布局
     */
    public void showLoadingView() {
        if (null != emptyLayout) {
            emptyLayout.showLoading();
        }
    }

    /**
     * 恢复显示内容布局
     */
    public void showNormalConten() {
        if (null != emptyLayout) {
            emptyLayout.hideAll();
        }
    }

    /**
     * 显示错误内容布局
     */
    public void showErrorView() {
        if (null != emptyLayout) {
            emptyLayout.showError();
        }
    }

    /**
     * 显示空内容默认布局
     */
    public void showEmptyView() {
        if (null != emptyLayout) {
            emptyLayout.showEmpty();
        }
    }

    /**
     * @param btnMsg 空布局刷新按钮文案修改
     */
    public void setEmptyBtnRes(String btnMsg) {
        if (null != emptyLayout) {
            emptyLayout.setEmptyBtnMsg(btnMsg);
        }
    }

    /**
     * 显示无网络默认布局
     */
    public void showNoNetView() {
        if (null != emptyLayout) {
            setErrorRes(getString(R.string.net_state_error), R.mipmap.ic_no_net);
            emptyLayout.showError();
        }
    }

    /**
     * @param msg    loading描述
     * @param iconId loading 图片
     */
    public void setLoadingRes(String msg, int iconId) {
        if (null != emptyLayout) {
            emptyLayout.setLoadingMessage(msg);
            emptyLayout.setLoadingIcon(iconId);
        }
    }

    /**
     * @param msg    空布局描述
     * @param iconId 空布局 图片
     */
    public void setEmptyRes(String msg, int iconId) {
        if (null != emptyLayout) {
            emptyLayout.setEmptyMessage(msg);
            emptyLayout.setEmptyIcon(iconId);
        }
    }

    /**
     * @param msg    error 布局描述
     * @param iconId error 布局图片
     */
    public void setErrorRes(String msg, int iconId) {
        if (null != emptyLayout) {
            emptyLayout.setErrorMessage(msg);
            emptyLayout.setErrorIcon(iconId);
        }
    }

    /**
     * @param clickListner 设置空内容点击事件
     */
    public void setEmtyClickListner(View.OnClickListener clickListner) {
        if (null != emptyLayout) {
            emptyLayout.setEmptyClickListener(clickListner);
        }
    }

    /**
     * @param clickListner 设置错误内容点击事件
     */
    public void setErrorClickListner(View.OnClickListener clickListner) {
        if (null != emptyLayout) {
            emptyLayout.setErrorClickListener(clickListner);
        }
    }

    public void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new CustomProgressDialog(this, msg == null ? load_ing : msg);
        }
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
