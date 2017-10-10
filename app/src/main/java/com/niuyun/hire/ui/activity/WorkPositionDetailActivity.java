package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.bean.JobDetailsBean;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.chat.ui.ChatActivity;
import com.niuyun.hire.ui.live.common.widget.CommonLivePlayerView;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.TitleBar;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.rtmp.TXLivePlayer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

import static com.niuyun.hire.base.Constants.EXTRA_CAHT_TYPE;

/**
 * Created by chen.zhiwei on 2017-7-26.
 */

public class WorkPositionDetailActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_view)
    TitleBar titleView;
    @BindView(R.id.tv_company_name1)
    TextView tv_company_name1;//公司名称
    @BindView(R.id.iv_company)
    ImageView iv_company;//公司图标
    @BindView(R.id.tv_position_name)
    TextView tv_position_name;//职位名称
    @BindView(R.id.tv_position_price)
    TextView tv_position_price;//薪资
    @BindView(R.id.tv_company_name)
    TextView tv_company_name;//公司名称
    @BindView(R.id.tv_location)
    TextView tv_location;//公司地址
    @BindView(R.id.tv_work_age)
    TextView tv_work_age;//工作年限
    @BindView(R.id.tv_education)
    TextView tv_education;//学历
    @BindView(R.id.tv_company_scale)
    TextView tv_company_scale;//公司规模
    @BindView(R.id.tv_company_type)
    TextView tv_company_type;//公司类型
    @BindView(R.id.tv_work_responsibilities)
    TextView tv_work_responsibilities;//工作职责
    @BindView(R.id.tv_company_location)
    TextView tv_company_location;//公司地址
    @BindView(R.id.tv_responsibility_more)
    TextView tv_responsibility_more;//更多
    @BindView(R.id.ll_company)
    LinearLayout ll_company;
    @BindView(R.id.bt_talk)
    Button bt_talk;
    @BindView(R.id.pv_play)
    CommonLivePlayerView pv_play;
    private String companyId;//公司id
    private String id;//职位id
    private String uid;//职位id
    private ImageView mCollectView;
    Call<JobDetailsBean> jobDetailsBeanCall;
    Call<SuperBean<String>> addAttentionCall;
    private JobDetailsBean bean;
    private TXLivePlayer mLivePlayer;

    @Override
    public void enterPictureInPictureMode() {
        super.enterPictureInPictureMode();
    }

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_work_position_detail_layout;
    }

    @Override
    public void initViewsAndEvents() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            companyId = bundle.getString("companyId");
            id = bundle.getString("id");
            uid = bundle.getString("uid");
        }
        initTitle();
        ll_company.setOnClickListener(this);
        bt_talk.setOnClickListener(this);
    }

    @Override
    public void loadData() {
        getJobDetails();
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

    private void setData(JobDetailsBean bean) {
        if (bean != null && bean.getData() != null) {
            tv_position_name.setText(bean.getData().getJobsName());
            tv_company_name1.setText(bean.getData().getCompanyname());

            tv_position_price.setText(bean.getData().getMinwage() / 1000 + "k-" + bean.getData().getMaxwage() / 1000 + "k");
            tv_location.setText(bean.getData().getDistrictCn());
            tv_work_age.setText(bean.getData().getExperienceCn());
            tv_education.setText(bean.getData().getEducationCn());

            //视频
            //创建player对象
//            mLivePlayer = new TXLivePlayer(this);
//            mLivePlayer.stopPlay(true);
//            mLivePlayer.enableHardwareDecode(true);
////关键player对象与界面view
//            mLivePlayer.setRenderMode(RENDER_MODE_ADJUST_RESOLUTION);
//            mLivePlayer.setPlayerView(pv_play);
            String flvUrl = "http://1254373020.vod2.myqcloud.com/78e1171avodgzp1254373020/bf96a5d29031868223275582949/2AalqnukGBIA.mp4";
//            mLivePlayer.startPlay(flvUrl, TXLivePlayer.PLAY_TYPE_VOD_MP4); //推荐FLV

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            pv_play.setTransaction(ft);
            pv_play.setmVideoPath(bean.getData().getVideo());

            //工作职责
            if (!TextUtils.isEmpty(bean.getData().getContents())) {
                tv_work_responsibilities.setText(bean.getData().getContents());
                int lineCount = tv_work_responsibilities.getLineCount();
                if (lineCount > 5) {
                    tv_responsibility_more.setVisibility(View.VISIBLE);
                    tv_work_responsibilities.setLines(5);
                } else {
                    tv_responsibility_more.setVisibility(View.GONE);
                    tv_work_responsibilities.setLines(lineCount);
                }
            }
            tv_responsibility_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tv_responsibility_more.getText().equals("展开全部")) {
                        tv_work_responsibilities.setLines(tv_work_responsibilities.getLineCount());
                        tv_responsibility_more.setText("收起全部");
                    } else {
                        tv_responsibility_more.setText("展开全部");
                        tv_work_responsibilities.setLines(5);
                    }
                }
            });
//公司
            try {
                ImageLoadedrManager.getInstance().display(this, Constants.COMMON_IMAGE_URL + bean.getData().getLogo(), iv_company);
            } catch (Exception e) {
            }
            tv_company_name.setText(bean.getData().getCompanyname());
            tv_company_scale.setText(bean.getData().getDistrictCn() + "/" + bean.getData().getNatureCn() + "/" + bean.getData().getScaleCn());
            tv_company_type.setText(bean.getData().getTradeCn());
            tv_company_location.setText("地址：" + bean.getData().getAddress());

            //是否关注
            if (!TextUtils.isEmpty(bean.getData().getFollowFlag()) && bean.getData().getFollowFlag().equals("2")) {
                mCollectView.setImageResource(R.mipmap.ic_stars_yes);
            } else {
                mCollectView.setImageResource(R.mipmap.ic_stars_no);
            }

        }
    }

    private void getJobDetails() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("companyId", companyId);
        map.put("uid", uid);
        if (BaseContext.getInstance().getUserInfo() != null) {
            map.put("memberUid", BaseContext.getInstance().getUserInfo().uid + "");
        }
        jobDetailsBeanCall = RestAdapterManager.getApi().getJobDetails(map);
        jobDetailsBeanCall.enqueue(new JyCallBack<JobDetailsBean>() {
            @Override
            public void onSuccess(Call<JobDetailsBean> call, Response<JobDetailsBean> response) {
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    bean = response.body();
                    setData(response.body());
                    LogUtils.e(response.body().getMsg());
                } else {
                    UIUtil.showToast("接口异常");
                }

            }

            @Override
            public void onError(Call<JobDetailsBean> call, Throwable t) {
                LogUtils.e(t.getMessage());
                UIUtil.showToast("接口异常");
            }

            @Override
            public void onError(Call<JobDetailsBean> call, Response<JobDetailsBean> response) {
                try {
                    LogUtils.e(response.errorBody().string());
                    UIUtil.showToast("接口异常");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void initTitle() {

        titleView.setTitle("职位详情");
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
        titleView.setActionTextColor(Color.WHITE);
        mCollectView = (ImageView) titleView.addAction(new TitleBar.ImageAction(R.mipmap.ic_stars_no) {
            @Override
            public void performAction(View view) {
                if (!UIUtil.isFastDoubleClick()) {
                    if (BaseContext.getInstance().getUserInfo() != null) {
                        if (!TextUtils.isEmpty(bean.getData().getFollowFlag()) && bean.getData().getFollowFlag().equals("2")) {
                            UIUtil.showToast("您已关注");
                        } else {
                            addAttention();
                        }
                    }
                }

            }
        });

        titleView.setImmersive(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_company:
                if (bean != null && bean.getData() != null) {
                    Intent intent = new Intent(this, CompanyDetailsActivity.class);
                    intent.putExtra("id", bean.getData().getCompanyId() + "");
                    startActivity(intent);
                }

                break;
            case R.id.bt_talk:

                if (BaseContext.getInstance().getUserInfo() == null) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    if (bean != null) {
                        Intent intent = new Intent(this, ChatActivity.class);
                        intent.putExtra(Constants.EXTRA_USER_ID, "niuyunApp" + bean.getData().getUid());
                        intent.putExtra(EXTRA_CAHT_TYPE, TIMConversationType.C2C);
                        intent.putExtra(Constants.EXTRA_CARD_MESSAGE, bean);
                        startActivity(intent);
                    }

                }

                break;
        }
    }



    private void addAttention() {
        Map<String, String> map = new HashMap<>();
        map.put("jobsId", bean.getData().getId() + "");
        map.put("jobsName", bean.getData().getJobsName());
        map.put("personalUid", BaseContext.getInstance().getUserInfo().uid + "");
        addAttentionCall = RestAdapterManager.getApi().addAttention(map);
        addAttentionCall.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                if (response.body() != null && response.body().getCode() == Constants.successCode) {
                    UIUtil.showToast(response.body().getMsg());
                    mCollectView.setImageResource(R.mipmap.ic_stars_yes);
                    bean.getData().setFollowFlag("2");
                } else {
                    UIUtil.showToast("接口异常");
                }
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Throwable t) {
                UIUtil.showToast("接口异常");
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                UIUtil.showToast("接口异常");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        pv_play.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pv_play.onPause();
    }
    @Override
    protected void onDestroy() {
        if (jobDetailsBeanCall != null) {
            jobDetailsBeanCall.cancel();
        }
        if (addAttentionCall != null) {
            addAttentionCall.cancel();
        }
        if (pv_play!=null){
            pv_play.onDestroy();
        }
        super.onDestroy();
    }
}
