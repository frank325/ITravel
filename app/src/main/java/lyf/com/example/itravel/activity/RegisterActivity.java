package lyf.com.example.itravel.activity;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhy.http.okhttp.callback.Callback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.bean.User;
import lyf.com.example.itravel.model.OkhttpModel;
import lyf.com.example.itravel.utlis.MD5Utils;
import lyf.com.example.itravel.utlis.NetUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 注册页面
 */

public class RegisterActivity extends AppCompatActivity {

    private String account, password, confirmPassword, md5Password;
    private HashMap<String, String> hashMap = new HashMap<>();
    private boolean isPasswordVisibilityOn = false;
    private boolean isConfirmPasswordVisibilityOn = false;

    @BindView(R.id.et_account) EditText etAccount;
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.et_confirm_password) EditText etConfirmPassword;

    @BindView(R.id.iv_password_visibility_on_off) ImageView ivPasswordVisibilityOnOff;
    @BindView(R.id.iv_confirm_password_visibility_on_off) ImageView ivConfirmPasswordVisibilityOnOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this); //绑定依赖注入ButterKnife
    }

    /**
     * 响应点击事件，执行注册
     */
    @OnClick(R.id.bt_register) public void register() {
        doRegister();
    }

    /**
     * 执行用户注册
     */
    private void doRegister() {
        account = etAccount.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        confirmPassword = etConfirmPassword.getText().toString().trim();

        if(account.length() < 6) {
            Toast.makeText(RegisterActivity.this, "帐号不能小于6位", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length() < 6) {
            Toast.makeText(RegisterActivity.this, "密码不能小于6位", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password.equals(confirmPassword)) {
            Toast.makeText(RegisterActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!NetUtils.isConnected(RegisterActivity.this)) {
            Toast.makeText(RegisterActivity.this, "无互联网连接", Toast.LENGTH_SHORT).show();
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
        okhttpModel.doGet("registerUser.do", hashMap, new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(RegisterActivity.this, "错误！请重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response, int id) {
                if("注册成功！".equals(response.toString())) {
                    /**
                     * 弹出Snackbar，设置监听器，点击跳转至登录页面
                     */
                    Snackbar.make(etConfirmPassword, response.toString(), Snackbar.LENGTH_LONG)
                            .setAction("前往登录", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EventBus.getDefault().post(new User(account, password)); //发送EventBus
                                    finish();
                                }
                            }).setActionTextColor(Color.parseColor("#3CB047")).show();
                }else {
                    Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 响应点击事件，返回登录页面
     */
    @OnClick(R.id.iv_back) public void back() {
        finish();
    }

    /**
     * 响应点击事件，设置可见或不可见密码
     */
    @OnClick(R.id.iv_password_visibility_on_off) public void passwordVisibilityOnOff() {
        if(isPasswordVisibilityOn) {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etPassword.setSelection(etPassword.getText().toString().length());
            ivPasswordVisibilityOnOff.setBackgroundResource(R.drawable.visibility_off);
            isPasswordVisibilityOn = false;
        }else {
            etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            etPassword.setSelection(etPassword.getText().toString().length());
            ivPasswordVisibilityOnOff.setBackgroundResource(R.drawable.visibility_on);
            isPasswordVisibilityOn = true;
        }
    }

    /**
     * 响应点击事件，设置可见或不可见确认密码
     */
    @OnClick(R.id.iv_confirm_password_visibility_on_off) public void confirmPasswordVisibilityOnOff() {
        if(isConfirmPasswordVisibilityOn) {
            etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etConfirmPassword.setSelection(etConfirmPassword.getText().toString().length());
            ivConfirmPasswordVisibilityOnOff.setBackgroundResource(R.drawable.visibility_off);
            isConfirmPasswordVisibilityOn = false;
        }else {
            etConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            etConfirmPassword.setSelection(etConfirmPassword.getText().toString().length());
            ivConfirmPasswordVisibilityOnOff.setBackgroundResource(R.drawable.visibility_on);
            isConfirmPasswordVisibilityOn = true;
        }
    }
}
