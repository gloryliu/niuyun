package com.niuyun.hire.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.utils.timepicker.TimePickerView;

import java.util.Date;

/**
 * Created by liu.zhenrong on 2016/5/7.
 */
public class DialogUtils {
    private static Dialog dialog;
    private static Dialog voiceDialog;

    /**
     * 此方法描述的是： 显示全屏半透明的dialog
     */
    public static void showDialog(Context context, String message, boolean cancelable) {
        closeDialog();
        dialog = new Dialog(context, R.style.Custom_Progress);
        dialog.setTitle("");
        dialog.setContentView(R.layout.progress_dialog);
        if (message == null) {
            dialog.findViewById(R.id.id_tv_loadingmsg).setVisibility(View.GONE);
        } else if (message.length() == 0) {
            TextView txt = (TextView) dialog.findViewById(R.id.id_tv_loadingmsg);
            txt.setText("加载中");
        } else {
            TextView txt = (TextView) dialog.findViewById(R.id.id_tv_loadingmsg);
            txt.setText(message);
        }

//        AnimationDrawable animation = (AnimationDrawable) ((ImageView) dialog.findViewById(R.id.loadingImageView)).getBackground();
//        if (animation != null) {
//            animation.start();
//        }
        // 按返回键是否取消
        dialog.setCancelable(cancelable);
        // 设置居中
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
//        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//        // 设置背景层透明度
//        lp.dimAmount = 0.2f;
//        dialog.getWindow().setAttributes(lp);
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 此方法描述的是： 关闭半透明的dialog
     */
    public static void closeDialog() {
        try {
            if (dialog != null) {
                dialog.cancel();
                dialog.dismiss();
                dialog = null;
            }
        } catch (Exception e) {

        }
    }

    /**
     * @param context
     * @param onClickListener 提示dialog
     * @param title           标题，
     * @param content         内容
     *                        带有确认，取消按钮回调
     */
    public static void commonDialog(final Context context, String title, String content, final View.OnClickListener onClickListener) {
        View dialogView = View.inflate(context, R.layout.custom_progress_dialog_delete, null);
        final Dialog dialog = new Dialog(context, R.style.dialog_styles);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        TextView tv_dialogtitle = (TextView) dialogView.findViewById(R.id.tv_dialogtitle);
        tv_dialogtitle.setText(title);
        TextView tv_dialog_content = (TextView) dialogView.findViewById(R.id.tv_dialog_content);
        if (!TextUtils.isEmpty(content)) {
            tv_dialog_content.setText(content);
            tv_dialog_content.setVisibility(View.VISIBLE);
        } else {
            tv_dialog_content.setVisibility(View.GONE);
        }
        dialogView.findViewById(R.id.tv_cancelNO).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag("取消");
                onClickListener.onClick(v);
                dialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.tv_cancelOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag("确定");
                onClickListener.onClick(v);
                dialog.dismiss();
            }
        });
        dialog.setContentView(dialogView);
        dialog.setCancelable(true);
        dialog.show();
    }

    /**
     * @param context
     * @param onClickListener 提示dialog
     * @param title           标题，
     *                        带有确认，取消按钮回调
     */
    public static void showOrderCancelMsg(final Context context, String title, final View.OnClickListener onClickListener) {
        View dialogView = View.inflate(context, R.layout.custom_progress_dialog_delete, null);
        final Dialog dialog = new Dialog(context, R.style.dialog_styles);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        TextView tv_dialogtitle = (TextView) dialogView.findViewById(R.id.tv_dialogtitle);
        tv_dialogtitle.setText(title);

        dialogView.findViewById(R.id.tv_cancelNO).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag("取消");
                onClickListener.onClick(v);
                dialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.tv_cancelOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag("确定");
                onClickListener.onClick(v);
                dialog.dismiss();
            }
        });
        dialog.setContentView(dialogView);
        dialog.setCancelable(true);
        dialog.show();
    }

    /**
     * 时间选择器
     *
     * @param context
     * @param listener
     */
    private static TimePickerView pvTime;

    public static void showTimePcikerDialog(final Context context, final TimePickerView.OnTimeSelectListener listener) {
        //时间选择器
        pvTime = new TimePickerView(context, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        pvTime.setTitle("请选择日期");
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                if (listener != null) {
                    listener.onTimeSelect(date);
                    pvTime.dismiss();
                }
            }
        });
        pvTime.show();
    }

    public static void showTimePcikerDialog(final Context context, final TimePickerView.Type type, final TimePickerView.OnTimeSelectListener listener) {
        //时间选择器
        if (type != null) {
            pvTime = new TimePickerView(context, type);
        } else {
            pvTime = new TimePickerView(context, TimePickerView.Type.YEAR_MONTH_DAY);
        }
//        pvTime.setRange(Integer.parseInt(UIUtil.getTime(new Date(), "yyyy")), Integer.parseInt(UIUtil.getTime(new Date(), "yyyy")) + 10);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        pvTime.setTitle("请选择日期");
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                if (listener != null) {
                    listener.onTimeSelect(date);
                    pvTime.dismiss();
                }
            }
        });
        pvTime.show();
    }
}
