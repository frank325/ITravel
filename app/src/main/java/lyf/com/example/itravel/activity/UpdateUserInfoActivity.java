package lyf.com.example.itravel.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.callback.Callback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lyf.com.example.itravel.ITravelApplication;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.model.OkhttpModel;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 修改信息页面
 */

public class UpdateUserInfoActivity extends AppCompatActivity {

    private String account;
    private HashMap<String, String> hashMap = new HashMap<>();
    private String data, dataName;
    private String str;

    @BindView(R.id.tb_update_user_info) Toolbar tbUpdateUserInfo;
    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.et_data) EditText etData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        ButterKnife.bind(this);
        initData();
        initToolbar();
        initView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        data = intent.getStringExtra("data");
        dataName = intent.getStringExtra("dataName");

        account = ITravelApplication.getiTravelApplication().getUser().getAccount();

        if ("name".equals(dataName)) {
            str = "昵称";
            etData.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        }else {
            str = "个性签名";
            etData.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        }
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        tbUpdateUserInfo.setNavigationIcon(R.drawable.back);
        tbUpdateUserInfo.setTitle("");
        tvTitle.setText(str);

        etData.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        setSupportActionBar(tbUpdateUserInfo); //设置SupportActionBar为Toolbar

        tbUpdateUserInfo.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        etData.setText(data);
        etData.setHint(str);
        etData.setSelection(etData.getText().toString().length());
    }

    /**
     * 响应点击事件，执行修改用户信息
     */
    @OnClick(R.id.tv_save) public void saveUserInfo() {
        data = etData.getText().toString().trim();

        if(TextUtils.isEmpty(data)) {
            Toast.makeText(UpdateUserInfoActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        hashMap.put("data", data);
        hashMap.put("dataName", dataName);
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
                Toast.makeText(UpdateUserInfoActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response, int id) {
                Toast.makeText(UpdateUserInfoActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}
