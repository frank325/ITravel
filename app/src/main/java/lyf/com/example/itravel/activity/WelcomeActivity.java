package lyf.com.example.itravel.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import lyf.com.example.itravel.ITravelApplication;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.bean.User;
import lyf.com.example.itravel.utlis.SPUtils;

/**
 * 欢迎页面
 */

public class WelcomeActivity extends AppCompatActivity {

    private SPUtils spUtils = new SPUtils();
    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        goToActivity();
    }

    /**
     * 页面跳转
     */
    private void goToActivity() {

        /**
         * 判断SP是否有用户信息，如果有直接跳转至主页面并将信息存入Application类，如果没有跳转至登录页面
         */
        if(spUtils.contains(WelcomeActivity.this, "account") &&
                spUtils.contains(WelcomeActivity.this, "password")) {
            intent.setClass(WelcomeActivity.this, MainActivity.class);
            ITravelApplication iTravelApplication = ITravelApplication.getiTravelApplication();
            iTravelApplication.setUser(new User(spUtils.get(WelcomeActivity.this, "account", "account").toString(),
                    spUtils.get(WelcomeActivity.this, "password", "password").toString()));
        }else {
            intent.setClass(WelcomeActivity.this, LoginActivity.class);
        }

        /**
         * 等待2秒跳转页面
         */
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(intent); //执行
                finish();
            }
        };
        timer.schedule(task, 2000);
    }
}
