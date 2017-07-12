package com.niuyun.hire.view.adviewpager;


import android.content.Context;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.niuyun.hire.R;


/**
 * 头部广告类
 *
 * @author:WangXF
 */
public class AdViewPager implements OnPageChangeListener {
    /**
     * 定义Context对象
     */
    private Context context;
    /**
     * 定义ImageView数组保存广告图片
     */
    private ScaleImageView[] advs;
    /**
     * 定义ImageView数组保存点图标
     */
    private ImageView[] dots;
    /**
     * 定义自动滑动ViewPager对象
     */
    private AutoScrollViewPager advs_content;
    ;
    /**
     * 定义广告标题TextView对象
     */
    private TextView advs_title;
    /**
     * 定义点图标LinearLayout布局对象
     */
    private LinearLayout advs_dots;
    /**
     * 定义广告布局
     */
    private View advs_views;
    /**
     * 当前滑动的页面索引
     */
    private int currentPosition = 0;
    /**
     * 定义标题数组
     */
    private String[] titles;
    /**
     * 定义点图标的资源文件
     */
    private Integer dotsBackRes;
    /**
     * 是否显示点图标(默认显示)
     */
    private boolean isShowDots = true;
    /**
     * 是否循环播放(默认不播放)
     */
    private boolean isAutoScroll = false;
    /**
     * ViewPager循环播放的时间(默认两秒)
     */
    private Integer autoScrollTime = 2000;
    /**
     * 定义ViewPager的Adapter对象
     */
    private AdViewPagerAdapter apAdapter;
    /**
     * 定义广告个数
     */
    private int count;
    /**
     * 轮播到最后一个
     */
    private boolean isCanOver = false;
    /**
     * 动态获取轮播图位置
     */
    private int mPosition;

    public boolean isNumTwo = false;

    public AdViewPager(Context context, ScaleImageView[] advs, String[] titles) {
        this.context = context;
        this.advs = advs;
        this.titles = titles;
    }

    public void init() {
        initView();
        initAdv();
    }

    // 初始化View
    private void initView() {
        advs_views = LayoutInflater.from(context).inflate(R.layout.adviewpager_layout, null);
        advs_content = (AutoScrollViewPager) advs_views.findViewById(R.id.advs_content);
        advs_title = (TextView) advs_views.findViewById(R.id.advs_title);
        advs_dots = (LinearLayout) advs_views.findViewById(R.id.advs_dots);
    }

    // 初始化广告
    private void initAdv() {
        count = advs.length;
        if (count > 1 && isShowDots) {
            // 初始化点图标
            dots = new ImageView[count];
            int dotLenght;
            if (isNumTwo) {
                dotLenght = dots.length / 2;
            } else {
                dotLenght = dots.length;
            }

            for (int i = 0; i < dotLenght; i++) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.rightMargin = 10;
                ImageView iv = new ImageView(context);

                iv.setLayoutParams(params);
                iv.setEnabled(false);
                if (dotsBackRes != null) {
                    iv.setBackgroundResource(dotsBackRes);
                } else {
                    iv.setBackgroundResource(R.drawable.dot_selecter);
                }
                dots[i] = iv;
                advs_dots.addView(iv);
            }
            dots[currentPosition].setEnabled(true);
        }

        apAdapter = new AdViewPagerAdapter(advs);
        advs_content.setAdapter(apAdapter);
        advs_content.setOnPageChangeListener(this);
        if (count > 1 && isAutoScroll) {
            // 启动自动循环播放广告
            advs_content.startAutoScroll();
            // 播放的间隔时间
            advs_content.setInterval(autoScrollTime);
            // 是否自动循环播放
            advs_content.setCycle(true);
        }
        // 设置广告从当前广告数的100倍数的位置开始滑动
        advs_content.setCurrentItem(count * 100);
    }


    /**
     * @param scaleImageViews
     * @return 数据为2张时 数据翻倍 解决少于三张时右滑动空白
     */
    private ScaleImageView[] advs2Double(ScaleImageView[] scaleImageViews) {
        ScaleImageView[] si = new ScaleImageView[advs.length + 1];
        for (int i = 0; i < advs.length + 1; i++) {
            si[i] = scaleImageViews[i % 2];
        }
        return si;
    }

    // 显示广告布局
    public void showAdvView() {
        advs_views.setVisibility(View.VISIBLE);
    }

    // 隐藏广告布局
    public void hideAdvView() {
        advs_views.setVisibility(View.GONE);
    }

    @Override
    public void onPageScrollStateChanged(int position) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int position) {
        position = position % advs.length;
        mPosition = position;
        if (currentPosition == advs.length - 1 && position == 0 && isCanOver) {
//            EventBus.getDefault().post(new EventBusCenter<String>(Constants.Tag.CHANGE_TAB));
        }
        isCanOver = true;


        if (dots != null) {
            if (isNumTwo) {
                dots[currentPosition % 2].setEnabled(false);
                dots[position % 2].setEnabled(true);
            } else {
                dots[currentPosition].setEnabled(false);
                dots[position].setEnabled(true);
            }

        }
        currentPosition = position;
        // 判断标题是否为空
        if (titles != null) {
            // 判断标题的数量是否和广告的数量相同
            if (titles.length != 0 && titles.length == count) {
                // 设置广告标题
                String title = titles[position];
                if (title.length() > 18) {
                    title = title.substring(0, 18) + "..";
                }
                advs_title.setText(title);
            } else {
                Toast.makeText(context, "你的标题数量和你的广告数量不符！", Toast.LENGTH_SHORT).show();
            }
        }

    }


    public void setDotsBackRes(Integer dotsBackRes) {
        this.dotsBackRes = dotsBackRes;
    }

    /**
     * 设置是否显示点图标
     */
    public void setShowDots(boolean isShowDots) {
        this.isShowDots = isShowDots;
    }

    public View getAdvs_views() {
        return advs_views;
    }

    /**
     * 设置是否自动滑动
     */
    public void setAutoScroll(boolean isAutoScroll) {
        this.isAutoScroll = isAutoScroll;
    }

    /**
     * 设置自动滑动的时间（毫秒为单位）
     */
    public void setAutoScrollTime(Integer autoScrollTime) {
        this.autoScrollTime = autoScrollTime;
    }

    /**
     * 刷新当前ViewPager中的数据
     */
    public void notifyAdapter(ScaleImageView[] advs, String[] titles) {
        this.advs = advs;
        this.titles = titles;
        initAdv();
        apAdapter.notifyDataSetChanged();
    }

    /**
     * 将当前广告View添加到ListView的头部
     */
    public void addToListHeader(ListView list) {
        if (advs_views != null) {
            list.addHeaderView(advs_views);
        }
    }

    public void setScrollDurationFactor(double scrollFactor) {
        advs_content.setScrollDurationFactor(scrollFactor);
    }

    /**
     * 开始滚动
     */
    public void onResume() {
        advs_content.startAutoScroll();
    }

    /**
     * 停止滚动
     */
    public void onPause() {
        advs_content.stopAutoScroll();
    }

    /**
     * 轮播图位置
     *
     * @return position
     */
    public int getCurrentPosition() {
        return mPosition;
    }
}
