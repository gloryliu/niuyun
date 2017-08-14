package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.adapter.CommonPerfectInfoTagAdapter;
import com.niuyun.hire.ui.adapter.PositionIntentListAdapter;
import com.niuyun.hire.ui.bean.CommonTagBean;
import com.niuyun.hire.ui.bean.CommonTagItemBean;
import com.niuyun.hire.ui.bean.GetBaseTagBean;
import com.niuyun.hire.ui.bean.PositionIntentBean;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.bean.UserInfoBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.MyDialog;
import com.niuyun.hire.view.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/12.
 */

public class ControlPositionIntentActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_view)
    TitleBar title_view;
    @BindView(R.id.rv_list)
    RecyclerView rv_list;
    @BindView(R.id.bt_add)
    Button bt_add;
    @BindView(R.id.rl_state)
    RelativeLayout rl_state;
    @BindView(R.id.tv_state)
    TextView tv_state;
    private Call<PositionIntentBean> gePositionIntentList;
    private Call<SuperBean<String>> setCurrentStateCall;
    private PositionIntentListAdapter listAdapter;
    private CommonTagBean commonTagBean;
    private CommonTagItemBean cacheCommonTagBean;
    private String cName;
    private String cId;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_control_position_intent;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        tv_state.setText(BaseContext.getInstance().getUserInfo().currentCn);
        bt_add.setOnClickListener(this);
        rl_state.setOnClickListener(this);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new PositionIntentListAdapter(this);
        rv_list.setAdapter(listAdapter);
        listAdapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {
                if (bean != null) {
                    PositionIntentBean.DataBean dataBean = (PositionIntentBean.DataBean) bean;
                    Intent intent = new Intent(ControlPositionIntentActivity.this, EditPositionIntentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("positionintent", dataBean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void loadData() {
        if (BaseContext.getInstance().getUserInfo() != null) {

            getIntentList();
        }
        getTagItmes();
    }

    @Override
    public boolean isRegistEventBus() {
        return true;
    }

    @Override
    public void onMsgEvent(EventBusCenter eventBusCenter) {
        if (eventBusCenter != null) {
            if (eventBusCenter.getEvenCode() == Constants.UPDATE_POSITION_INTENT) {
                getIntentList();
            }
        }
    }

    @Override
    protected View isNeedLec() {
        return null;
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        title_view.setTitle("管理求职意向");
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
        title_view.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        title_view.setActionTextColor(Color.WHITE);
        title_view.setImmersive(true);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_add:
                startActivity(new Intent(this, EditPositionIntentActivity.class));
                break;
            case R.id.rl_state:
                if (!UIUtil.isFastDoubleClick()) {
                    if (commonTagBean != null) {
                        commonDialog(commonTagBean.getData().getQS_current());
                    } else {
                        getTagItmes();
                    }
                }
                break;
        }
    }

    private void getIntentList() {
        gePositionIntentList = RestAdapterManager.getApi().gePositionIntentList(BaseContext.getInstance().getUserInfo().uid + "");
        gePositionIntentList.enqueue(new JyCallBack<PositionIntentBean>() {
            @Override
            public void onSuccess(Call<PositionIntentBean> call, Response<PositionIntentBean> response) {
                if (response.body() != null && response.body().getCode() == Constants.successCode) {
                    listAdapter.ClearData();
                    listAdapter.addList(response.body().getData());
                } else {
                    UIUtil.showToast(response.body().getMsg());
                }
            }

            @Override
            public void onError(Call<PositionIntentBean> call, Throwable t) {
                UIUtil.showToast("接口异常");
            }

            @Override
            public void onError(Call<PositionIntentBean> call, Response<PositionIntentBean> response) {
                UIUtil.showToast("接口异常");
            }
        });
    }

    private void setCurrentState() {
        DialogUtils.showDialog(this, "加载中", false);
        Map<String, String> map = new HashMap<>();
        map.put("current", cId);
        map.put("currentCn", cName);
        if (BaseContext.getInstance().getUserInfo() != null) {

            map.put("uid", BaseContext.getInstance().getUserInfo().uid + "");
        }
        setCurrentStateCall
                = RestAdapterManager.getApi().setCurrentState(map);
        setCurrentStateCall.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
                if (response.body() != null && response.body().getCode() == Constants.successCode) {
                    UIUtil.showToast(response.body().getMsg());
                    tv_state.setText(cName);
                    UserInfoBean infoBean = BaseContext.getInstance().getUserInfo();
                    infoBean.current = Integer.parseInt(cId);
                    infoBean.currentCn = cName;
                    BaseContext.getInstance().setUserInfo(infoBean);
                } else {
                    UIUtil.showToast("修改失败");
                }
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Throwable t) {
                UIUtil.showToast("接口异常");
                DialogUtils.closeDialog();
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                UIUtil.showToast("接口异常");
                DialogUtils.closeDialog();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gePositionIntentList != null) {
            gePositionIntentList.cancel();
        }
    }

    /**
     * 根据对应的分类id获取对应的数据
     */

    private void getTagItmes() {
        List<String> list = new ArrayList<>();
        list.add("QS_current");
        GetBaseTagBean tagBean = new GetBaseTagBean();
        tagBean.setAlias(list);
        Call<CommonTagBean> commonTagBeanCall = RestAdapterManager.getApi().getWorkAgeAndResume(tagBean);
        commonTagBeanCall.enqueue(new JyCallBack<CommonTagBean>() {
            @Override
            public void onSuccess(Call<CommonTagBean> call, Response<CommonTagBean> response) {
                LogUtils.e(response.body().getMsg());
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    commonTagBean = response.body();
//                    commonDialog(response.body());
                } else {
                    UIUtil.showToast(response.body().getMsg());
                }
            }

            @Override
            public void onError(Call<CommonTagBean> call, Throwable t) {
                LogUtils.e(t.getMessage());
            }

            @Override
            public void onError(Call<CommonTagBean> call, Response<CommonTagBean> response) {

            }
        });
    }

    /**
     * 选择tag公告dialog
     */
    public void commonDialog(List<CommonTagItemBean> tagBean) {
        if (tagBean == null || tagBean.size() <= 0) {
            return;
        }

        View view = View.inflate(ControlPositionIntentActivity.this, R.layout.dialog_common_tag, null);
        showdialog(view);
        RecyclerView tag_recyclerview = (RecyclerView) view.findViewById(R.id.tag_recyclerview);
        TextView cancle = (TextView) view.findViewById(R.id.tv_cancel);
        TextView confirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText("目前状态");
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCommitValue(cacheCommonTagBean);
                myDialog.dismiss();
            }
        });
        tag_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        CommonPerfectInfoTagAdapter adapter = new CommonPerfectInfoTagAdapter(this, tagBean);
        tag_recyclerview.setAdapter(adapter);
        adapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {
                cacheCommonTagBean = (CommonTagItemBean) bean;
            }
        });
    }

    /**
     * 点击确定设置相应的数据
     *
     * @param tagBean
     */
    private void setCommitValue(CommonTagItemBean tagBean) {
        if (tagBean == null) {
            UIUtil.showToast("请重新选择");
            return;
        }

        cId = tagBean.getCId() + "";
        cName = tagBean.getCName();
        setCurrentState();
        cacheCommonTagBean = null;
    }

    private MyDialog myDialog;

    /**
     * 弹出dialog
     *
     * @param view
     */
    private void showdialog(View view) {

        myDialog = new MyDialog(this, 0, UIUtil.dip2px(this, 200), view, R.style.dialog);
        myDialog.show();
    }
}
