package lyf.com.example.itravel.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lyf.com.example.itravel.ITravelApplication;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.adapter.MyShareAdapter;
import lyf.com.example.itravel.bean.TravelNotes;
import lyf.com.example.itravel.model.OkhttpModel;
import lyf.com.example.itravel.utlis.JsonAnalysisUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 我的分享页面
 */

public class MyShareActivity extends AppCompatActivity {

    private HashMap<String, String> hashMap = new HashMap<>();
    private String account;
    private MyShareAdapter myShareAdapter;
    private List<TravelNotes> travelNotes = new ArrayList<>();

    @BindView(R.id.tb_my_share) Toolbar tbMyShare;
    @BindView(R.id.rv_my_share) RecyclerView rvMyShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_share);

        ButterKnife.bind(this);
        initToolbar();
        initRecyclerView();
        initData();
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        tbMyShare.setNavigationIcon(R.drawable.back);
        tbMyShare.setTitle("");

        setSupportActionBar(tbMyShare); //设置SupportActionBar为Toolbar

        tbMyShare.setNavigationOnClickListener(new View.OnClickListener() {
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
        rvMyShare.setLayoutManager(new LinearLayoutManager(MyShareActivity.this, LinearLayoutManager.VERTICAL, false));
        myShareAdapter = new MyShareAdapter(MyShareActivity.this);
        rvMyShare.setAdapter(myShareAdapter);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        account = ITravelApplication.getiTravelApplication().getUser().getAccount();
        hashMap.put("account", account);

        /**
         * 发送网络请求
         */
        OkhttpModel okhttpModel = new OkhttpModel();
        okhttpModel.doGet("getMyShareTravelNotesInfo.ao", hashMap, new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(MyShareActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response, int id) {
                if ("无结果".equals(response.toString())) {
                    Toast.makeText(MyShareActivity.this, "您还未分享", Toast.LENGTH_SHORT).show();
                } else {
                    travelNotes = JsonAnalysisUtils.parseAllTravelNotesJson(response.toString()); //解析Json数据
                    if (JsonAnalysisUtils.isSuccess) {
                        myShareAdapter.addList(travelNotes);
                        myShareAdapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(MyShareActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
