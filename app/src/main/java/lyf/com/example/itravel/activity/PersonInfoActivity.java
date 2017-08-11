package lyf.com.example.itravel.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.callback.Callback;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lyf.com.example.itravel.ITravelApplication;
import lyf.com.example.itravel.ITravelConstant;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.adapter.HomePageAdapter;
import lyf.com.example.itravel.adapter.MyShareAdapter;
import lyf.com.example.itravel.adapter.PersonInfoAdapter;
import lyf.com.example.itravel.bean.TravelNotes;
import lyf.com.example.itravel.bean.User;
import lyf.com.example.itravel.model.OkhttpModel;
import lyf.com.example.itravel.utlis.JsonAnalysisUtils;
import lyf.com.example.itravel.view.CircleTransform;
import okhttp3.Call;
import okhttp3.Response;

public class PersonInfoActivity extends AppCompatActivity {

    private PersonInfoAdapter personInfoAdapter;
    private List<TravelNotes> travelNotes = new ArrayList<>();
    private User user;
    private HashMap<String, String> hashMap = new HashMap<>();
    private String account;

    @BindView(R.id.rv_person_info) RecyclerView rvPersonInfo;
    @BindView(R.id.tb_person_info) Toolbar tbPersonInfo;
    @BindView(R.id.tv_gender) TextView tvGender;
    @BindView(R.id.iv_person_head) ImageView ivPersonHead;
    @BindView(R.id.tv_person_name) TextView tvPersonName;
    @BindView(R.id.tv_person_signature) TextView tvPersonSignature;
    @BindView(R.id.tv_share) TextView tvShare;
    @BindView(R.id.tv_result) TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        ButterKnife.bind(this);
        initToolbar();
        initRecyclerView();
        initData();
    }

    private void initRecyclerView() {
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(PersonInfoActivity.this, 2);
        rvPersonInfo.setLayoutManager(gridLayoutManager);
        personInfoAdapter = new PersonInfoAdapter(PersonInfoActivity.this);
        rvPersonInfo.setAdapter(personInfoAdapter);
        rvPersonInfo.addItemDecoration(new RecyclerView.ItemDecoration() {
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

    private void initToolbar() {
        tbPersonInfo.setNavigationIcon(R.drawable.back);
        tbPersonInfo.setTitle("");

        setSupportActionBar(tbPersonInfo); //设置SupportActionBar为Toolbar

        tbPersonInfo.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        account = intent.getStringExtra("travel_account");

        hashMap.put("account", account);
        getPersonInfo();
    }

    public void getPersonInfo() {
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
                Toast.makeText(PersonInfoActivity.this, "获取用户信息失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response, int id) {
                user = JsonAnalysisUtils.parseUserJson(response.toString());
                if (JsonAnalysisUtils.isSuccess) {
                    if ("男".equals(user.getGender())) {
                        tvShare.setText("他的分享");
                        tvGender.setText("♂");
                        tvGender.setTextColor(Color.parseColor("#0099FF"));
                    }else if("女".equals(user.getGender())) {
                        tvShare.setText("她的分享");
                        tvGender.setText("♀");
                        tvGender.setTextColor(Color.parseColor("#FF6666"));
                    }
                    tvPersonName.setText(user.getName());
                    Picasso.with(PersonInfoActivity.this).load(ITravelConstant.URL + user.getHead_portrait_url())
                            .resize(150, 150).transform(new CircleTransform())
                            .error(R.drawable.default_head).into(ivPersonHead);
                    if (user.getSignature().length() > 18) {
                        tvPersonSignature.setText(user.getSignature().substring(0,18) + "...");
                    }else {
                        tvPersonSignature.setText(user.getSignature());
                    }
                    getPersonTravelNotesInfo();
                }
            }
        });
    }

    public void getPersonTravelNotesInfo() {
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
                Toast.makeText(PersonInfoActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response, int id) {
                if ("无结果".equals(response.toString())) {
                    tvResult.setText("该用户无分享");
                } else {
                    travelNotes = JsonAnalysisUtils.parseAllTravelNotesJson(response.toString());
                    if (JsonAnalysisUtils.isSuccess) {
                        tvResult.setText("共有" + travelNotes.size() + "条分享");
                        personInfoAdapter.addList(travelNotes);
                        personInfoAdapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(PersonInfoActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
