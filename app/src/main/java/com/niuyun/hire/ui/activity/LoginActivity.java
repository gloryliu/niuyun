package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.bean.UserInfoBean;
import com.niuyun.hire.ui.index.MainActivity;
import com.niuyun.hire.ui.utils.LoginUtils;
import com.niuyun.hire.utils.NetUtil;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.CleanableEditText;
import com.niuyun.hire.view.CustomProgressDialog;

import butterknife.BindString;
import butterknife.BindView;
import retrofit2.Call;


/**
 * 登录
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.ll_login)
    LinearLayout ll_login;
    //titleView
//    @BindView(R.id.title_view)
//    TitleBar titleView;
    //用户名，手机号码
    @BindView(R.id.user_name)
    CleanableEditText userName;
    //密码
    @BindView(R.id.pass_word)
    EditText passWord;
    //登陆
    @BindView(R.id.login)
    Button login;
    //忘记密码
    @BindView(R.id.forget_password)
    TextView forgetPassword;
    //注册
    @BindView(R.id.regist)
    Button regist;
    @BindView(R.id.tv_pass)
    TextView tv_pass;//随便看看
    /**
     * 是否清除密码
     */
    private boolean isClearPwd;
    //进度窗
    private CustomProgressDialog mDialog;
    //注册成功标识
    public static final int REGIST_CODE = 2;
    @BindString(R.string.login)
    String logins;

    Call<SuperBean<UserInfoBean>> loginCall;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.login_activity;
    }

    @Override
    public void initViewsAndEvents() {
        setIsOpenTitle(false);
        initTitle();
        mDialog = new CustomProgressDialog(this, "登录中...");

        login.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        regist.setOnClickListener(this);
        tv_pass.setOnClickListener(this);

    }

    @Override
    public void loadData() {
        UserInfoBean userInfo = BaseContext.getInstance().getUserInfo();
        if (null != userInfo) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
//        if (null != userInfo) {
//            userName.setText(userInfo.phone);
//            userName.setSelection(userName.getText().length());
//        }
//        setLoginState();
    }

    @Override
    protected View isNeedLec() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                //登陆
                UIUtil.ShowOrHideSoftInput(this, false);
                if (!NetUtil.isNetworkConnected(LoginActivity.this)) {
                    Toast.makeText(LoginActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    return;
                } else {
//                    startActivity(new Intent(this, MainActivity.class));
                    Login();
                }
                break;
            case R.id.regist:
                //注册
                Intent registIntent = new Intent(this, SelectedRegisterRoler.class);
                startActivityForResult(registIntent, REGIST_CODE);
                break;
            case R.id.forget_password:
                //忘记密码
                Intent forgetIntent = new Intent(this, FindPasswordActivity.class);
                startActivity(forgetIntent);
                break;
            case R.id.tv_pass:
                //
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String username = data.getStringExtra("user_name");
            String password = data.getStringExtra("password");
            userName.setText(username);
            passWord.setText(password);
//            setLoginState();
        }
    }

    /**
     * 登陆
     */
    private void Login() {
        final String telNumber = userName.getText().toString();
        final String password = passWord.getText().toString();
        String checkCode = "";
        if (TextUtils.isEmpty(telNumber) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "账号或者密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (telNumber.trim().length() != 11) {
            Toast.makeText(this, "请输入11位账号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "密码至少为6位", Toast.LENGTH_SHORT).show();
            return;
        }
        LoginUtils.commitlogin(this, userName.getText().toString().trim(), passWord.getText().toString().trim());
//        commitlogin();

    }

    @Override
    public boolean isRegistEventBus() {
        return true;
    }

    @Override
    public void onMsgEvent(EventBusCenter eventCenter) {
        if (null != eventCenter) {
            if (Constants.LOGIN_SUCCESS == eventCenter.getEvenCode()) {
                finish();
            }
        }
    }

    /**
     * 初始化标题
     */
    private void initTitle() {

//        titleView.setTitle("登陆");
//        titleView.setTitleColor(Color.WHITE);
//        titleView.setBackgroundColor(getResources().getColor(R.color.color_ff6900));
//        titleView.setImmersive(true);
    }

}
