package com.niuyun.hire.ui.activity;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.fragment.BuyGoodsOrderListFragment;
import com.niuyun.hire.ui.fragment.RentGoodsOrderListFragment;
import com.niuyun.hire.view.ClickChangeViewPager;
import com.niuyun.hire.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 订单列表
 * Created by chen.zhiwei on 2017-6-22.
 */

public class OrderListActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.customViewpager)
    ClickChangeViewPager customViewpager;
    @BindView(R.id.rl_left)
    RelativeLayout rl_left;//左边按钮
    @BindView(R.id.rl_right)
    RelativeLayout rl_right;//右边按钮
    @BindView(R.id.tv_left)
    TextView tv_left;//左边标题
    @BindView(R.id.tv_right)
    TextView tv_right;//右边标题
    @BindView(R.id.line_left)
    View line_left;//左边线
    @BindView(R.id.line_right)
    View line_right;//右边线
    @BindView(R.id.title_view)
    TitleBar title_view;
    private Fragment leftFragment, rightFragment;
    private List<Fragment> fragments = new ArrayList<>();
    private int currentItem = 0;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_order_list;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        rl_left.setOnClickListener(this);
        rl_right.setOnClickListener(this);
        //出售fragment
        leftFragment = new RentGoodsOrderListFragment();
        //出租fragment
        rightFragment = new BuyGoodsOrderListFragment();
        fragments.add(leftFragment);
        fragments.add(rightFragment);
        customViewpager.setOffscreenPageLimit(2);
        FragmentStatePagerAdapter fAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        customViewpager.setAdapter(fAdapter);
        customViewpager.setCurrentItem(currentItem);
        if (currentItem==0){
            rl_left.performClick();
        }else {
            rl_right.performClick();
        }
    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean isRegistEventBus() {
        return false;
    }

    @Override
    public void onMsgEvent(EventBusCenter eventBusCenter) {

    }

    @Override
    protected View isNeedLec() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_left:
                tv_left.setTextColor(getResources().getColor(R.color.color_ff6900));
                line_left.setBackgroundColor(getResources().getColor(R.color.color_ff6900));

                tv_right.setTextColor(getResources().getColor(R.color.color_333333));
                line_right.setBackgroundColor(getResources().getColor(R.color.color_ffffffff));
                customViewpager.setCurrentItem(0);
                break;
            case R.id.rl_right:
                tv_left.setTextColor(getResources().getColor(R.color.color_333333));
                line_left.setBackgroundColor(getResources().getColor(R.color.color_ffffffff));

                tv_right.setTextColor(getResources().getColor(R.color.color_ff6900));
                line_right.setBackgroundColor(getResources().getColor(R.color.color_ff6900));
                customViewpager.setCurrentItem(1);
                break;
        }
    }
    /**
     * 初始化标题
     */
    private void initTitle() {
        title_view.setTitle("订单");
        title_view.setTitleColor(Color.WHITE);
        title_view.setLeftImageResource(R.mipmap.ic_title_back);
        title_view.setLeftText("返回");
        title_view.setLeftTextColor(Color.WHITE);
        title_view.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title_view.setBackgroundColor(getResources().getColor(R.color.color_ff6900));
        title_view.setImmersive(true);

    }

}
