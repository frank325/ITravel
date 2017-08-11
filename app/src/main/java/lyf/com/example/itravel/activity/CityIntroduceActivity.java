package lyf.com.example.itravel.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lyf.com.example.itravel.ITravelApplication;
import lyf.com.example.itravel.ITravelConstant;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.bean.City;
import lyf.com.example.itravel.bean.User;
import lyf.com.example.itravel.fragment.TravelNotesFragment;
import lyf.com.example.itravel.fragment.ScenicFragment;
import lyf.com.example.itravel.model.OkhttpModel;
import lyf.com.example.itravel.utlis.JsonAnalysisUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 城市信息页面
 */

public class CityIntroduceActivity extends AppCompatActivity {

    private static final int SCENIC = 1;
    private static final int TRAVEL_NOTES = 2;
    private User user;
    private City city;
    private int num;
    private String account, city_name;
    private Fragment fgTravelNotes, fgScenic;
    private boolean isGood;
    private HashMap<String, String> hashMap = new HashMap<>();

    @BindView(R.id.tb_city_introduce) Toolbar tbCityIntroduce;
    @BindView(R.id.iv_city_photo) ImageView ivCityPhoto;
    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.tv_city_address) TextView tvCityAddress;
    @BindView(R.id.tv_think_go_num) TextView tvThinkGoNum;
    @BindView(R.id.iv_good) ImageView ivGood;
    @BindView(R.id.tv_travel_notes) TextView tvTravelNotes;
    @BindView(R.id.tv_scenic) TextView tvScenic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_introduce);

        ButterKnife.bind(this);
        initToolbar();
        initData();
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        tbCityIntroduce.setNavigationIcon(R.drawable.back);
        tbCityIntroduce.setTitle("");

        setSupportActionBar(tbCityIntroduce); //设置SupportActionBar为Toolbar

        tbCityIntroduce.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        city_name = intent.getStringExtra("city_name");
        account = ITravelApplication.getiTravelApplication().getUser().getAccount();

        hashMap.put("city_name", city_name);
        hashMap.put("account", account);

        setTabe(SCENIC);
        getCityInfo();
    }

    /**
     * 获取城市信息
     */
    public void getCityInfo() {
        /**
         * 发送网络请求
         */
        OkhttpModel okhttpModel = new OkhttpModel();
        okhttpModel.doGet("getCityInfo.to", hashMap, new Callback() {

            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(CityIntroduceActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response, int id) {
                city = JsonAnalysisUtils.parseCityJson(response.toString());
                Picasso.with(CityIntroduceActivity.this).load(ITravelConstant.URL + city.getCity_photo_url())
                        .resize(ITravelConstant.WITTH, 500).into(ivCityPhoto);
                tvTitle.setText(city.getCity_name());
                tvCityAddress.setText(city.getCity_address());
                tvThinkGoNum.setText(city.getThink_go_num());
                getUserInfo();
            }
        });
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo() {
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
                Toast.makeText(CityIntroduceActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response, int id) {
                user = JsonAnalysisUtils.parseUserJson(response.toString());
                if (user.getThink_go_city() != null) {
                    String [] think_go_citys = user.getThink_go_city().split(" ");
                    for (String think_go: think_go_citys) {
                        if (city.getCity_name().equals(think_go)) {
                            ivGood.setImageResource(R.drawable.good_gray);
                            isGood = true;
                        }
                    }
                }
            }
        });
    }

    /**
     * 监听点击事件，执行修改点赞数
     */
    @OnClick(R.id.iv_good) public void good() {
        if (isGood) {
            ivGood.setImageResource(R.drawable.good_black);
            num = Integer.parseInt(tvThinkGoNum.getText().toString()) - 1;
            tvThinkGoNum.setText(num + "");
            isGood = false;
        }else {
            ivGood.setImageResource(R.drawable.good_gray);
            num = Integer.parseInt(tvThinkGoNum.getText().toString()) + 1;
            tvThinkGoNum.setText(num + "");
            isGood = true;
        }

        hashMap.put("isGood", isGood + "");
        hashMap.put("think_go_num", "" + num);
        hashMap.put("city_name", city.getCity_name());

        OkhttpModel okhttpModel = new OkhttpModel();
        okhttpModel.doGet("updateThinkGoNum.to", hashMap, new Callback() {

            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(CityIntroduceActivity.this, "错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response, int id) {
                if("Success".equals(response.toString())) {
                    if (isGood) {
                        Toast.makeText(CityIntroduceActivity.this, "赞", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(CityIntroduceActivity.this, "取消", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(CityIntroduceActivity.this, "错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 设置要显示的Fragment
     * @param tabe 要显示页面的值
     */
    private void setTabe(int tabe) {
        resetText();
        Bundle bundle = new Bundle();
        bundle.putString("scenic_city_name", city_name);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction); //隐藏fragment

        // 设置内容区域
        switch (tabe)
        {
            case SCENIC:
                if (fgScenic == null) //没有被创建过
                {
                    fgScenic = new ScenicFragment();
                    fgScenic.setArguments(bundle); //传值
                    transaction.add(R.id.fl_city_introduce, fgScenic); //设置页面
                } else {
                    transaction.show(fgScenic); //直接显示页面
                }
                tvScenic.setTextColor(Color.parseColor("#86D2C6")); //把文字设置为亮的
                break;
            case TRAVEL_NOTES:
                if (fgTravelNotes == null) //没有被创建过
                {
                    fgTravelNotes = new TravelNotesFragment();
                    fgTravelNotes.setArguments(bundle); //传值
                    transaction.add(R.id.fl_city_introduce, fgTravelNotes); //设置页面
                } else {
                    transaction.show(fgTravelNotes); //直接显示页面
                }
                tvTravelNotes.setTextColor(Color.parseColor("#86D2C6")); //把文字设置为亮的
                break;
            default:
                break;
        }
        transaction.commit(); //提交
    }

    /**
     * 将字体颜色设置为灰色
     */
    private void resetText() {
        tvTravelNotes.setTextColor(Color.parseColor("#999999"));
        tvScenic.setTextColor(Color.parseColor("#999999"));
    }

    /**
     * 隐藏fragment
     */
    private void hideFragment(FragmentTransaction transaction)
    {
        if (fgTravelNotes != null)
        {
            transaction.hide(fgTravelNotes);
        }
        if (fgScenic != null)
        {
            transaction.hide(fgScenic);
        }
    }


    /**
     * 监听点击事件，显示景点Fragment
     */
    @OnClick (R.id.tv_scenic) public void scenic() {
        setTabe(SCENIC);
    }

    /**
     * 监听点击事件，显示游记Fragment
     */
    @OnClick (R.id.tv_travel_notes) public void travelNotes() {
        setTabe(TRAVEL_NOTES);
    }

}
