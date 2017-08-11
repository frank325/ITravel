package lyf.com.example.itravel.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.adapter.UserSafeAdapter;

/**
 * 账户安全页面
 */

public class UserSafeActivity extends AppCompatActivity {

    private UserSafeAdapter userSafeAdapter;
    private String[] info = new String[]{
            "修改密码"
    };

    @BindView(R.id.tb_user_safe) Toolbar tbUserSafe;
    @BindView(R.id.rv_user_safe) RecyclerView rvUserSafe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_safe);

        ButterKnife.bind(this); //绑定依赖注入ButterKnif
        initToolbar();
        initRecyclerView();
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        tbUserSafe.setNavigationIcon(R.drawable.back);
        tbUserSafe.setTitle("");

        setSupportActionBar(tbUserSafe); //设置SupportActionBar为Toolbar

        tbUserSafe.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 初始化initRecyclerView
     */
    private void initRecyclerView() {
        rvUserSafe.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        userSafeAdapter = new UserSafeAdapter(this);
        rvUserSafe.setAdapter(userSafeAdapter);
        userSafeAdapter.addArray(info);
        userSafeAdapter.notifyDataSetChanged();
    }

}
