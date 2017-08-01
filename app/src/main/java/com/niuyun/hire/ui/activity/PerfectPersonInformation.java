package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.AppManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.adapter.CommonPerfectInfoTagAdapter;
import com.niuyun.hire.ui.adapter.JobPerfectInfoTagAdapter;
import com.niuyun.hire.ui.adapter.JobPerfectInfoTagAdapter1;
import com.niuyun.hire.ui.adapter.JobPerfectInfoTagAdapter2;
import com.niuyun.hire.ui.bean.AllTagBean;
import com.niuyun.hire.ui.bean.CommonTagBean;
import com.niuyun.hire.ui.bean.CommonTagItemBean;
import com.niuyun.hire.ui.bean.GetBaseTagBean;
import com.niuyun.hire.ui.bean.JobTagBean;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.bean.UserInfoBean;
import com.niuyun.hire.ui.index.MainActivity;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.ErrorMessageUtils;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.utils.UploadFile;
import com.niuyun.hire.utils.photoTool.PhotoPicActivity;
import com.niuyun.hire.utils.photoTool.TakingPicturesActivity;
import com.niuyun.hire.view.CircularImageView;
import com.niuyun.hire.view.MyDialog;
import com.niuyun.hire.view.TitleBar;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-7-21.
 */

public class PerfectPersonInformation extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_view)
    TitleBar titleView;
    @BindView(R.id.bt_next)
    Button bt_next;
    @BindView(R.id.rl_first)
    RelativeLayout rl_first;
    @BindView(R.id.ll_second)
    LinearLayout ll_second;
    @BindView(R.id.iv_intent_line)
    ImageView iv_intent_line;
    @BindView(R.id.iv_head)
    CircularImageView iv_head;
    @BindView(R.id.tv_intent_title)
    TextView tv_intent_title;
    @BindView(R.id.rl_resume)
    RelativeLayout rl_resume;
    @BindView(R.id.rl_work_age)
    RelativeLayout rl_work_age;
    @BindView(R.id.rl_job_tag)
    RelativeLayout rl_job_tag;
    @BindView(R.id.rl_wage)
    RelativeLayout rl_wage;

    @BindView(R.id.tv_wage)
    TextView tv_wage;
    @BindView(R.id.tv_job)
    TextView tv_job;
    @BindView(R.id.tv_work_age)
    TextView tv_work_age;
    @BindView(R.id.tv_resume)
    TextView tv_resume;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.rg_sex)
    RadioGroup rg_sex;
    @BindView(R.id.rb_man)
    RadioButton rb_man;
    @BindView(R.id.rb_woman)
    RadioButton rb_woman;
    @BindView(R.id.rl_content)
    RelativeLayout rl_content;
    private Call<SuperBean<String>> upLoadImageCall;
    List<String> list = new ArrayList<String>();
    private String headimg;
    private static final int resultCode_Photos = 10;//跳转到相册
    private static final int resultCode_Camera = 11;//跳转到相机
    private AllTagBean allTagBean;
    private CommonTagBean commonTagBean;
    private String clickTag;
    private CommonTagItemBean cacheCommonTagBean;

    private String education;//学历
    private String educationCn;

    private String experience;//经验
    private String experienceCn;


    private String wage;//期望薪资
    private String wageCn;

    private String intentionJobsId;//期望职位,每一级的id中间用 . 隔开
    private String intentionJobs;

    private String intentionJobsId1;
    private String intentionJobsId2;
    private String intentionJobsId3;


    private int jobStep = 0;

    private JobPerfectInfoTagAdapter1 adapter1;
    private JobPerfectInfoTagAdapter2 adapter2;
    private JobTagBean.DataBean cacheJobTag;

    private String sex;//1 男  ------     2 女
    private String sexCn;
    private String uid;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_perfect_person_information;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            uid = bundle.getString("uid");
        }
        bt_next.setOnClickListener(this);
        iv_head.setOnClickListener(this);
        rl_resume.setOnClickListener(this);
        rl_work_age.setOnClickListener(this);
        rl_job_tag.setOnClickListener(this);
        rl_wage.setOnClickListener(this);
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rb_man:
                        sex = "1";
                        sexCn = "男";
                        break;
                    case R.id.rb_woman:
                        sex = "2";
                        sexCn = "女";
                        break;
                }
            }
        });
    }

    @Override
    public void loadData() {
//        getAllTag();
        getTagItmes();
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

    private void initTitle() {

        titleView.setTitle("完善信息");
        titleView.setTitleColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        titleView.setImmersive(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_next:
                //下一步
                if (bt_next.getText().equals("下一步")) {
                    if (checkDataFirstPage()) {
                        ll_second.setVisibility(View.VISIBLE);
                        rl_first.setVisibility(View.GONE);
                        bt_next.setText("完成");
                        tv_intent_title.setTextColor(getResources().getColor(R.color.color_ea0000));
                        iv_intent_line.setImageResource(R.mipmap.ic_line_right_red);
                        rl_content.setBackgroundResource(R.mipmap.ic_perfect_person_info2);
                    }
//
                } else {
//保存数据
                    if (checkDataSecondPage()) {
                        upLoadImage();
                    }
                }
                break;
            case R.id.iv_head:
                //上传头像
                photodialog();
                break;
            case R.id.rl_resume:
                //学历
                clickTag = "QS_education";
                if (!UIUtil.isFastDoubleClick()) {

                    click(clickTag);
                }
                break;
            case R.id.rl_work_age:
                //工作年限
                clickTag = "QS_experience";
                if (!UIUtil.isFastDoubleClick()) {

                    click(clickTag);
                }
                break;
            case R.id.rl_job_tag:
                //期望职位
                clickTag = "QS_jobs_nature";
                if (!UIUtil.isFastDoubleClick()) {
                    jobStep = 0;
                    getJobData("0");
                }
                break;
            case R.id.rl_wage:
                //期望薪资
                clickTag = "QS_wage";
                if (!UIUtil.isFastDoubleClick()) {
                    click(clickTag);
                }
                break;
        }
    }

    /**
     * 检查第一页数据完整性
     *
     * @return
     */
    private boolean checkDataFirstPage() {
        if (list.size() <= 0) {
            UIUtil.showToast("请选择要上传的头像");
            return false;
        }
        if (TextUtils.isEmpty(et_name.getText())) {
            UIUtil.showToast("请填写姓名");
            return false;
        }
        if (TextUtils.isEmpty(sex)) {
            UIUtil.showToast("请选择性别");
            return false;
        }
        if (TextUtils.isEmpty(et_phone.getText())) {
            UIUtil.showToast("请填写电话");
            return false;
        }
        return true;
    }

    /**
     * 检查第二页数据完整性
     *
     * @return
     */
    private boolean checkDataSecondPage() {
        if (TextUtils.isEmpty(education)) {
            UIUtil.showToast("请选择学历");
            return false;
        }
        if (TextUtils.isEmpty(experience)) {
            UIUtil.showToast("请选择工作年限");
            return false;
        }
        if (TextUtils.isEmpty(intentionJobs)) {
            UIUtil.showToast("请选择期望职位");
            return false;
        }
        if (TextUtils.isEmpty(wage)) {
            UIUtil.showToast("请选择期望薪资");
            return false;
        }
        return true;
    }


//    --------------------------期望职位开始-----------------------------------------------------------------------

    /**
     * 选择tag公告dialog
     */
    public void jobTagDialog(JobTagBean tagBean) {
        if (tagBean == null) {
            return;
        }

        View view = View.inflate(PerfectPersonInformation.this, R.layout.dialog_job_tag, null);
        showdialog(view);
        final RecyclerView tag1 = (RecyclerView) view.findViewById(R.id.tag1);
        final RecyclerView tag2 = (RecyclerView) view.findViewById(R.id.tag2);
        final RecyclerView tag3 = (RecyclerView) view.findViewById(R.id.tag3);
        TextView cancle = (TextView) view.findViewById(R.id.tv_cancel);
        TextView confirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tv_itle = (TextView) view.findViewById(R.id.tv_itle);
        tv_itle.setText("期望职位");
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setJonTag(cacheJobTag);
                myDialog.dismiss();
            }
        });
        tag1.setLayoutManager(new LinearLayoutManager(PerfectPersonInformation.this));
        JobPerfectInfoTagAdapter adapter = new JobPerfectInfoTagAdapter(PerfectPersonInformation.this, tagBean.getData());
        tag1.setAdapter(adapter);
        adapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {

                tag2.setLayoutManager(new LinearLayoutManager(PerfectPersonInformation.this));
                adapter1 = new JobPerfectInfoTagAdapter1(PerfectPersonInformation.this);
                tag2.setAdapter(adapter1);
                adapter1.setCommonInterface(new RecyclerViewCommonInterface() {
                    @Override
                    public void onClick(Object bean) {
                        //点击了第二页的
                        tag3.setLayoutManager(new LinearLayoutManager(PerfectPersonInformation.this));
                        adapter2 = new JobPerfectInfoTagAdapter2(PerfectPersonInformation.this);
                        tag3.setAdapter(adapter2);
                        adapter2.setCommonInterface(new RecyclerViewCommonInterface() {
                            @Override
                            public void onClick(Object bean) {
                                //点击了第三页
                                intentionJobsId3 = ((JobTagBean.DataBean) bean).getId() + "";
                                cacheJobTag = (JobTagBean.DataBean) bean;
                            }
                        });

                        jobStep = 2;
                        intentionJobsId2 = ((JobTagBean.DataBean) bean).getId() + "";
                        getJobData(((JobTagBean.DataBean) bean).getId() + "");
                    }
                });

                //点击了第一页的
                jobStep = 1;
                intentionJobsId1 = ((JobTagBean.DataBean) bean).getId() + "";
                getJobData(((JobTagBean.DataBean) bean).getId() + "");
            }
        });

    }

    private void getJobData(String id) {
        Call<JobTagBean> jobTagBeanCall = RestAdapterManager.getApi().getJobType(id);
        jobTagBeanCall.enqueue(new JyCallBack<JobTagBean>() {
            @Override
            public void onSuccess(Call<JobTagBean> call, Response<JobTagBean> response) {
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    if (jobStep == 0) {
                        jobTagDialog(response.body());
                    } else if (jobStep == 1) {
                        adapter1.ClearData();
                        adapter1.addList(response.body().getData());
                    } else if (jobStep == 2) {
                        adapter2.ClearData();
                        adapter2.addList(response.body().getData());
                    }
                } else {
                    UIUtil.showToast("接口错误");
                }


            }

            @Override
            public void onError(Call<JobTagBean> call, Throwable t) {
                LogUtils.e(t.getMessage());
                UIUtil.showToast("接口错误");
            }

            @Override
            public void onError(Call<JobTagBean> call, Response<JobTagBean> response) {

            }
        });
    }

    private void setJonTag(JobTagBean.DataBean cacheJobTag) {
        if (cacheJobTag == null) {
            UIUtil.showToast("请选择");
            return;
        }
        intentionJobsId = intentionJobsId1 + "." + intentionJobsId2 + "." + intentionJobsId3;
        intentionJobs = cacheJobTag.getCategoryname();
        tv_job.setText(cacheJobTag.getCategoryname());
    }
//-----------------------------------------------------------------------期望职位结束----------------------------------------------------------


    /**
     * 统一处理各个类型的点击事件
     *
     * @param tag
     */
    private void click(String tag) {
        if (commonTagBean == null) {
            getTagItmes();

        } else {
            switch (tag) {
                case "QS_education":
                    commonDialog(commonTagBean.getData().getQS_education());
                    break;
                case "QS_experience":
                    commonDialog(commonTagBean.getData().getQS_experience());
                    break;
                case "QS_jobs_nature":
                    commonDialog(commonTagBean.getData().getQS_jobtag());
                    break;
                case "QS_wage":
                    commonDialog(commonTagBean.getData().getQS_wage());
                    break;
            }

        }
    }

    /**
     * 选择tag公告dialog
     */
    public void commonDialog(List<CommonTagItemBean> tagBean) {
        if (tagBean == null || tagBean.size() <= 0) {
            return;
        }

        View view = View.inflate(PerfectPersonInformation.this, R.layout.dialog_common_tag, null);
        showdialog(view);
        RecyclerView tag_recyclerview = (RecyclerView) view.findViewById(R.id.tag_recyclerview);
        TextView cancle = (TextView) view.findViewById(R.id.tv_cancel);
        TextView confirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        switch (clickTag) {
            case "QS_education":
                tv_title.setText("最高学历");
                break;
            case "QS_experience":
                tv_title.setText("工作经验");
                break;
            case "QS_wage":
                tv_title.setText("期望薪资");
                break;
        }
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
        switch (clickTag) {
            case "QS_education":
                education = tagBean.getCId() + "";
                educationCn = tagBean.getCName();
                tv_resume.setText(tagBean.getCName());
                break;
            case "QS_experience":
                experience = tagBean.getCId() + "";
                experienceCn = tagBean.getCName();
                tv_work_age.setText(tagBean.getCName());
                break;
            case "QS_jobs_nature":
//                tv_job.setText(tagBean.getCName());
                break;
            case "QS_wage":
                tv_wage.setText(tagBean.getCName());
                wage = tagBean.getCId() + "";
                wageCn = tagBean.getCName();
                break;
        }
        cacheCommonTagBean = null;
    }

    /**
     * 根据对应的分类id获取对应的数据
     */

    private void getTagItmes() {
        List<String> list = new ArrayList<>();
        list.add("QS_education");
        list.add("QS_experience");
        list.add("QS_wage");
        GetBaseTagBean tagBean=new GetBaseTagBean();
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
     * 获取所有的标签分类
     * 暂时不用，数据写死
     */
    private void getAllTag() {
        final Call<AllTagBean> allTagBeanCall = RestAdapterManager.getApi().getAllTags();
        allTagBeanCall.enqueue(new JyCallBack<AllTagBean>() {
            @Override
            public void onSuccess(Call<AllTagBean> call, Response<AllTagBean> response) {
//                LogUtils.e(response.body().getMsg());
                if (response != null && response.body() != null && response.body().getCode() == 1000) {
                    allTagBean = response.body();
                    BaseContext.getInstance().setAllTagBean(response.body());
                }

            }

            @Override
            public void onError(Call<AllTagBean> call, Throwable t) {
                LogUtils.e(t.getMessage());
            }

            @Override
            public void onError(Call<AllTagBean> call, Response<AllTagBean> response) {
                try {
                    LogUtils.e(response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


///////////////////////////////////////////////////////////////////上传头像///////////////////////////////////////////////////////

    /**
     * 选择图片dialog
     */
    public void photodialog() {

        View view = View.inflate(PerfectPersonInformation.this, R.layout.dialog_publish_photo, null);
        showdialog1(view);

        final TextView photo = (TextView) view.findViewById(R.id.photo);
        final TextView picture = (TextView) view.findViewById(R.id.picture);
        // 从图库中选择
        photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (photo.getText().toString().contains(getResources().getString(R.string.publish_photo))) {

                    Intent intent = new Intent(PerfectPersonInformation.this, PhotoPicActivity.class);
                    intent.putExtra("max", 1);

                    startActivityForResult(intent, resultCode_Photos);
                    myDialog.dismiss();
                }

            }
        });
        // 拍照
        picture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (picture.getText().toString().contains(getResources().getString(R.string.publish_picture))) {
                    Intent intent = new Intent(PerfectPersonInformation.this, TakingPicturesActivity.class);

                    startActivityForResult(intent, resultCode_Camera);
                    myDialog.dismiss();
                }
            }
        });

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

    /**
     * 弹出dialog
     *
     * @param view
     */
    private void showdialog1(View view) {

        myDialog = new MyDialog(this, 0, 0, view, R.style.dialog);
        myDialog.show();
    }

    /**
     * 弹出dialog
     *
     * @param view
     */
    private void showdialog2(View view) {

        myDialog = new MyDialog(this, 0, 0, Gravity.CENTER_VERTICAL, view, R.style.dialog);
        myDialog.setCancelable(false);
        myDialog.show();
    }

    /**
     * 上传头像
     */
    private void upLoadImage() {
        DialogUtils.showDialog(this, "上传中", false);
        List<MultipartBody.Part> parts = UploadFile.filesToMultipartBody(list);
        upLoadImageCall = RestAdapterManager.getApi().uploadFile(parts.get(0));
        upLoadImageCall.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
//                UIUtil.showToast(response.body());
//                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    //上传图片成功
                    headimg = response.body().getData();
                    LogUtils.e(headimg);
//                        UIUtil.showToast("成功" + headimg);
                    upLoadInfo();
                } else {
                    UIUtil.showToast("上传头像失败");
                    DialogUtils.closeDialog();
                }
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast("上传头像失败");
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                try {
                    DialogUtils.closeDialog();
                    ErrorMessageUtils.taostErrorMessage(PerfectPersonInformation.this, response.errorBody().string(), "");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void upLoadInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
//        map.put("uid", "48");
        map.put("avatars", headimg);
        map.put("realname", et_name.getText().toString());
        map.put("sex", sex);
        map.put("sexCn", sexCn);
        map.put("education", education);
        map.put("educationCn", educationCn);
        map.put("wage", wage);
        map.put("wageCn", wageCn);
        map.put("intentionJobsId", intentionJobsId);
        map.put("intentionJobs", intentionJobs);
        map.put("experience", experience);
        map.put("experienceCn", experienceCn);
        map.put("phone", et_phone.getText().toString());
        Call<SuperBean<UserInfoBean>> upLoadCall = RestAdapterManager.getApi().perfectBaseInfo(map);
        upLoadCall.enqueue(new JyCallBack<SuperBean<UserInfoBean>>() {
            @Override
            public void onSuccess(Call<SuperBean<UserInfoBean>> call, Response<SuperBean<UserInfoBean>> response) {
                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    BaseContext.getInstance().setUserInfo(response.body().getData());
                    perfectSuccessDialog();
                } else {
                    try {
                        UIUtil.showToast(response.body().getMsg());
                    } catch (Exception e) {
                    }

                }
            }

            @Override
            public void onError(Call<SuperBean<UserInfoBean>> call, Throwable t) {
                UIUtil.showToast("接口异常");
                DialogUtils.closeDialog();
            }

            @Override
            public void onError(Call<SuperBean<UserInfoBean>> call, Response<SuperBean<UserInfoBean>> response) {
                UIUtil.showToast("接口异常");
                DialogUtils.closeDialog();
            }
        });
    }

    /**
     * 完善成功确认dialog
     */
    public void perfectSuccessDialog() {

        View view = View.inflate(PerfectPersonInformation.this, R.layout.dialog_perfect_success, null);
        showdialog2(view);

        final Button bt_confirm = (Button) view.findViewById(R.id.bt_confirm);
        bt_confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfectPersonInformation.this, MainActivity.class);
                startActivity(intent);
                myDialog.dismiss();
                AppManager.getAppManager().finishActivity(LoginActivity.class);
                EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.PERFECT_INFO_SUCCESS));
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        list.clear();
        if (resultCode == resultCode_Camera) {
            //相机返回图片
            Bundle b = data.getExtras();
            String fileName = b.getString("picture");
            list.add(fileName);
        } else if (resultCode == resultCode_Photos) {
            // 图库中选择
            if (data == null || "".equals(data)) {
                return;
            }
            list = data.getExtras().getStringArrayList("photo");
            LogUtils.e("image路径--" + list.get(0));
        }
//        headIsChange = true;
        ImageLoadedrManager.getInstance().displayNoFilter(this, list.get(0), iv_head);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
