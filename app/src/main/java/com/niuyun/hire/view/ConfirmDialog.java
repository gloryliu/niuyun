package com.niuyun.hire.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.utils.UIUtil;


/**
 * Created by chen.zhiwei on 2016/3/15.
 * 基础dialog，可以定义标题，内容，确认，取消
 */
public class ConfirmDialog extends Dialog {
    View view;
    private Activity context;
    private TextView tvMessage;//内容
    private TextView tvCancle;
    private TextView tvConfirm;
    private View line;
    private TextView tv_title;//标题，默认gone

    public ConfirmDialog(Context context, String message) {
        super(context, R.style.customDialogStyle);
        this.context = (Activity) context;
        init();
        tvMessage.setText(message);

    }


    private void init() {
        view = View.inflate(context, R.layout.dialog_confirm, null);
        tvMessage = (TextView) view.findViewById(R.id.confirm_dialog_message);
        tvCancle = (TextView) view.findViewById(R.id.cancel);
        tvConfirm = (TextView) view.findViewById(R.id.confirm);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        line = view.findViewById(R.id.line);

        getWindow().setContentView(view);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = UIUtil.dip2px(context, 290);
        window.setAttributes(params);
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    /**
     * 取消按钮点击事件
     *
     * @param cancleClick
     */
    public void setCancleClick(View.OnClickListener cancleClick) {
        tvCancle.setOnClickListener(cancleClick);
    }

    /**
     * 确认按钮点击事件
     *
     * @param confirmClick
     */
    public void setConfirmClick(View.OnClickListener confirmClick) {
        tvConfirm.setOnClickListener(confirmClick);
    }

    /**
     * 取消按钮自定义文案
     *
     * @param text
     */
    public void setCancleText(String text) {
        tvCancle.setText(text);
    }

    /**
     * 确认按钮自定义文案
     *
     * @param text
     */
    public void setConfirmText(String text) {
        tvConfirm.setText(text);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        tv_title.setText(title);
        tv_title.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏取消按钮
     */
    public void hideCancleButton() {
        tvCancle.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
    }


}
