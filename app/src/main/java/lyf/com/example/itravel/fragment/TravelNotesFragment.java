package lyf.com.example.itravel.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.callback.Callback;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.adapter.TravelNotesAdapter;
import lyf.com.example.itravel.bean.TravelNotes;
import lyf.com.example.itravel.model.OkhttpModel;
import lyf.com.example.itravel.utlis.JsonAnalysisUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 游记Fragment
 */

public class TravelNotesFragment extends Fragment{

    private TravelNotesAdapter travelNotesAdapter;
    private View view;
    private String city_name;
    private List<TravelNotes> travelNotes;
    private HashMap<String, String> hashMap = new HashMap<>();

    @BindView(R.id.rv_travel_notes) RecyclerView rvTravelNotes;
    @BindView(R.id.tv_result) TextView tvResult;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_travel_notes, container, false);

        ButterKnife.bind(this, view);
        initRecylerView();
        initData();
        return view;
    }

    /**
     * 初始化RecylerView
     */
    private void initRecylerView() {
        rvTravelNotes.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        travelNotesAdapter = new TravelNotesAdapter(getContext());
        rvTravelNotes.setAdapter(travelNotesAdapter);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Bundle bundle = getArguments();
        city_name = bundle.getString("scenic_city_name");

        tvResult.setText("正在获取信息");

        hashMap.put("city_name", city_name);

        /**
         * 发送网络请求
         */
        OkhttpModel okhttpModel = new OkhttpModel();
        okhttpModel.doGet("getCityTravelNotesInfo.ao", hashMap, new Callback() {

            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getActivity(), "获取信息失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response, int id) {
                if ("无结果".equals(response.toString())) {
                    tvResult.setText("无");
                } else {
                    travelNotes = JsonAnalysisUtils.parseAllTravelNotesJson(response.toString());
                    if (JsonAnalysisUtils.isSuccess) {
                        tvResult.setText("共有" + travelNotes.size() + "条记录");
                        travelNotesAdapter.addList(travelNotes);
                        travelNotesAdapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(getActivity(), "获取信息失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
