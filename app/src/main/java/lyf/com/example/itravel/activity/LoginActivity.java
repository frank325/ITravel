package lyf.com.example.itravel.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.callback.Callback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import lyf.com.example.itravel.ITravelApplication;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.bean.User;
import lyf.com.example.itravel.model.OkhttpModel;
import lyf.com.example.itravel.utlis.MD5Utils;
import lyf.com.example.itravel.utlis.NetUtils;
import lyf.com.example.itravel.utlis.SPUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 登录页面
 */

public class LoginActivity extends AppCompatActivity {

    private SPUtils spUtils = new SPUtils();
    private String account, password, md5Password;
    private HashMap<String, String> hashMap = new HashMap<>();
    private boolean isVisibilityOn = false;

    @BindView(R.id.et_account) EditText etAccount;
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.tv_register) TextView tvRegister;
    @BindView(R.id.iv_visibility_on_off) ImageView ivVisibilityOnOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this); //绑定依赖注入ButterKnife
        EventBus.getDefault().register(this); //注册EventBus
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this); //注册EventBus
    }

    /**
     *接收EventBus
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void userEventBus(User user){
        etAccount.setText(user.getAccount());
        etPassword.setText(user.getPassword());
    }

    /**
     * 执行用户登录
     */
    private void doLogin() {
        account = etAccount.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if(TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "帐号或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!NetUtils.isConnected(LoginActivity.this)) {
            Toast.makeText(LoginActivity.this, "无互联网连接", Toast.LENGTH_SHORT).show();
            return;
        }

        /**
         * 密码MD5加密
         */
        md5Password = MD5Utils.StringToMD5(password);

        hashMap.put("account", account);
        hashMap.put("password", md5Password);

        /**
         * 发送网络请求
         */
        OkhttpModel okhttpModel = new OkhttpModel();
        okhttpModel.doGet("login.do", hashMap, new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(LoginActivity.this, "错误！请重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response, int id) {
                Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                if("登录成功！".equals(response.toString())) {

                    /**
                     * 将用户信息存入Application类
                     */
                    ITravelApplication iTravelApplication = ITravelApplication.getiTravelApplication();
                    iTravelApplication.setUser(new User(account, md5Password));

                    /**
                     * 将用户信息存入SP
                     */
                    spUtils.put(LoginActivity.this, "account", account);
                    spUtils.put(LoginActivity.this, "password", md5Password);

                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    /**
     * 响应点击事件，执行登录
     */
    @OnClick(R.id.bt_login) public void login() {
        doLogin();
    }

    /**
     * 响应点击事件，设置可见或不可见密码
     */
    @OnClick(R.id.iv_visibility_on_off) public void visibilityOnOff() {
        if(isVisibilityOn) {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etPassword.setSelection(etPassword.getText().toString().length());
            ivVisibilityOnOff.setBackgroundResource(R.drawable.visibility_off);
            isVisibilityOn = false;
        }else {
            etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            etPassword.setSelection(etPassword.getText().toString().length());
            ivVisibilityOnOff.setBackgroundResource(R.drawable.visibility_on);
            isVisibilityOn = true;
        }
    }

    /**
     * 响应点击事件，跳转至注册页面
     */
    @OnClick(R.id.tv_register) public void register() {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

}
