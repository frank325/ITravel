package lyf.com.example.itravel.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.callback.Callback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lyf.com.example.itravel.ITravelApplication;
import lyf.com.example.itravel.ITravelConstant;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.activity.MyClooectActivity;
import lyf.com.example.itravel.activity.MyShareActivity;
import lyf.com.example.itravel.activity.UserInfoActivity;
import lyf.com.example.itravel.adapter.HomePageAdapter;
import lyf.com.example.itravel.adapter.PersonAdapter;
import lyf.com.example.itravel.bean.User;
import lyf.com.example.itravel.model.OkhttpModel;
import lyf.com.example.itravel.utlis.JsonAnalysisUtils;
import lyf.com.example.itravel.view.CircleTransform;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 我Fragment
 */

public class PersonFragment extends Fragment {

    private PersonAdapter personAdapter;
    private User user;
    private HashMap<String, String> hashMap = new HashMap<>();
    private String account;
    private View view;
    private String[] info = new String[] {
            "我的分享", "我的收藏"
    };

    @BindView(R.id.tv_content) TextView tvContent;
    @BindView(R.id.rv_person) RecyclerView rvPerson;
    @BindView(R.id.tv_gender) TextView tvGender;
    @BindView(R.id.iv_head_portrait) ImageView ivHeadPortrait;
    @BindView(R.id.tv_name) TextView tvName;
    @BindView(R.id.iv_go) ImageView ivGo;
    @BindView(R.id.rl_go) RelativeLayout rlGo;
    @BindView(R.id.tv_refresh) TextView tvRefresh;
    @BindView(R.id.tv_share_num) TextView tvShareNum;
    @BindView(R.id.tv_clooect_num) TextView tvClooectNum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_person, container, false);

        ButterKnife.bind(this, view);
        initData();
        initRecyclerView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        account = ITravelApplication.getiTravelApplication().getUser().getAccount();
        ivGo.setEnabled(false);
        rlGo.setEnabled(false);
        tvRefresh.setVisibility(View.GONE);
        hashMap.put("account", account);
        /**
         * 发送网络请求
         */
        OkhttpModel okhttpModel = new OkhttpModel();
        okhttpModel.doGet("getUserInfo.do", hashMap, new Callback() {

            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getActivity(), "获取用户信息失败", Toast.LENGTH_SHORT).show();
                tvRefresh.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(Object response, int id) {
                user = JsonAnalysisUtils.parseUserJson(response.toString());
                if (JsonAnalysisUtils.isSuccess) {
                    ivGo.setEnabled(true);
                    rlGo.setEnabled(true);
                    if ("男".equals(user.getGender())) {
                        tvGender.setText("♂");
                        tvGender.setTextColor(Color.parseColor("#0099FF"));
                    }else if("女".equals(user.getGender())) {
                        tvGender.setText("♀");
                        tvGender.setTextColor(Color.parseColor("#FF6666"));
                    }
                    tvRefresh.setVisibility(View.GONE);
                    tvName.setText(user.getName());
                    tvShareNum.setText(user.getShare_num());
                    Toast.makeText(getContext(), user.getClooect_travel_notes(), Toast.LENGTH_LONG);
                    if(!"".equals(user.getClooect_travel_notes()) && user.getClooect_travel_notes() != "null") {
                        String[] clooects = user.getClooect_travel_notes().split(" ");
                        if(clooects.length > 0) {
                            tvClooectNum.setText(clooects.length + "");
                        }else {
                            tvClooectNum.setText("0");
                        }
                    }else {
                        tvClooectNum.setText("0");
                    }
                    if (user.getSignature().length() > 15) {
                        tvContent.setText(user.getSignature().substring(0, 15) + "...");
                    }else {
                        tvContent.setText(user.getSignature());
                    }
                    Picasso.with(getContext()).load(ITravelConstant.URL + user.getHead_portrait_url())
                            .resize(150, 150).transform(new CircleTransform())
                            .error(R.drawable.default_head).into(ivHeadPortrait);
                }else {
                    Toast.makeText(getActivity(), "获取用户信息失败", Toast.LENGTH_SHORT).show();
                    tvRefresh.setVisibility(View.VISIBLE);
                }

            }
        });
    }


    private void initRecyclerView() {
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvPerson.setLayoutManager(gridLayoutManager);
        personAdapter = new PersonAdapter(getContext());
        rvPerson.setAdapter(personAdapter);
        personAdapter.addArray(info);
        personAdapter.notifyDataSetChanged();
    }

    @OnClick ({R.id.iv_go, R.id.rl_go}) public void go() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), UserInfoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_refresh) public void refresh() {
        initData();
    }

    @OnClick (R.id.ll_share) public void goMyShare() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), MyShareActivity.class);
        startActivity(intent);
    }

    @OnClick (R.id.ll_clooect) public void goMyClooect() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), MyClooectActivity.class);
        startActivity(intent);
    }
}
