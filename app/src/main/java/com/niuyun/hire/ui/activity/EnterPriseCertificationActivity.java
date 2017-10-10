package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.AppManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.index.EnterpriseMainActivity;
import com.niuyun.hire.ui.index.MainActivity;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.ErrorMessageUtils;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.utils.UploadFile;
import com.niuyun.hire.utils.photoutils.TakeSimpleActivity;
import com.niuyun.hire.view.MyDialog;
import com.niuyun.hire.view.TitleBar;

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
 * 企业认证
 * Created by chen.zhiwei on 2017-7-31.
 */

public class EnterPriseCertificationActivity extends BaseActivity {
    @BindView(R.id.title_view)
    TitleBar titleView;
    @BindView(R.id.iv_add_business_license)
    ImageView iv_add_business_license;
    @BindView(R.id.bt_certification)
    Button bt_certification;
    List<String> list = new ArrayList<String>();
    private Call<SuperBean<String>> upLoadImageCall;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_enterprise_certification;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        iv_add_business_license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photodialog();
            }
        });
        bt_certification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.size() > 0) {
                    upLoadImage();
                } else {
                    UIUtil.showToast("请选择图片");
                }
            }
        });
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

    private void initTitle() {

        titleView.setTitle("企业认证");
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
        titleView.addAction(new TitleBar.TextAction("跳过") {
            @Override
            public void performAction(View view) {
                goNext();
            }
        });
        titleView.setImmersive(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goNext();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goNext() {

        if (BaseContext.getInstance().getUserInfo()!=null&&BaseContext.getInstance().getUserInfo().utype == 1) {
            startActivity(new Intent(this, EnterpriseMainActivity.class));
            AppManager.getAppManager().finishActivity(MainActivity.class);
        } else {
            startActivity(new Intent(this, MainActivity.class));
            AppManager.getAppManager().finishActivity(EnterpriseMainActivity.class);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        list.clear();
        if (resultCode == resultCode_header_Camera) {
            //相机返回图片
            list .addAll(data.getStringArrayListExtra("picture")) ;
        } else if (resultCode == resultCode_header_Photos) {
            // 图库中选择
            if (data == null || "".equals(data)) {
                return;
            }
            list .addAll(data.getStringArrayListExtra("photo")) ;
            LogUtils.e("image路径--" + list.get(0));
        }
//        headIsChange = true;
        if (list.size() > 0) {
            ImageLoadedrManager.getInstance().displayNoFilter(this, list.get(0), iv_add_business_license);
        }
    }
    ///////////////////////////////////////////////////////////////////上传头像///////////////////////////////////////////////////////

    /**
     * 选择图片dialog
     */
    public void photodialog() {

        View view = View.inflate(EnterPriseCertificationActivity.this, R.layout.dialog_publish_photo, null);
        showdialog1(view);

        final TextView photo = (TextView) view.findViewById(R.id.photo);
        final TextView picture = (TextView) view.findViewById(R.id.picture);
        // 从图库中选择
        photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (photo.getText().toString().contains(getResources().getString(R.string.publish_photo))) {

                    Intent intent = new Intent(EnterPriseCertificationActivity.this, TakeSimpleActivity.class);
                    intent.putExtra("Type", 1);
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
                    Intent intent = new Intent(EnterPriseCertificationActivity.this, TakeSimpleActivity.class);
                    intent.putExtra("Type", 0);
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
        upLoadImageCall = RestAdapterManager.getApi().uploadCertificateImage(parts.get(0));
        upLoadImageCall.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
//                UIUtil.showToast(response.body());
                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    //上传图片成功
                    bindImage(response.body().getData());
                    try {
                        UIUtil.showToast(response.body().getMsg());
                    } catch (Exception e) {

                    }
                } else {
                    try {
                        UIUtil.showToast(response.body().getMsg());
                    } catch (Exception e) {

                    }
                    DialogUtils.closeDialog();
                }
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast("上传失败");
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                try {
                    DialogUtils.closeDialog();
                    ErrorMessageUtils.taostErrorMessage(EnterPriseCertificationActivity.this, response.errorBody().string(), "");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void bindImage(String imageid) {
        DialogUtils.showDialog(this, "", false);
        Map<String, String> map = new HashMap<>();
        map.put("certificateImg", imageid);
        map.put("companyId", BaseContext.getInstance().getUserInfo().companyId + "");
        map.put("uid", BaseContext.getInstance().getUserInfo().uid + "");
        Call<String> bindCenterpriseImage = RestAdapterManager.getApi().bindCenterpriseImage(map);
        bindCenterpriseImage.enqueue(new JyCallBack<String>() {
            @Override
            public void onSuccess(Call<String> call, Response<String> response) {
                startActivity(new Intent(EnterPriseCertificationActivity.this, EnterpriseMainActivity.class));
                finish();
                DialogUtils.closeDialog();
            }

            @Override
            public void onError(Call<String> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast("上传失败");
            }

            @Override
            public void onError(Call<String> call, Response<String> response) {
                DialogUtils.closeDialog();
                UIUtil.showToast("上传失败");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (upLoadImageCall != null) {
            upLoadImageCall.cancel();
        }
    }
}
