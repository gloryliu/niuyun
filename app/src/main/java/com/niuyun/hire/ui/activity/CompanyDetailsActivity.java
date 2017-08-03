package com.niuyun.hire.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.BaseFragment;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.bean.CompanyDetailsBean;
import com.niuyun.hire.ui.fragment.CompanyHomePageFragment;
import com.niuyun.hire.ui.fragment.CompanyHotPositionFragment;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.NoScrollViewPager;
import com.niuyun.hire.view.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-7-26.
 */

public class CompanyDetailsActivity extends BaseActivity {
    @BindView(R.id.title_view)
    TitleBar titleView;
    @BindView(R.id.viewpager)
    NoScrollViewPager viewPager;
    @BindView(R.id.vv_v1)
    View vv_v1;
    @BindView(R.id.vv_v2)
    View vv_v2;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.iv_company)
    ImageView iv_company;//公司图标
    @BindView(R.id.tv_company_name)
    TextView tv_company_name;//职位名称
    @BindView(R.id.tv_company_scale)
    TextView tv_company_scale;//公司规模
    @BindView(R.id.tv_company_type)
    TextView tv_company_type;//公司类型
    @BindView(R.id.tv_web_addresss)
    TextView tv_web_addresss;//公司网址
    Class[] fragments = {CompanyHomePageFragment.class, CompanyHotPositionFragment.class};
    private int[] tabNames = {R.string.company_home_page, R.string.company_hot_position};
    private int[] tabIcons = {R.drawable.selector_index_tab_message, R.drawable.selector_index_tab_company};
    private List<BaseFragment> fragmentList = new ArrayList<>();

    private String id;
    Call<CompanyDetailsBean> companyDetailsBeanCall;
    private CompanyDetailsBean companyDetailsBean;
    private CompanyHomePageFragment homePageFragment;
    public static Context mContext;

    public static Context getmContext() {
        return mContext;
    }

    public CompanyDetailsBean getCompanyDetailsBean() {
        return companyDetailsBean;
    }

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_company_details_layout;
    }

    @Override
    public void initViewsAndEvents() {
        mContext = this;
        initTitle();
        try {
            id = getIntent().getExtras().getString("id");
        } catch (Exception e) {
        }

    }

    private void init() {
        if (fragmentList.size() == 0) {
            homePageFragment = new CompanyHomePageFragment();
            fragmentList.add(homePageFragment);
            fragmentList.add(new CompanyHotPositionFragment());
            for (int i = 0; i < tabNames.length; i++) {
                View tabView = View.inflate(CompanyDetailsActivity.this, R.layout.layout_tab_item, null);
                TextView textView = (TextView) tabView.findViewById(R.id.tab_title);
                textView.setText(tabNames[i]);
                textView.setTextColor(getResources().getColor(R.color.color_333333));
                // 利用这种办法设置图标是为了解决默认设置图标和文字出现的距离较大问题
//                textView.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[i], 0, 0);
                mTabLayout.addTab(mTabLayout.newTab().setCustomView(textView));
            }

            viewPager.setOffscreenPageLimit(2);
            viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                @Override
                public int getCount() {
                    return fragments.length;
                }

                @Override
                public Fragment getItem(int position) {
                    Bundle bundle = new Bundle();
                    if (position == 0) {

                        if (homePageFragment != null && companyDetailsBean != null && companyDetailsBean.getData() != null) {
                            bundle.putString("content", companyDetailsBean.getData().getContents());
                            homePageFragment.setArguments(bundle);
                        }
                    }
                    return Fragment.instantiate(CompanyDetailsActivity.this, fragments[position].getName(), bundle);
                }


            });
            // Tablayout选择tab监听
            mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int position = mTabLayout.getSelectedTabPosition();//tab.getPosition();
                    viewPager.setCurrentItem(tab.getPosition());
                    if (position == 0) {

                        vv_v1.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
                        vv_v2.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        vv_v2.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
                        vv_v1.setBackgroundColor(Color.TRANSPARENT);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
    }

    @Override
    public void loadData() {
        getCompanyDetail();
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

    private void setData() {
        init();
        if (companyDetailsBean != null) {
            ImageLoadedrManager.getInstance().display(this, Constants.COMMON_IMAGE_URL + companyDetailsBean.getData().getLogo(), iv_company);
            tv_company_name.setText(companyDetailsBean.getData().getCompanyname());
            tv_company_scale.setText(companyDetailsBean.getData().getDistrictCn() + "/" + companyDetailsBean.getData().getNatureCn() + "/" + companyDetailsBean.getData().getScaleCn());
            tv_company_type.setText(companyDetailsBean.getData().getTradeCn());
            tv_web_addresss.setText(companyDetailsBean.getData().getWebsite());
        }
    }

    private void getCompanyDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        if (BaseContext.getInstance().getUserInfo() != null) {
            map.put("uid", BaseContext.getInstance().getUserInfo().uid + "");
        }
        companyDetailsBeanCall = RestAdapterManager.getApi().getCompanyDetails(map);
        companyDetailsBeanCall.enqueue(new JyCallBack<CompanyDetailsBean>() {
            @Override
            public void onSuccess(Call<CompanyDetailsBean> call, Response<CompanyDetailsBean> response) {
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    companyDetailsBean = response.body();
                    setData();
                } else {
                    UIUtil.showToast("接口异常");
                }
            }

            @Override
            public void onError(Call<CompanyDetailsBean> call, Throwable t) {
                UIUtil.showToast("接口异常");
            }

            @Override
            public void onError(Call<CompanyDetailsBean> call, Response<CompanyDetailsBean> response) {
                UIUtil.showToast("接口异常");
            }
        });
    }

    private void initTitle() {

        titleView.setTitle("公司详情");
        titleView.setTitleColor(Color.WHITE);
        titleView.setLeftImageResource(R.mipmap.ic_title_back);
        titleView.setLeftText("返回");
        titleView.setLeftTextColor(Color.WHITE);
        titleView.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleView.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        titleView.setImmersive(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (companyDetailsBeanCall != null) {
            companyDetailsBeanCall.cancel();
        }
    }
}
