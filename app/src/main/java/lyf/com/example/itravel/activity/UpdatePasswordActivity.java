package lyf.com.example.itravel.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.zhy.http.okhttp.callback.Callback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
 * 修改密码页面
 */

public class UpdatePasswordActivity extends AppCompatActivity {

    private HashMap<String, String> hashMap = new HashMap<>();
    private String account,password, newPassword, confirmPassword, md5Password;

    @BindView(R.id.tb_update_password) Toolbar tbUpdatePassword;
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.et_new_password) EditText etNewPassword;
    @BindView(R.id.et_confirm_password) EditText etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        ButterKnife.bind(this);
        initToolbar();
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        tbUpdatePassword.setNavigationIcon(R.drawable.back);
        tbUpdatePassword.setTitle("");

        setSupportActionBar(tbUpdatePassword); //设置SupportActionBar为Toolbar

        etPassword.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE); //自动弹出窗口

        tbUpdatePassword.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @OnClick(R.id.tv_save) public void save() {
        account = ITravelApplication.getiTravelApplication().getUser().getAccount();
        password = etPassword.getText().toString().trim();
        newPassword = etNewPassword.getText().toString().trim();
        confirmPassword = etConfirmPassword.getText().toString().trim();

        if(TextUtils.isEmpty(password) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(UpdatePasswordActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!newPassword.equals(confirmPassword)) {
            Toast.makeText(UpdatePasswordActivity.this, "两次密码输入不同", Toast.LENGTH_SHORT).show();
            return;
        }

        md5Password = MD5Utils.StringToMD5(password); //将密码转换成md5

        if (!ITravelApplication.getiTravelApplication().getUser().getPassword().equals(md5Password)) {
            Toast.makeText(UpdatePasswordActivity.this, "原密码错误", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!NetUtils.isConnected(UpdatePasswordActivity.this)) {
            Toast.makeText(UpdatePasswordActivity.this, "无互联网连接", Toast.LENGTH_SHORT).show();
            return;
        }

        md5Password = MD5Utils.StringToMD5(newPassword); //MD5加密

        updatePassword();
    }

    /**
     * 修改密码
     */
    private void updatePassword() {
        hashMap.put("data", newPassword);
        hashMap.put("dataName", "password");
        hashMap.put("account", account);

        /**
         * 发送网络请求
         */
        OkhttpModel okhttpModel = new OkhttpModel();
        okhttpModel.doGet("updateUserInfo.do", hashMap, new Callback() {

            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(UpdatePasswordActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response, int id) {
                if ("修改成功".equals(response.toString())) {
                    Toast.makeText(UpdatePasswordActivity.this, response.toString() + "！需重新登录", Toast.LENGTH_SHORT).show();

                    ITravelApplication.getiTravelApplication().setUser(new User("", ""));

                    SPUtils.remove(UpdatePasswordActivity.this, "account");
                    SPUtils.remove(UpdatePasswordActivity.this, "password");

                    Intent intent = new Intent();
                    intent.setClass(UpdatePasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

}
