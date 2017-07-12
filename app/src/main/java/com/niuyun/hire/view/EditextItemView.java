package com.niuyun.hire.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.niuyun.hire.R;


/**
 * 菜单view
 * Created by liu.zhenrong on 2016/12/29.
 */

public class EditextItemView extends LinearLayout {

    private String leftLable;//左边标签文本
    private String editextHint;//编辑框提示语
    private int rightSrcId;//右边图片
    private int right_image_visibility;//右边图片是否显示

    private TextView tv_left_lable;

    private ImageView iv_right_icon;
    private EditText editext;

    public EditextItemView(Context context) {
        super(context);
        init(context, null);
    }

    public EditextItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditextItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EdiItem);
            leftLable = ta.getString(R.styleable.EdiItem_lf_label);
            editextHint = ta.getString(R.styleable.EdiItem_edt_hint);
            rightSrcId = ta.getResourceId(R.styleable.EdiItem_rt_src, -1);

            right_image_visibility = ta.getInt(R.styleable.EdiItem_rt_img_visibility, 0);
            ta.recycle();
        }
        View.inflate(context, R.layout.item_editext, this);
        iv_right_icon = (ImageView) findViewById(R.id.iv_right_icon);

        tv_left_lable = (TextView) findViewById(R.id.tv_left_lable);
        editext = (EditText) findViewById(R.id.editext);
        editext.setHint(editextHint);


        convert(iv_right_icon, right_image_visibility);


        if (rightSrcId > 0) {
            iv_right_icon.setImageResource(rightSrcId);
        }

        tv_left_lable.setText(leftLable);



    }


    /**
     * 设置是否显示
     *
     * @param view
     * @param type
     */
    private void convert(View view, int type) {
        switch (type) {
            case 0:
                view.setVisibility(VISIBLE);
                break;
            case 4:
                view.setVisibility(INVISIBLE);
                break;
            case 8:
                view.setVisibility(GONE);
                break;

        }
    }


    /**
     * @return 获取左侧文本组件
     */
    public TextView getLeftLable() {
        return tv_left_lable;
    }

    /**
     * @return 获取编辑框组件
     */
    public EditText getEditext() {
        return editext;
    }

    /**
     * @return 获取 右侧图片组件
     */
    public ImageView getRightIcon(){return iv_right_icon;}
}
