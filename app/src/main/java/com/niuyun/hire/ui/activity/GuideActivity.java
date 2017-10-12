package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.adapter.ViewPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by sun.luwei on 2016/12/29.
 */

public class GuideActivity extends BaseActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener, View.OnTouchListener {


    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.iv_quick_experience)
    ImageView iv_quick_experience;

    // 定义ViewPager适配器
    ViewPagerAdapter mViewPagerAdapter;
    // 定义一个ArrayList来存放View
    private ArrayList<View> mViews;
    private Button startBtn;
    private LayoutInflater inflater;
    private boolean misScrolled;
    // 引导图片资源
    private static final int[] mGuidePics = {
            R.mipmap.ic_guide1,
            R.mipmap.ic_guide2, R.mipmap.ic_guide3
    };

    // 底部小点的图片
    private ImageView[] mPoints;
    // 记录当前选中位置
    private int currentIndex;
    private int type = 0;

    private int lastX = 0;

    @Override
    public int getContentViewLayoutId() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        return R.layout.activity_guide;
    }

    @Override
    public void initViewsAndEvents() {
        setIsOpenTitle(false);
        inflater = getLayoutInflater();
        type = getIntent().getIntExtra(Constants.INT_TAG, 0);

        // 实例化ArrayList对象
        mViews = new ArrayList<>();
        // 实例化ViewPager
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        // 实例化ViewPager适配器
        mViewPagerAdapter = new ViewPagerAdapter(mViews);

        // 定义一个布局并设置参数
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        // 初始化引导图片列表
        for (int i = 0; i < mGuidePics.length; i++) {
            ImageView mImage = new ImageView(this);
            mImage.setLayoutParams(mParams);
            mImage.setScaleType(ImageView.ScaleType.FIT_XY);
            mImage.setImageResource(mGuidePics[i]);
            if (i == mGuidePics.length - 1) {
                mImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startbutton(v);
                    }
                });
            }
            mViews.add(mImage);
        }
//        mViews.add(inflater.inflate(R.layout.guide_last_page, null));

        // 设置数据
        mViewPager.setAdapter(mViewPagerAdapter);
        // 设置监听
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setOnTouchListener(this);
        // 初始化底部小点
        initPoint();
//        iv_quick_experience.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startbutton(view);
//            }
//        });
    }

    /**
     * 初始化底部小点
     */
    private void initPoint() {

        mPoints = new ImageView[mGuidePics.length];
        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.ll_container);
        // 循环取得小点图片
        for (int i = 0; i < mGuidePics.length; i++) {
            // 得到一个LinearLayout下面的每一个子元素
            mPoints[i] = (ImageView) mLinearLayout.getChildAt(i);
//             默认都设为灰色
            mPoints[i].setEnabled(true);
//             给每个小点设置监听
            mPoints[i].setOnClickListener(this);
//             设置位置tag，方便取出与当前位置对应
            mPoints[i].setTag(i);
        }

        // 设置当面默认的位置
        currentIndex = 0;
        // 设置为白色，即选中状态
        mPoints[currentIndex].setEnabled(false);
    }

    @Override
    public void loadData() {

    }

    @Override
    protected View isNeedLec() {
        return null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        // 设置底部小点选中状态
        setCurDot(position);
        currentIndex = position;

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                misScrolled = false;
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                misScrolled = true;
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int position = (Integer) view.getTag();
        setCurView(position);
        setCurDot(position);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) motionEvent.getX();
                break;
            case MotionEvent.ACTION_MOVE:

                // 当前页码为最后一页并且向右滑动时退出/跳转
//                if ((lastX - motionEvent.getX()) > 100 && (currentIndex == mGuidePics.length - 1)) {
//                    startbutton(null);
//                }

                break;
        }
        return false;
    }


    /**
     * 设置当前页面的位置
     */
    private void setCurView(int position) {
        if (position < 0 || position >= mGuidePics.length) {
            return;
        }
        mViewPager.setCurrentItem(position);
    }

    /**
     * 设置当前的小点的位置
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > mGuidePics.length
                || currentIndex == positon) {
            return;
        }
        mPoints[positon].setEnabled(false);
        mPoints[currentIndex].setEnabled(true);

        currentIndex = positon;
        iv_quick_experience.clearAnimation();
        if (currentIndex == mGuidePics.length - 1) {
            iv_quick_experience.setVisibility(View.VISIBLE);
        } else {
            iv_quick_experience.setVisibility(View.GONE);
        }

    }

    // 立即体验，在XML文件中设置onClick属性
    public void startbutton(View v) {
        if (type == 1) {//设置界面
            finish();
        } else {
//            Intent intent = new Intent(GuideActivity.this, MainActivity.class);
            Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        GuideActivity.this.finish();
    }

    @Override
    public boolean isRegistEventBus() {
        return false;
    }

    @Override
    public void onMsgEvent(EventBusCenter eventBusCenter) {

    }
}
