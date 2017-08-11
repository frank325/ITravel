package lyf.com.example.itravel.activity;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.adapter.SearchAdapter;
import lyf.com.example.itravel.adapter.TopAdapter;
import lyf.com.example.itravel.bean.City;
import lyf.com.example.itravel.model.OkhttpModel;
import lyf.com.example.itravel.utlis.JsonAnalysisUtils;
import okhttp3.Call;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    private HashMap<String, String> hashMap = new HashMap<>();
    private String data;
    private SearchAdapter searchAdapter;
    private List<City> citys = new ArrayList<>();
    private int initNum = 0;

    @BindView(R.id.rv_search) RecyclerView rvSearch;
    @BindView(R.id.tv_result) TextView tvResult;
    @BindView(R.id.tb_search) Toolbar tbSearch;
    @BindView(R.id.et_search) EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this); //绑定依赖注入ButterKnife
        initToolbar();
    }

    private void initRecylerView() {
        ++initNum;
        rvSearch.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        searchAdapter = new SearchAdapter(this);
        rvSearch.setAdapter(searchAdapter);
        if (initNum == 1) {
            rvSearch.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.top = 20;
                    outRect.left = 30;
                    outRect.right = 30;
                }
            });
        }
    }

    private void initToolbar() {
        tbSearch.setNavigationIcon(R.drawable.back);
        tbSearch.setTitle("");

        setSupportActionBar(tbSearch); //设置SupportActionBar为Toolbar

        etSearch.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        tbSearch.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @OnClick(R.id.tv_search) public void search() {
        tvResult.setText("");
        initRecylerView();
        data = etSearch.getText().toString().trim();

        if(TextUtils.isEmpty(data)) {
            Toast.makeText(SearchActivity.this, "请输入关键字搜索", Toast.LENGTH_SHORT).show();
            return;
        }

        hashMap.put("data", data);
        /**
         * 发送网络请求
         */
        OkhttpModel okhttpModel = new OkhttpModel();
        okhttpModel.doGet("getForListWithCityInfo.to", hashMap, new Callback() {

            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(SearchActivity.this, "搜索失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response, int id) {
                if ("无结果".equals(response.toString())) {
                    tvResult.setText(response.toString());
                }else {
                    citys = JsonAnalysisUtils.parseAllCityJson(response.toString());
                    if (JsonAnalysisUtils.isSuccess) {
                        searchAdapter.addList(citys);
                        searchAdapter.notifyDataSetChanged();
                        tvResult.setText("搜索成功，共有" + citys.size() + "条结果");
                    }else {
                        Toast.makeText(SearchActivity.this, "搜索失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
