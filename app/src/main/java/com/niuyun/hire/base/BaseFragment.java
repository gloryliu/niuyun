package com.niuyun.hire.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.niuyun.hire.R;
import com.niuyun.hire.utils.LogUtils;
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

public abstract class BaseFragment extends Fragment {
    public View view;
    @BindString(R.string.load_ing)
    String load_ing;
    private CustomProgressDialog progressDialog;
    private EmptyLayout emptyLayout;

    public BaseFragment() {
        super();
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new CustomProgressDialog(getContext(), load_ing);
        }
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.d("onCreateView  has created");
        if (view == null) {
            view = inflater.inflate(getContentViewLayoutId(), null);
            ButterKnife.bind(this, view);
            if (null != isNeedLec()) {//需要lec
                intEmptyLyaout();
            }
            initViewsAndEvents();
            loadData();

            return view;

        } else {
            Object obj = view.getParent();
            if (obj != null && obj instanceof ViewGroup) {
                ((ViewGroup) obj).removeView(view);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.color_ff6900);
        return view;
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getActivity().getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    /***
     * 绑定资源文件ID
     *
     * @return
     */
    protected abstract int getContentViewLayoutId();

    /***
     * 绑定视图组件
     */
    protected abstract void initViewsAndEvents();

    /***
     * 加载数据
     */
    protected abstract void loadData();

    /***
     * 是否需要LCE 内容
     */
    protected abstract View isNeedLec();

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (isRegistEventBus()) {
            EventBus.getDefault().register(this);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRegistEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * @return 是否需要注册EventBus
     */
    public abstract boolean isRegistEventBus();

    /**
     * @param eventBusCenter EventBus 通知事件
     */
    public abstract void onMsgEvent(EventBusCenter eventBusCenter);

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusCenter eventBusCenter) {
        onMsgEvent(eventBusCenter);
    }


    /**
     * 初始化 EmptyLyaout
     */
    private void intEmptyLyaout() {
        if (null == emptyLayout) {
            emptyLayout = new EmptyLayout(getActivity());
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
     * @param btnMsg 空布局刷新按钮文案修改
     */
    public void setEmptyBtnRes(String btnMsg) {
        if (null != emptyLayout) {
            emptyLayout.setEmptyBtnMsg(btnMsg);
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

}
