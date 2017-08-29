package com.niuyun.hire.ui.index;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.niuyun.hire.R;
import com.niuyun.hire.base.AppManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.activity.PerfectEnterpriseInformation;
import com.niuyun.hire.ui.activity.PerfectPersonInformation;
import com.niuyun.hire.ui.chat.Constant;
import com.niuyun.hire.ui.chat.DemoHelper;
import com.niuyun.hire.ui.chat.db.DemoDBManager;
import com.niuyun.hire.ui.chat.runtimepermissions.PermissionsManager;
import com.niuyun.hire.ui.chat.runtimepermissions.PermissionsResultAction;
import com.niuyun.hire.ui.chat.ui.LoginActivity;
import com.niuyun.hire.ui.index.fragment.AlljobSeekerFragment;
import com.niuyun.hire.ui.index.fragment.EnterpriseIndexFragment;
import com.niuyun.hire.ui.index.fragment.FindFragment;
import com.niuyun.hire.ui.index.fragment.LiveFragment;
import com.niuyun.hire.ui.index.fragment.MyFragment;
import com.niuyun.hire.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;


public class EnterpriseMainActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    NoScrollViewPager viewPager;

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    private TextView unreadLabel;
    // textview for unread event message
    private TextView unreadAddressLable;
    protected static final String TAG = "MainActivity";
    /**
     * 是否退出
     **/
    private boolean isWaitingExit = false;
//    Class[] fragments = {EnterpriseIndexFragment.class, AlljobSeekerFragment.class, LiveFragment.class, FindFragment.class, MyFragment.class};
    private int[] tabNames = {R.string.main_tab_name_index, R.string.main_tab_name_allPerson, R.string.main_tab_name_live, R.string.main_tab_name_find, R.string.main_tab_name_me};
    private int[] tabIcons = {R.drawable.selector_main_tab_index, R.drawable.selector_main_tab_alljobs, R.drawable.selector_main_tab_live, R.drawable.selector_main_tab_find, R.drawable.selector_main_tab_mine};
    private List<Fragment> fragmentList = new ArrayList<>();


    @Override
    public int getContentViewLayoutId() {
        if (getIntent() != null &&
                (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) ||
                        getIntent().getBooleanExtra(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false) ||
                        getIntent().getBooleanExtra(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE, false))) {
            DemoHelper.getInstance().logout(false, null);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        } else if (getIntent() != null && getIntent().getBooleanExtra("isConflict", false)) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        return R.layout.activity_main;
    }

    @Override
    public void initViewsAndEvents() {
        if (fragmentList.size() <= 0) {
            fragmentList.add(new EnterpriseIndexFragment());
            fragmentList.add(new AlljobSeekerFragment());
            fragmentList.add(new LiveFragment());
            fragmentList.add(new FindFragment());
            fragmentList.add(new MyFragment());
            for (int i = 0; i < tabNames.length; i++) {
                View tabView = View.inflate(this, R.layout.layout_tab_item, null);
                TextView textView = (TextView) tabView.findViewById(R.id.tab_title);
                textView.setText(tabNames[i]);
                // 利用这种办法设置图标是为了解决默认设置图标和文字出现的距离较大问题
                textView.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[i], 0, 0);
                mTabLayout.addTab(mTabLayout.newTab().setCustomView(textView));
            }

            viewPager.setOffscreenPageLimit(5);
            viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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
                    if (BaseContext.getInstance().getUserInfo() != null) {
                        viewPager.setCurrentItem(tab.getPosition());
                    } else {
                        if (tab.getPosition() != 1) {
                            startActivity(new Intent(EnterpriseMainActivity.this, com.niuyun.hire.ui.activity.LoginActivity.class));
                        } else {
                            viewPager.setCurrentItem(tab.getPosition());
                        }
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


        if (BaseContext.getInstance().getUserInfo() != null) {

            initChat();
            initMessage();
            checkData();
        }
    }

    private void checkData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (BaseContext.getInstance().getUserInfo() != null) {
                    if (BaseContext.getInstance().getUserInfo().utype == 1) {
                        //企业注册
                        if (BaseContext.getInstance().getUserInfo().perfect == 1) {
                            if (BaseContext.getInstance().getUserInfo().perfect == 1) {
                                Intent findPsIntent = new Intent(EnterpriseMainActivity.this, PerfectEnterpriseInformation.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("uid", BaseContext.getInstance().getUserInfo().uid + "");
                                bundle.putString("companyId", BaseContext.getInstance().getUserInfo().companyId + "");
                                findPsIntent.putExtras(bundle);
                                startActivity(findPsIntent);
                            }
                        }
                    } else {
                        //个人注册
                        if (BaseContext.getInstance().getUserInfo().perfect == 1) {
                            Intent findPsIntent = new Intent(EnterpriseMainActivity.this, PerfectPersonInformation.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("uid", BaseContext.getInstance().getUserInfo().uid + "");
                            findPsIntent.putExtras(bundle);
                            startActivity(findPsIntent);
                        }
                    }
                }
            }
        }, 1000);

    }

    /**
     * 登陆聊天
     */
    private void initChat() {
        if (TextUtils.isEmpty(BaseContext.getInstance().getUserInfo().chatUserName) || TextUtils.isEmpty(BaseContext.getInstance().getUserInfo().chatPwd)) {
            return;
        }
        // After logout，the DemoDB may still be accessed due to async callback, so the DemoDB will be re-opened again.
        // close it before login to make sure DemoDB not overlap
        DemoDBManager.getInstance().closeDB();

        // reset current user name before login
        DemoHelper.getInstance().setCurrentUserName(BaseContext.getInstance().getUserInfo().chatUserName);
        EMClient.getInstance().login(BaseContext.getInstance().getUserInfo().chatUserName, BaseContext.getInstance().getUserInfo().chatPwd, new EMCallBack() {

            @Override
            public void onSuccess() {


                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                // update current user's display name for APNs
                boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
                        BaseContext.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }

//                if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
//                    pd.dismiss();
//                }
                // get user's info (this should be get from App's server or 3rd party service)
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

//                Intent intent = new Intent(LoginActivity.this,
//                        MainActivity.class);
//                startActivity(intent);

//                finish();
            }

            @Override
            public void onProgress(int progress, String status) {
//                Log.d(TAG, "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
//                Log.d(TAG, "login: onError: " + code);
//                if (!progressShow) {
//                    return;
//                }
                runOnUiThread(new Runnable() {
                    public void run() {
//                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void initMessage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                try {
                    //some device doesn't has activity to handle this intent
                    //so add try catch
                    Intent intent = new Intent();
                    intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivity(intent);
                } catch (Exception e) {
                }
            }
        }
        if (getIntent() != null &&
                (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) ||
                        getIntent().getBooleanExtra(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false) ||
                        getIntent().getBooleanExtra(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE, false))) {
            DemoHelper.getInstance().logout(false, null);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (getIntent() != null && getIntent().getBooleanExtra("isConflict", false)) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
//        setContentView(R.layout.em_activity_main);
        // runtime permission for android 6.0, just require all permissions here for simple
        requestPermissions();

    }

    @Override
    public void loadData() {
    }

    @Override
    public boolean isRegistEventBus() {
        return true;
    }

    @Override
    public void onMsgEvent(EventBusCenter eventBusCenter) {
        if (null != eventBusCenter) {
            if (eventBusCenter.getEvenCode()== Constants.LOGIN_SUCCESS){
                if (BaseContext.getInstance().getUserInfo() != null) {
                    initChat();
                    initMessage();
                    checkData();
                }
            }
        }
    }

    @Override
    protected View isNeedLec() {
        return null;
    }

    /**
     * 再按一次退出程序。
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isWaitingExit) {
                isWaitingExit = false;
//                Intent intent = new Intent(mContext, TabIconService.class);
//                stopService(intent);
                AppManager.getAppManager().AppExit(EnterpriseMainActivity.this);
            } else {
                Toast.makeText(EnterpriseMainActivity.this, "再按一次退出程序...", Toast.LENGTH_SHORT).show();

                isWaitingExit = true;

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isWaitingExit = false;
                    }
                }, 3000);
                return true;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }
}
