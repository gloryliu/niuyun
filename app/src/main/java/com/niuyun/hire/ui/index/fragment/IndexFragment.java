package com.niuyun.hire.ui.index.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseFragment;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.chat.ui.ConversationFragment;
import com.niuyun.hire.ui.fragment.SeekCompanyFragment;
import com.niuyun.hire.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 首页
 * Created by chen.zhiwei on 2017-6-19.
 */

public class IndexFragment extends BaseFragment {
    //    @BindView(R.id.refreshLayout)
//    RefreshLayout refreshLayout;
    protected static final String TAG = "MainActivity";
    @BindView(R.id.viewpager)
    NoScrollViewPager viewPager;

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.iv_message)
    ImageView iv_message;
    @BindView(R.id.iv_company)
    ImageView iv_company;
    private int[] tabNames = {R.string.index_tab_name_message, R.string.index_tab_name_company};
    private int[] tabIcons = {R.drawable.selector_index_tab_message, R.drawable.selector_index_tab_company};
    private List<Fragment> fragmentList = new ArrayList<>();
    private int currentTabIndex;
    ConversationFragment conversationListFragment;
    @Override
    protected int getContentViewLayoutId() {

        return R.layout.fragment_index_layout;
    }

    @Override
    protected void initViewsAndEvents() {


    }

    @Override
    public void onStart() {
        super.onStart();
        if (fragmentList.size() == 0) {
            conversationListFragment = new ConversationFragment();
            fragmentList.add(conversationListFragment);
            fragmentList.add(new SeekCompanyFragment());
            for (int i = 0; i < tabNames.length; i++) {
                View tabView = View.inflate(getActivity(), R.layout.layout_tab_item, null);
                TextView textView = (TextView) tabView.findViewById(R.id.tab_title);
                textView.setText(tabNames[i]);
                textView.setTextColor(getResources().getColor(R.color.halfwhite_or_wihte_seletor));
                // 利用这种办法设置图标是为了解决默认设置图标和文字出现的距离较大问题
                textView.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[i], 0, 0);
                mTabLayout.addTab(mTabLayout.newTab().setCustomView(textView));
            }

            viewPager.setOffscreenPageLimit(2);
            viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
                @Override
                public int getCount() {
                    return fragmentList.size();
                }

                @Override
                public Fragment getItem(int position) {
                    return fragmentList.get(position);
                }


            });
            // Tablayout选择tab监听
            mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int position = mTabLayout.getSelectedTabPosition();//tab.getPosition();
                    viewPager.setCurrentItem(tab.getPosition());
                    currentTabIndex = position;
                    if (position == 0) {
                        iv_message.setImageResource(R.mipmap.ic_down_triangle);
                        iv_company.setImageResource(R.color.transparent);
                    } else {
                        iv_company.setImageResource(R.mipmap.ic_down_triangle);
                        iv_message.setImageResource(R.color.transparent);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            iv_message.setImageResource(R.mipmap.ic_down_triangle);
            iv_company.setImageResource(R.color.transparent);
        }


    }

    @Override
    protected void loadData() {
    }

    @Override
    protected View isNeedLec() {
        return null;
    }

    @Override
    public boolean isRegistEventBus() {
        return true;
    }

    @Override
    public void onMsgEvent(EventBusCenter eventBusCenter) {
        if (eventBusCenter != null) {
            if (eventBusCenter.getEvenCode() == Constants.LOGIN_FAILURE || eventBusCenter.getEvenCode() == Constants.LOGIN_SUCCESS) {
                loadData();
            }
        }

    }



    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {

        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
