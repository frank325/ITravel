package lyf.com.example.itravel.activity;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.callback.Callback;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.adapter.TopAdapter;
import lyf.com.example.itravel.bean.City;
import lyf.com.example.itravel.model.OkhttpModel;
import lyf.com.example.itravel.utlis.JsonAnalysisUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 热门城市页面
 */

public class CityTopActivity extends AppCompatActivity {

    private List<City> citys;
    private TopAdapter topAdapter;

    @BindView(R.id.tb_top) Toolbar tbTop;
    @BindView(R.id.rv_top) RecyclerView rvTop;
    @BindView(R.id.pb_loading) ProgressBar pbLoading;
    @BindView(R.id.tv_loading) TextView tvLoading;
    @BindView(R.id.iv_refresh) ImageView ivRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_top);

        ButterKnife.bind(this); //绑定依赖注入ButterKnife
        initToolbar();
        initRecylerView();
        initData();
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        tbTop.setNavigationIcon(R.drawable.back);
        tbTop.setTitle("");

        setSupportActionBar(tbTop); //设置SupportActionBar为Toolbar

        tbTop.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 初始化RecylerView
     */
    private void initRecylerView() {
        rvTop.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        topAdapter = new TopAdapter(this);
        rvTop.setAdapter(topAdapter);
        rvTop.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) { //设置Item间距
                outRect.top = 20;
                outRect.left = 30;
                outRect.right = 30;
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        tvLoading.setText("加载中···");
        tvLoading.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
        ivRefresh.setVisibility(View.GONE);

        /**
         * 发送网络请求
         */
        OkhttpModel okhttpModel = new OkhttpModel();
        okhttpModel.doGet("getCityTopInfo.to", new HashMap<String, String>(), new Callback() {

            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(CityTopActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                pbLoading.setVisibility(View.INVISIBLE);
                ivRefresh.setVisibility(View.VISIBLE);
                tvLoading.setText("点击刷新");
            }

            @Override
            public void onResponse(Object response, int id) {
                citys = JsonAnalysisUtils.parseAllCityJson(response.toString()); //解析Json数据
                if (JsonAnalysisUtils.isSuccess) {
                    pbLoading.setVisibility(View.GONE);
                    tvLoading.setVisibility(View.GONE);
                    topAdapter.addList(citys);
                    topAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(CityTopActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    pbLoading.setVisibility(View.INVISIBLE);
                    ivRefresh.setVisibility(View.VISIBLE);
                    tvLoading.setText("点击刷新");
                }
            }
        });
    }

    /**
     * 监听点击事件，刷新数据
     */
    @OnClick(R.id.iv_refresh) public void refresh() {
        initData();
    }

}
