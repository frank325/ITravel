package lyf.com.example.itravel.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import lyf.com.example.itravel.adapter.ScenicAdapter;
import lyf.com.example.itravel.bean.Scenic;
import lyf.com.example.itravel.model.OkhttpModel;
import lyf.com.example.itravel.utlis.JsonAnalysisUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/5.
 */

public class ScenicFragment extends Fragment {

    private String scenic_city_name;
    private HashMap<String, String> hashMap = new HashMap<>();
    private List<Scenic> scenics;
    private ScenicAdapter scenicAdapter;
    private View view;

    @BindView(R.id.rv_scenic) RecyclerView rvScenic;
    @BindView(R.id.pb_loading) ProgressBar pbLoading;
    @BindView(R.id.tv_loading) TextView tvLoading;
    @BindView(R.id.iv_refresh) ImageView ivRefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scenic, container, false);

        ButterKnife.bind(this, view);
        initRecylerView();
        initData();
        return view;
    }

    private void initRecylerView() {
        rvScenic.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        scenicAdapter = new ScenicAdapter(getContext());
        rvScenic.setAdapter(scenicAdapter);
    }

    private void initData() {
        Bundle bundle = getArguments();
        scenic_city_name = bundle.getString("scenic_city_name");
        hashMap.put("scenic_city_name", scenic_city_name);

        tvLoading.setText("加载中···");
        tvLoading.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
        ivRefresh.setVisibility(View.GONE);

        /**
         * 发送网络请求
         */
        OkhttpModel okhttpModel = new OkhttpModel();
        okhttpModel.doGet("getCityScenicInfo.go", hashMap, new Callback() {

            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                pbLoading.setVisibility(View.INVISIBLE);
                ivRefresh.setVisibility(View.VISIBLE);
                tvLoading.setText("点击刷新");
            }

            @Override
            public void onResponse(Object response, int id) {
                scenics = JsonAnalysisUtils.parseCityScenicJson(response.toString());
                if (JsonAnalysisUtils.isSuccess) {
                    pbLoading.setVisibility(View.GONE);
                    tvLoading.setVisibility(View.GONE);
                    scenicAdapter.addList(scenics);
                    scenicAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                    pbLoading.setVisibility(View.INVISIBLE);
                    ivRefresh.setVisibility(View.VISIBLE);
                    tvLoading.setText("点击刷新");
                }
            }
        });
    }

    @OnClick(R.id.iv_refresh) public void refresh() {
        initData();
    }

}
