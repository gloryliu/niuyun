package com.niuyun.hire.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jyall.apkupdate.ApkDownloadService;
import com.jyall.apkupdate.ApkUpdateTool;
import com.jyall.apkupdate.OnUpdateListener;
import com.jyall.apkupdate.UpdateInfo;
import com.niuyun.hire.R;
import com.niuyun.hire.api.JyApi;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.bean.UpdateBean;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 升级模块管理
 * Created by liu.zhenrong on 2016/12/30.
 */

public class ApkUpdateManager {
    private static ApkUpdateManager apkUpdateManager;
    ApkUpdateTool updateTool;

    private ApkUpdateManager() {
    }

    public static ApkUpdateManager getInstance() {
        if (null == apkUpdateManager) {
            apkUpdateManager = new ApkUpdateManager();
        }
        return apkUpdateManager;
    }


    /**
     * 监测版本升级
     */
    public void checkVersion(final Context mContext, final boolean isShowToast) {

        if (!NetUtil.isNetworkConnected(mContext)) {
            if (isShowToast)
                UIUtil.showToast(R.string.net_state_error);
            return;
        }

        if (null != updateTool) {
            updateTool.recycle();
        }
        updateTool = ApkUpdateTool.getInstance(mContext, new OnUpdateListener() {
            @Override
            public void initDialog(UpdateInfo updateInfo) {
                showUpdateDialog(mContext, updateInfo);
            }

            @Override
            public boolean checkSDPermision() {
                return checkPermision(mContext);
            }
        });

        JyApi jyApi = RestAdapterManager.getApi();
        Call<SuperBean<UpdateBean>> call = jyApi.checkVersion("Android", "niuyunAPP", updateTool.getPackageInfo().versionCode + "");
        call.enqueue(new JyCallBack<SuperBean<UpdateBean>>() {
            @Override
            public void onSuccess(Call<SuperBean<UpdateBean>> call, Response<SuperBean<UpdateBean>> response) {
                try {
                    UpdateBean updateInfo = response.body().getData();
                    if (updateInfo != null) {
                        if (Integer.parseInt(updateInfo.version) <= updateTool.getPackageInfo().versionCode) {
                            EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.SHOW_POP_AD));
                        }
                        updateTool.updateVersion(updateInfo, isShowToast);
                    } else {
                        if (isShowToast)
                            UIUtil.showToast(R.string.last_version);
                    }
                } catch (Exception e) {

                }


            }

            @Override
            public void onError(Call<SuperBean<UpdateBean>> call, Throwable t) {
                if (isShowToast)
                    UIUtil.showToast(R.string.last_version);
                EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.SHOW_POP_AD));
            }

            @Override
            public void onError(Call<SuperBean<UpdateBean>> call, Response<SuperBean<UpdateBean>> response) {
                try {
                    EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.SHOW_POP_AD));
                    if (isShowToast)
                        ErrorMessageUtils.taostErrorMessage(mContext, response.errorBody().string(),
                                mContext.getString(R.string.last_version));
                } catch (IOException e) {
                    LogUtils.e(e.getMessage());
                }
            }
        });

    }

    /**
     * 监测权限
     *
     * @return
     */
    private boolean checkPermision(Context mContext) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions((Activity) mContext,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        2);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * 初始化升级弹窗UI
     *
     * @param mContext
     * @param updateInfo
     */
    private void showUpdateDialog(Context mContext, final UpdateInfo updateInfo) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_version_update, null);
        TextView info = (TextView) view.findViewById(R.id.tv_info);
        info.setText(Html.fromHtml(updateInfo.versionInfo));
        TextView mNegativeBtn = (TextView) view.findViewById(R.id.positive_btn);
        ImageView mPositiveBtn = (ImageView) view.findViewById(R.id.negative_btn);
//        View line = view.findViewById(R.id.upadte_dialog_line);
        ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        final Dialog dialog = new Dialog(mContext, R.style.version_dialog);
        dialog.setContentView(view);
        if (updateInfo.forceUpdate == 1) {
            dialog.setCanceledOnTouchOutside(false);
            mPositiveBtn.setVisibility(View.GONE);
//            line.setVisibility(View.GONE);
        } else {
            mPositiveBtn.setVisibility(View.VISIBLE);
//            line.setVisibility(View.VISIBLE);
        }
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (updateInfo.forceUpdate == 1) {
                        return true;
                    }
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                }
                return false;
            }
        });

        mNegativeBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {//确认更新
                Intent intent = new Intent(dialog.getContext(), ApkDownloadService.class);
                intent.putExtra("fileName",R.string.app_name);
                intent.putExtra("url", updateInfo.updateUrl);
                dialog.getContext().startService(intent);
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        mPositiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = UIUtil.dip2px(dialog.getContext(), 310);
        window.setAttributes(params);
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.SHOW_POP_AD));
            }
        });
    }
}
