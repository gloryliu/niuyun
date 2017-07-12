package com.niuyun.hire.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.niuyun.hire.R;


/**
 * Created by zhao.wenchao on 2016/12/28.
 * email: zhao.wenchao@jyall.com
 * introduce: 统一标题导航布局
 */

public class CommonTitleView extends FrameLayout {
    TitleRightClickListener titleRightClickListener;
    private ImageView mLeftImg;
    private TextView mLeftMsg;
    private TextView mTitle;
    private View mLineView;
    private TitleLeftIconClickListener leftIconClickListener;
    private TitleOnclickListerner titleOnclickListerner;
    private TextView right_text;//右边按钮的颜色


    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.left_icon:
                    leftClick();
                    break;
                case R.id.title_text:
                    if (titleOnclickListerner != null) {
                        titleOnclickListerner.clickTitle();
                    }
                    break;
                case R.id.right_text: {
                    if (titleRightClickListener != null) {
                        titleRightClickListener.clickRight();
                    }
                }
                break;
            }
        }
    };

    public CommonTitleView(Context context) {
        super(context);
        initView();
    }

    public CommonTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CommonTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View titleView = LayoutInflater.from(getContext()).inflate(R.layout.simple_common_title, null);
        mLeftImg = (ImageView) titleView.findViewById(R.id.left_icon);
        mLeftMsg = (TextView) titleView.findViewById(R.id.left_text);
        mTitle = (TextView) titleView.findViewById(R.id.title_text);
        mLineView = titleView.findViewById(R.id.line);
        mLeftImg.setOnClickListener(mClickListener);
        mTitle.setOnClickListener(mClickListener);
        right_text = (TextView) titleView.findViewById(R.id.right_text);
        right_text.setOnClickListener(mClickListener);
        addView(titleView);

    }

    private void leftClick() {
        if (leftIconClickListener != null) {
            leftIconClickListener.clickLeftIcon();
        } else {
            if (getContext() instanceof Activity) {
                ((Activity) getContext()).finish();
            }
        }
    }

    /**
     * @param leftIcon 左侧图标
     * @param leftMsg  左侧图标右边文字
     * @param title    标题
     */
    public void setVisibleElement(int leftIcon, int leftMsg, int title) {
        mLeftImg.setVisibility(leftIcon);
        mLeftMsg.setVisibility(leftMsg);
        mTitle.setVisibility(title);
    }

    public void setUnderLineVisible(int visible) {
        mLineView.setVisibility(visible);
    }

    /**
     * @param msg 设置标题
     */
    public void setTitleMsg(String msg) {
        mTitle.setText(msg);
    }

    /**
     * @param id 设置标题
     */
    public void setTitleMsg(int id) {
        mTitle.setText(id);
    }

    /**
     * @param msg 设置返回箭头右侧文字
     */
    public CommonTitleView setLeftMsg(String msg) {
        if (msg != null) {
            mLeftImg.setVisibility(View.GONE);
            mLeftMsg.setVisibility(View.VISIBLE);
            mLeftMsg.setText(msg);
            mLeftMsg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    leftClick();
                }
            });
        }
        return this;
    }

    /**
     * @return 左侧文字信息组件
     */
    public TextView getmLeftMsg(){
        return mLeftMsg;
    }

    /**
     * @return 获取右侧文字信息组件
     */
    public TextView getRightText(){
        return right_text;
    }

    /**
     * @return 获取左侧图片组件
     */
    public ImageView getmLeftImg(){
        return mLeftImg;
    }

    public void setTitleOnclickListerner(TitleOnclickListerner titleOnclickListerner) {
        this.titleOnclickListerner = titleOnclickListerner;
    }

    public CommonTitleView setTitleRightClickListener(TitleRightClickListener titleRightClickListener) {
        this.titleRightClickListener = titleRightClickListener;
        return this;
    }

    public CommonTitleView setRightText(@StringRes int rightText) {
        if (rightText != 0) {
            right_text.setVisibility(View.VISIBLE);
            right_text.setText(rightText);
        } else {
            right_text.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置左边文字内容
     *
     * @param leftText
     * @return
     */
    public CommonTitleView setLeftText(@StringRes int leftText) {
        if (leftText != 0) {
            right_text.setVisibility(View.VISIBLE);
            right_text.setText(leftText);
        } else {
            right_text.setVisibility(View.GONE);
        }
        return this;
    }

    public TextView getRightTextView() {
        return right_text;
    }

    public void setTitleLeftIconClickListener(TitleLeftIconClickListener leftIconListener) {
        this.leftIconClickListener = leftIconListener;
    }

    public interface TitleOnclickListerner {
        void clickTitle();
    }

    public interface TitleLeftIconClickListener {
        void clickLeftIcon();
    }

    public interface TitleRightClickListener {
        void clickRight();
    }
}
