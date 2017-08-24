package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.adapter.JobPerfectInfoTagAdapter;
import com.niuyun.hire.ui.bean.JobTagBean;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.ui.utils.LoginUtils;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.ErrorMessageUtils;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.utils.UploadFile;
import com.niuyun.hire.utils.photoutils.TakeSimpleActivity;
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

import static com.niuyun.hire.base.Constants.resultCode_header_Camera;
import static com.niuyun.hire.base.Constants.resultCode_header_Photos;

/**
 * Created by chen.zhiwei on 2017-8-23.
 */

public class EnterpriseEditPersonActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_view)
    TitleBar titleView;
    @BindView(R.id.iv_header)
    CircularImageView ivHeader;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.iv_position_more)
    ImageView ivPositionMore;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.bt_next)
    Button bt_next;
    List<String> list = new ArrayList<>();
    private Call<SuperBean<String>> upLoadImageCall;
    private String headimg;
    private String intentionJobsId;//期望职位,每一级的id中间用 . 隔开
    private String intentionJobs;

    private String intentionJobsId1;
    private String intentionJobsId2;
    private String intentionJobsId3;


    private int jobStep = 0;
    private JobPerfectInfoTagAdapter adapter1;
    private JobPerfectInfoTagAdapter adapter2;
    private JobTagBean.DataBean cacheJobTag;
    private JobTagBean JobBean;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_enterprise_person_info;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        bt_next.setOnClickListener(this);
        ivHeader.setOnClickListener(this);
        tvPosition.setOnClickListener(this);
        ivPositionMore.setOnClickListener(this);
    }

    @Override
    public void loadData() {
        setdata();
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

    private void setdata() {
        if (BaseContext.getInstance().getUserInfo() != null) {
            try {
                ImageLoadedrManager.getInstance().display(this, Constants.COMMON_PERSON_URL + BaseContext.getInstance().getUserInfo().avatars, ivHeader, R.mipmap.ic_default_head);
                etName.setText(BaseContext.getInstance().getUserInfo().username);
                etPhone.setText(BaseContext.getInstance().getUserInfo().mobile);
                tvPosition.setText(BaseContext.getInstance().getUserInfo().contactTitle);
                intentionJobs = BaseContext.getInstance().getUserInfo().contactTitle;
                et_email.setText(BaseContext.getInstance().getUserInfo().email);
            } catch (Exception e) {
            }
        }
    }

    private boolean checkData() {
//        if (list.size() <= 0) {
//            UIUtil.showToast("请选择头像");
//            return false;
//        }
        if (TextUtils.isEmpty(etName.getText().toString())) {
            UIUtil.showToast("请输入名字");
            return false;
        }
        if (TextUtils.isEmpty(tvPosition.getText())) {
            UIUtil.showToast("请选择职位");
            return false;
        }
        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            UIUtil.showToast("请输入电话");
            return false;
        }
        if (TextUtils.isEmpty(et_email.getText().toString())) {
            UIUtil.showToast("请输入邮箱");
            return false;
        }
        return true;
    }

    private void initTitle() {

        titleView.setTitle("完善个人信息");
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
        titleView.addAction(new TitleBar.TextAction("提交") {
            @Override
            public void performAction(View view) {
                if (!UIUtil.isFastDoubleClick()) {
                    upload();
                }
            }
        });
        titleView.setImmersive(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_header://个人头像
                photodialog();
                break;
            case R.id.tv_position://职位
            case R.id.iv_position_more:
                if (!UIUtil.isFastDoubleClick()) {
                    jobStep = 0;
                    if (JobBean == null) {
                        DialogUtils.showDialog(this, "加载...", false);
                        getJobData("0");
                    } else {
                        jobTagDialog(JobBean);
                    }
                }
                break;
            case R.id.bt_next:
                upload();
                break;
        }
    }

    private void upload() {
        if (checkData()) {
            if (list.size() > 0) {
                if (!TextUtils.isEmpty(headimg)) {
                    upLoadInfo();
                } else {
                    upLoadImage();
                }
            } else {
                upLoadInfo();
            }
        }
    }
    ///////////////////////////////////////////////////////////////////上传头像///////////////////////////////////////////////////////

    /**
     * 选择图片dialog
     */
    public void photodialog() {

        View view = View.inflate(EnterpriseEditPersonActivity.this, R.layout.dialog_publish_photo, null);
        showdialog1(view);

        final TextView photo = (TextView) view.findViewById(R.id.photo);
        final TextView picture = (TextView) view.findViewById(R.id.picture);
        // 从图库中选择
        photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (photo.getText().toString().contains(getResources().getString(R.string.publish_photo))) {

                    Intent intent = new Intent(EnterpriseEditPersonActivity.this, TakeSimpleActivity.class);
                    intent.putExtra("Type", 1);
                    //头像
                    startActivityForResult(intent, resultCode_header_Photos);
                    myDialog.dismiss();
                }

            }
        });
        // 拍照
        picture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (picture.getText().toString().contains(getResources().getString(R.string.publish_picture))) {
                    Intent intent = new Intent(EnterpriseEditPersonActivity.this, TakeSimpleActivity.class);
                    intent.putExtra("Type", 0);
                    //头像
                    startActivityForResult(intent, resultCode_header_Camera);
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
                    ErrorMessageUtils.taostErrorMessage(EnterpriseEditPersonActivity.this, response.errorBody().string(), "");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    //    --------------------------期望职位开始-----------------------------------------------------------------------

    /**
     * 选择tag公告dialog
     */
    public void jobTagDialog(JobTagBean tagBean) {
        if (tagBean == null) {
            return;
        }

        View view = View.inflate(EnterpriseEditPersonActivity.this, R.layout.dialog_job_tag, null);
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
                if (jobStep != 3) {
                    UIUtil.showToast("请选择");
                    return;
                }
                setJonTag(cacheJobTag);
                myDialog.dismiss();
            }
        });
        tag1.setLayoutManager(new LinearLayoutManager(EnterpriseEditPersonActivity.this));
        JobPerfectInfoTagAdapter adapter = new JobPerfectInfoTagAdapter(EnterpriseEditPersonActivity.this, tagBean.getData());
        tag1.setAdapter(adapter);
        adapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {

                tag2.setLayoutManager(new LinearLayoutManager(EnterpriseEditPersonActivity.this));
                adapter1 = new JobPerfectInfoTagAdapter(EnterpriseEditPersonActivity.this);
                tag2.setAdapter(adapter1);
                adapter1.setCommonInterface(new RecyclerViewCommonInterface() {
                    @Override
                    public void onClick(Object bean) {
                        //点击了第二页的
                        tag3.setLayoutManager(new LinearLayoutManager(EnterpriseEditPersonActivity.this));
                        adapter2 = new JobPerfectInfoTagAdapter(EnterpriseEditPersonActivity.this);
                        tag3.setAdapter(adapter2);
                        adapter2.setCommonInterface(new RecyclerViewCommonInterface() {
                            @Override
                            public void onClick(Object bean) {
                                //点击了第三页
                                jobStep = 3;
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

    /**
     * 根据各级id获取职位信息
     *
     * @param id
     */
    private void getJobData(String id) {
        Call<JobTagBean> jobTagBeanCall = RestAdapterManager.getApi().getJobType(id);
        jobTagBeanCall.enqueue(new JyCallBack<JobTagBean>() {
            @Override
            public void onSuccess(Call<JobTagBean> call, Response<JobTagBean> response) {
                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    if (jobStep == 0) {
                        JobBean = response.body();
                        jobTagDialog(JobBean);
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
                DialogUtils.closeDialog();
                UIUtil.showToast("接口错误");
            }

            @Override
            public void onError(Call<JobTagBean> call, Response<JobTagBean> response) {

            }
        });
    }

    private void setJonTag(JobTagBean.DataBean cacheJobTag) {
        if (cacheJobTag == null) {
            DialogUtils.closeDialog();
            UIUtil.showToast("请选择");
            return;
        }
        intentionJobsId = intentionJobsId1 + "." + intentionJobsId2 + "." + intentionJobsId3;
        intentionJobs = cacheJobTag.getCategoryname();
        tvPosition.setText(cacheJobTag.getCategoryname());
    }

    //-----------------------------------------------------------------------期望职位结束----------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        list.clear();
        if (resultCode == resultCode_header_Camera) {
            //相机返回图片
//                Bundle b = data.getExtras();
//                String fileName = b.getString("picture");
            list = data.getStringArrayListExtra("picture");
        } else if (resultCode == resultCode_header_Photos) {
            // 图库中选择
            if (data == null || "".equals(data)) {
                return;
            }
            list = data.getStringArrayListExtra("photo");
        }
        if (list.size() > 0) {
            ImageLoadedrManager.getInstance().displayNoFilter(this, list.get(0), ivHeader);
        }
    }

    private void upLoadInfo() {
        DialogUtils.showDialog(this, "", false);
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(headimg)) {

            map.put("avatars", headimg);
        } else {
            map.put("avatars", BaseContext.getInstance().getUserInfo().avatars);
        }
        map.put("contact", etName.getText().toString());
        map.put("telephone", etPhone.getText().toString());
        map.put("contactTitle", intentionJobs);
        map.put("uid", BaseContext.getInstance().getUserInfo().uid + "");
        map.put("companyId", BaseContext.getInstance().getUserInfo().companyId + "");
//        map.put("companyId", BaseContext.getInstance().getUserInfo().email + "");
        map.put("email", et_email.getText().toString());

        Call<SuperBean<String>> upLoadInfo = RestAdapterManager.getApi().editEnterpreInfo(map);
        upLoadInfo.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
                try {
                    UIUtil.showToast(response.body().getMsg());
                } catch (Exception e) {
                }
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    LoginUtils.getUserByUid();
                    EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.PERFECT_INFO_SUCCESS));
                    finish();
                }
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Throwable t) {
                DialogUtils.closeDialog();
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
            }
        });
    }
}
