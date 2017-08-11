package lyf.com.example.itravel.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.adapter.SettingAdapter;
import lyf.com.example.itravel.adapter.UserInfoAdapter;

public class SettingActivity extends AppCompatActivity {

    private SettingAdapter settingAdapter;
    private String[] info = new String[]{
            "个人资料", "账户安全", "退出登录"
    };

    @BindView(R.id.tb_setting) Toolbar tbSetting;
    @BindView(R.id.rv_setting) RecyclerView rvSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ButterKnife.bind(this); //绑定依赖注入ButterKnif
        initToolbar();
        initRecyclerView();
    }

    private void initToolbar() {
        tbSetting.setNavigationIcon(R.drawable.back);
        tbSetting.setTitle("");

        setSupportActionBar(tbSetting); //设置SupportActionBar为Toolbar

        tbSetting.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initRecyclerView() {
        rvSetting.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        settingAdapter = new SettingAdapter(this);
        rvSetting.setAdapter(settingAdapter);
        settingAdapter.addArray(info);
        settingAdapter.notifyDataSetChanged();
    }
}
