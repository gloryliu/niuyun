package com.niuyun.hire.ui.index.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.easefun.polyvsdk.live.chat.ppt.api.PolyvLiveMessage;
import com.easefun.polyvsdk.live.chat.ppt.api.entity.PolyvLiveMessageEntity;
import com.easefun.polyvsdk.live.chat.ppt.api.listener.PolyvLiveMessageListener;
import com.easefun.polyvsdk.rtmp.core.login.IPolyvRTMPLoginListener;
import com.easefun.polyvsdk.rtmp.core.login.PolyvRTMPLoginErrorReason;
import com.easefun.polyvsdk.rtmp.core.login.PolyvRTMPLoginVerify;
import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseFragment;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.adapter.IndexLiveListItemAdapter;
import com.niuyun.hire.ui.bean.LiveListBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.ui.polyvLive.activity.PolyvPlayerActivity;
import com.niuyun.hire.ui.polyvLive.activity.PolyvSettingActivity;
import com.niuyun.hire.ui.polyvLive.util.PolyvScreenUtils;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.TitleBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

import static com.niuyun.hire.R.id.title_view;

/**
 * Created by chen.zhiwei on 2017-7-18.
 */

public class LiveFragment extends BaseFragment implements View.OnClickListener {
    @BindView(title_view)
    TitleBar titleView;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.bt_go_live)
    ImageButton bt_go_live;
    private IndexLiveListItemAdapter listItemAdapter;
    private String uid = "fb3a182807";
    private String channelId = "128591";
    private String password = "128591";
    private Call<LiveListBean> listBeanCall;
    private int pageNum = 1;
    private int pageSize = 20;
    private LiveListBean liveListBean;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_live_layout;
    }

    @Override
    protected void initViewsAndEvents() {
        initTitle();
        bt_go_live.setOnClickListener(this);
        //下拉刷新
        refreshLayout.setEnableHeaderTranslationContent(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                getList();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageNum += 1;
                getList();
            }
        });
        //求职recyclerView
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        listItemAdapter = new IndexLiveListItemAdapter(getActivity());
        recyclerview.setAdapter(listItemAdapter);
        listItemAdapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {

                if (bean != null) {
                    LiveListBean.DataBeanX.DataBean dataBean = (LiveListBean.DataBeanX.DataBean) bean;
                    channelId = dataBean.getChannelId() + "";
                    uid = dataBean.getUserId() + "";
                }

                new PolyvLiveMessage().getLiveType(channelId, new PolyvLiveMessageListener() {
                    @Override
                    public void success(boolean isPPTLive, PolyvLiveMessageEntity entity) {
                        Intent playUrl = new Intent(getActivity(), PolyvPlayerActivity.class);
                        playUrl.putExtra("uid", uid);
                        playUrl.putExtra("cid", channelId);
                        startActivity(playUrl);
                    }

                    @Override
                    public void fail(final String failTips, final int code) {
                        UIUtil.showToast("获取频道失败");
                    }
                });
            }
        });

    }

    @Override
    protected void loadData() {
        getList();
    }

    @Override
    protected View isNeedLec() {
        return null;
    }

    @Override
    public boolean isRegistEventBus() {
        return false;
    }

    @Override
    public void onMsgEvent(EventBusCenter eventBusCenter) {

    }

    private void getList() {
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", pageNum + "");
        map.put("pageSize", pageSize + "");
        listBeanCall = RestAdapterManager.getApi().getLiveList(map);
        listBeanCall.enqueue(new JyCallBack<LiveListBean>() {
            @Override
            public void onSuccess(Call<LiveListBean> call, Response<LiveListBean> response) {
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadmore();
                }
                if (pageNum == 1) {
                    listItemAdapter.ClearData();
                }
                if (response != null && response.body() != null && response.body().getData() != null && response.body().getCode() == Constants.successCode) {
                    listItemAdapter.addList(response.body().getData().getData());
                    if (pageNum >= response.body().getData().getPageCount()) {
                        refreshLayout.setEnableLoadmore(false);
                    } else {
                        refreshLayout.setEnableLoadmore(true);
                    }
                } else {
                    if (pageNum == 1) {
                        //无数据

                    } else {
                        //加载完全部数据
                    }
                }
            }

            @Override
            public void onError(Call<LiveListBean> call, Throwable t) {
                if (refreshLayout != null) {
                    if (pageNum == 1) {
                        listItemAdapter.ClearData();
                    }
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadmore();
                }
                LogUtils.e(t.getMessage());
            }

            @Override
            public void onError(Call<LiveListBean> call, Response<LiveListBean> response) {
                if (refreshLayout != null) {
                    if (pageNum == 1) {
                        listItemAdapter.ClearData();
                    }
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadmore();
                }
                try {
                    LogUtils.e(response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initTitle() {

        titleView.setTitle("直播");
        titleView.setTitleColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        titleView.setImmersive(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_go_live:
                //我要直播
                prepareLive();
                break;

        }
    }
    private void prepareLive(){
        PolyvRTMPLoginVerify.verify("128664", "123456", new IPolyvRTMPLoginListener() {
            @Override
            public void onError(PolyvRTMPLoginErrorReason errorReason) {
                switch (errorReason.getType()) {
                    case PolyvRTMPLoginErrorReason.SERVER_STATUS_EMPTY:
                        UIUtil.showToast("服务返回状态为空");
                        break;
                    case PolyvRTMPLoginErrorReason.SERVER_STATUS_FAIL:
                        UIUtil.showToast(errorReason.getMsg());
                        break;
                    case PolyvRTMPLoginErrorReason.NETWORK_DENIED:
                        UIUtil.showToast("无法连接网络");
                        break;
                    case PolyvRTMPLoginErrorReason.DATA_ERROR:
                        UIUtil.showToast("数据错误");
                        break;
                    case PolyvRTMPLoginErrorReason.CHANNEL_ID_EMPTY:
                        UIUtil.showToast("请输入直播频道ID");
                        break;
                    case PolyvRTMPLoginErrorReason.PASSWORD_EMPTY:
                        UIUtil.showToast("请输入直播密码");
                        break;
                    case PolyvRTMPLoginErrorReason.REQUEST_SERVER_FAIL:
                        UIUtil.showToast("登陆失败");
                        break;
                }
            }

            @Override
            public void onSuccess(String[] preview_nickname_avatar) {
                PolyvScreenUtils.setDecorVisible(getActivity());
                // 初始化分享配置
//                PolyvShareFragment.initShareConfig(preview_nickname_avatar[0], preview_nickname_avatar[2], null, null);
//
//                SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString(LAST_ACCOUNT_ID_KEY, channelId);
//                editor.commit();
//
//                Set<String> accountIdList = sharedPreferences.getStringSet(ACCOUNT_ID_LIST_KEY, new HashSet<String>());
//                if (!accountIdList.contains(channelId)) {
//                    accountIdList.add(channelId);
//                    editor = sharedPreferences.edit();
//                    editor.putStringSet(ACCOUNT_ID_LIST_KEY, accountIdList);
//                    editor.commit();
//                }
//
//                if (!isSavePasswordCB.isChecked()) {
//                    if (sharedPreferences.contains(channelId)) {
//                        editor = sharedPreferences.edit();
//                        editor.remove(channelId);
//                        editor.commit();
//                    }
//
//                    if (sharedPreferences.contains(getCheckSelectKey(channelId))) {
//                        editor = sharedPreferences.edit();
//                        editor.remove(getCheckSelectKey(channelId));
//                        editor.commit();
//                    }
//                } else {
//                    editor = sharedPreferences.edit();
//                    editor.putString(channelId, password);
//                    editor.commit();
//
//                    if (!sharedPreferences.contains(getCheckSelectKey(channelId))) {
//                        editor = sharedPreferences.edit();
//                        editor.putBoolean(getCheckSelectKey(channelId), true);
//                        editor.commit();
//                    }
//                }
                Intent intent = new Intent(getActivity(), PolyvSettingActivity.class);
                intent.putExtra("channelId", "128664");
                intent.putExtra("title", preview_nickname_avatar[1]);
                intent.putExtra("avatarUrl", preview_nickname_avatar[2]);
                startActivity(intent);
            }
        }, getActivity());
    }
}
