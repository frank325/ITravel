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
import lyf.com.example.itravel.adapter.MyClooectAdapter;
import lyf.com.example.itravel.bean.TravelNotes;
import lyf.com.example.itravel.model.OkhttpModel;
import lyf.com.example.itravel.utlis.JsonAnalysisUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 我的收藏页面
 */

public class MyClooectActivity extends AppCompatActivity {

    private MyClooectAdapter myClooectAdapter;
    private HashMap<String, String> hashMap = new HashMap<>();
    private String account;
    private List<TravelNotes> travelNotes = new ArrayList<>();

    @BindView(R.id.tb_my_clooect) Toolbar tbMyClooect;
    @BindView(R.id.rv_my_cloect) RecyclerView rvMyCloect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_clooect);

        ButterKnife.bind(this);
        initToolbar();
        initRecyclerView();
        initData();
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        tbMyClooect.setNavigationIcon(R.drawable.back);
        tbMyClooect.setTitle("");

        setSupportActionBar(tbMyClooect); //设置SupportActionBar为Toolbar

        tbMyClooect.setNavigationOnClickListener(new View.OnClickListener() {
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
        rvMyCloect.setLayoutManager(new LinearLayoutManager(MyClooectActivity.this, LinearLayoutManager.VERTICAL, false));
        myClooectAdapter = new MyClooectAdapter(MyClooectActivity.this);
        rvMyCloect.setAdapter(myClooectAdapter);
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
        okhttpModel.doGet("getMyClooectTravelNotesInfo.ao", hashMap, new Callback() {

            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(MyClooectActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response, int id) {
                if ("无结果".equals(response.toString())) {
                    Toast.makeText(MyClooectActivity.this, "您没有收藏", Toast.LENGTH_SHORT).show();
                } else {
                    travelNotes = JsonAnalysisUtils.parseAllTravelNotesJson(response.toString()); //Json数据解析
                    if (JsonAnalysisUtils.isSuccess) {
                        myClooectAdapter.addList(travelNotes);
                        myClooectAdapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(MyClooectActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
