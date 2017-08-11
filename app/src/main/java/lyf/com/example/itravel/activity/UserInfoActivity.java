package lyf.com.example.itravel.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.callback.Callback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lyf.com.example.itravel.ITravelApplication;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.adapter.UserInfoAdapter;
import lyf.com.example.itravel.bean.User;
import lyf.com.example.itravel.model.OkhttpModel;
import lyf.com.example.itravel.utlis.JsonAnalysisUtils;
import okhttp3.Call;
import okhttp3.Response;

public class UserInfoActivity extends AppCompatActivity {

    private String [] userInfo;
    private String account;
    private int resumeNum = 0;
    private User user;
    private UserInfoAdapter userInfoAdapter;
    private HashMap<String, String> hashMap = new HashMap<>();

    @BindView(R.id.tb_user_info) Toolbar tbUserInfo;
    @BindView(R.id.rv_user_info) RecyclerView rvUserInfo;
    @BindView(R.id.tv_refresh) TextView tvRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        ButterKnife.bind(this);
        initToolbar();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ++resumeNum;
        if (resumeNum != 1) {
            initData();
        }
    }

    private void initToolbar() {
        tbUserInfo.setNavigationIcon(R.drawable.back);
        tbUserInfo.setTitle("");

        setSupportActionBar(tbUserInfo); //设置SupportActionBar为Toolbar

        tbUserInfo.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initRecylerView() {
        rvUserInfo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        userInfoAdapter = new UserInfoAdapter(this);
        rvUserInfo.setAdapter(userInfoAdapter);
        userInfoAdapter.addArray(userInfo);
        userInfoAdapter.notifyDataSetChanged();
    }

    private void initData() {
        account = ITravelApplication.getiTravelApplication().getUser().getAccount();
        tvRefresh.setVisibility(View.GONE);
        hashMap.put("account", account);
        /**
         * 发送网络请求
         */
        OkhttpModel okhttpModel = new OkhttpModel();
        okhttpModel.doGet("getUserInfo.do", hashMap, new Callback() {

            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(UserInfoActivity.this, "获取用户信息失败", Toast.LENGTH_SHORT).show();
                tvRefresh.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(Object response, int id) {
                user = JsonAnalysisUtils.parseUserJson(response.toString());
                Log.i("UserInfo", user.toString());
                if (JsonAnalysisUtils.isSuccess) {
                    userInfo = new String[] {
                            user.getHead_portrait_url(), user.getAccount(),
                            user.getName(), user.getGender(), user.getSignature()
                    };
                    tvRefresh.setVisibility(View.GONE);
                    initRecylerView();
                }else {
                    Toast.makeText(UserInfoActivity.this, "获取用户信息失败", Toast.LENGTH_SHORT).show();
                    tvRefresh.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @OnClick(R.id.tv_refresh) public void refresh() {
        initData();
    }
}
