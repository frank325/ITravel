package lyf.com.example.itravel.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import lyf.com.example.itravel.R;

/**
 * 找回密码页面
 */

public class RetrievePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_password);

        ButterKnife.bind(this); //绑定依赖注入ButterKnife
    }

    /**
     * 响应点击事件，返回
     */
    @OnClick(R.id.iv_back) public void back() {
        finish();
    }
}
