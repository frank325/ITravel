package lyf.com.example.itravel.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.callback.Callback;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lyf.com.example.itravel.ITravelConstant;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.activity.CityIntroduceActivity;
import lyf.com.example.itravel.activity.CityTopActivity;
import lyf.com.example.itravel.adapter.HomePageAdapter;
import lyf.com.example.itravel.bean.City;
import lyf.com.example.itravel.model.OkhttpModel;
import lyf.com.example.itravel.utlis.JsonAnalysisUtils;
import lyf.com.example.itravel.view.ImageBarnnerFramLayout;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 首页Fragment
 */

public class HomePageFragment extends Fragment {

    private int resunmeNum = 0;
    private boolean isResume;
    private HomePageAdapter homePageAdapter;
    private List<City> citys;
    private View view;
    private String[] urls = new String[] {
            "images/xm.jpg", "images/yj.jpg",
            "images/bj.jpg", "images/zjj.jpg"
    };

    @BindView(R.id.ibfl_home_page) ImageBarnnerFramLayout ibflHomePage;
    @BindView(R.id.rv_home_page) RecyclerView rvHomePage;
    @BindView(R.id.pb_loading) ProgressBar pbLoading;
    @BindView(R.id.tv_loading) TextView tvLoading;
    @BindView(R.id.iv_refresh) ImageView ivRefresh;
    @BindView(R.id.ll_ibfl) LinearLayout llIBFL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_page, container, false);

        ButterKnife.bind(this, view);
        initViewGroup();
        initRecylerView();
        initData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ++resunmeNum;
        isResume = true;
        if (resunmeNum != 1) {
            initRecylerView();
            initData();
        }
    }

    /**
     * 初始化图片轮播ViewGroup
     */
    private void initViewGroup() {
        /**
         * 获取屏幕宽度
         */
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        ITravelConstant.WITTH = dm.widthPixels;

        llIBFL.setEnabled(false);

        ibflHomePage.addBitmaps(urls);
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecylerView() {
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvHomePage.setLayoutManager(gridLayoutManager);
        homePageAdapter = new HomePageAdapter(getContext());
        rvHomePage.setAdapter(homePageAdapter);
        if (!isResume) {
            rvHomePage.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
                    int spanSize = layoutParams.getSpanSize();
                    int spanIndex = layoutParams.getSpanIndex();
                    outRect.top = 20;
                    if (spanSize != gridLayoutManager.getSpanCount()) {
                        if (spanIndex == 1) {
                            outRect.left = 10;
                            outRect.right = 20;
                        }else {
                            outRect.right = 10;
                            outRect.left = 20;
                        }
                    }
                }
            });
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        llIBFL.setEnabled(false);
        tvLoading.setText("加载中···");
        tvLoading.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
        ivRefresh.setVisibility(View.GONE);

        /**
         * 发送网络请求
         */
        OkhttpModel okhttpModel = new OkhttpModel();
        okhttpModel.doGet("getAllCityInfo.to", new HashMap<String, String>(), new Callback() {

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
                citys = JsonAnalysisUtils.parseAllCityJson(response.toString());
                if (JsonAnalysisUtils.isSuccess) {
                    pbLoading.setVisibility(View.GONE);
                    tvLoading.setVisibility(View.GONE);
                    homePageAdapter.addList(citys);
                    homePageAdapter.notifyDataSetChanged();
                    llIBFL.setEnabled(true);
                }else {
                    Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                    pbLoading.setVisibility(View.INVISIBLE);
                    ivRefresh.setVisibility(View.VISIBLE);
                    tvLoading.setText("点击刷新");
                }
            }
        });
    }

    /**
     * 响应点击事件，刷新页面
     */
    @OnClick (R.id.iv_refresh) public void refresh() {
        initData();
    }

    /**
     * 响应点击事件。跳转至城市信息页面
     */
    @OnClick (R.id.ll_ibfl) public void vgClick() {
        Intent intent = new Intent();
        intent.putExtra("city_name", citys.get(ibflHomePage.index).getCity_name());
        intent.setClass(getActivity(), CityIntroduceActivity.class);
        startActivity(intent);
    }

    /**
     * 响应点击事件，跳转至热门城市页面
     */
    @OnClick (R.id.iv_top) public void top() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), CityTopActivity.class);
        startActivity(intent);
    }

}
