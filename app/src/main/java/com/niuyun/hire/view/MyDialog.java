package com.niuyun.hire.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 公共dialog可以自定义layout初始化给view参数
 *
 * @author neusoft
 */
public class MyDialog extends Dialog {

    public MyDialog(Context context) {
        super(context);
    }

    private static int default_width = 160; //宽

    private static int default_height = 240;//高

    public MyDialog(Context context, View layout, int style) {
        this(context, default_width, default_height, layout, style);
    }

    public MyDialog(Context context, int width, int height, View layout, int style) {
        super(context, style);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        if (height>0){
            params.height=height;
        }
        window.setAttributes(params);
    }
    public MyDialog(Context context, int width, int height,int gravity, View layout, int style) {
        super(context, style);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = gravity;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        if (height>0){
            params.height=height;
        }
        window.setAttributes(params);
    }
}
