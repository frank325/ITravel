package lyf.com.example.itravel.activity;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.adapter.SettingAdapter;

/**
 * 设置页面
 */

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

    /**
     * 初始化Toolbar
     */
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

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        rvSetting.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        settingAdapter = new SettingAdapter(this);
        rvSetting.setAdapter(settingAdapter);

        rvSetting.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int pos = parent.getChildAdapterPosition(view);
                if (pos == 1) {
                    outRect.top = 2;
                }
                if (pos == 2) {
                    outRect.top= 60;
                }
            }
        });

        settingAdapter.addArray(info);
        settingAdapter.notifyDataSetChanged();
    }

}
