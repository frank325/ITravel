package lyf.com.example.itravel.holder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.callback.Callback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lyf.com.example.itravel.ITravelApplication;
import lyf.com.example.itravel.ITravelConstant;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.activity.CityIntroduceActivity;
import lyf.com.example.itravel.bean.City;
import lyf.com.example.itravel.bean.User;
import lyf.com.example.itravel.model.OkhttpModel;
import lyf.com.example.itravel.utlis.JsonAnalysisUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 首页Holder
 */

public class HomePageHolder extends RecyclerView.ViewHolder {

    private String account;
    private City city;
    private User user;
    private boolean isGood = false;
    private int num;
    private HashMap<String, String> hashMap = new HashMap<>();

    @BindView(R.id.iv_city_photo) ImageView ivCityPhoto;
    @BindView(R.id.tv_city_name) TextView tvCityName;
    @BindView(R.id.tv_city_address) TextView tvCityAddress;
    @BindView(R.id.tv_think_go_num) TextView tvThinkGoNum;
    @BindView(R.id.iv_good) ImageView ivGood;

    public HomePageHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
        initDate();
    }

    /**
     * 初始化数据
     */
    private void initDate() {
        account = ITravelApplication.getiTravelApplication().getUser().getAccount();

        hashMap.put("account", account);

        OkhttpModel okhttpModel = new OkhttpModel();
        okhttpModel.doGet("getUserInfo.do", hashMap, new Callback() {

            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(itemView.getContext(), "获取用户信息失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response, int id) {
                user = JsonAnalysisUtils.parseUserJson(response.toString());
                if(JsonAnalysisUtils.isSuccess) {
                    if (user.getThink_go_city() != null) {
                        String [] think_go_citys = user.getThink_go_city().split(" ");
                        for (String think_go: think_go_citys) {
                            if (city.getCity_name().equals(think_go)) {
                                ivGood.setImageResource(R.drawable.good);
                                isGood = true;
                            }
                        }
                    }
                }else {
                    Toast.makeText(itemView.getContext(), "获取用户信息失败", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     * 绑定数据
     */
    public void bindHolder(City city) {
        this.city = city;
        Picasso.with(itemView.getContext()).load(ITravelConstant.URL + city.getCity_photo_url())
                .resize(160, 110).into(ivCityPhoto);

        tvCityName.setText(city.getCity_name());
        tvCityAddress.setText(city.getCity_address());
        tvThinkGoNum.setText(city.getThink_go_num());
    }

    /**
     * 响应点击事件，执行修改点赞数
     */
    @OnClick(R.id.iv_good) public void good() {
        if (isGood) {
            ivGood.setImageResource(R.drawable.good_black);
            num = Integer.parseInt(tvThinkGoNum.getText().toString()) - 1;
            tvThinkGoNum.setText(num + "");
            isGood = false;
        }else {
            ivGood.setImageResource(R.drawable.good);
            num = Integer.parseInt(tvThinkGoNum.getText().toString()) + 1;
            tvThinkGoNum.setText(num + "");
            isGood = true;
        }

        hashMap.put("isGood", isGood + "");
        hashMap.put("think_go_num", "" + num);
        hashMap.put("city_name", city.getCity_name());

        /**
         * 发送网络请求
         */
        OkhttpModel okhttpModel = new OkhttpModel();
        okhttpModel.doGet("updateThinkGoNum.to", hashMap, new Callback() {

            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(itemView.getContext(), "错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response, int id) {
                if("Success".equals(response.toString())) {
                    if (isGood) {
                        Toast.makeText(itemView.getContext(), "赞", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(itemView.getContext(), "取消", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * 响应点击事件，跳转至城市信息页面
     */
    @OnClick (R.id.iv_city_photo) public void clickPhoto() {
        Intent intent = new Intent();
        intent.putExtra("city_name", city.getCity_name());
        intent.setClass(itemView.getContext(), CityIntroduceActivity.class);
        itemView.getContext().startActivity(intent);
    }

}
