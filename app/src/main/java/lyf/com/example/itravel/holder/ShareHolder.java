package lyf.com.example.itravel.holder;

import android.content.Intent;
import android.graphics.Color;
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
import lyf.com.example.itravel.activity.PersonInfoActivity;
import lyf.com.example.itravel.activity.TravelNotesActivity;
import lyf.com.example.itravel.bean.TravelNotes;
import lyf.com.example.itravel.bean.User;
import lyf.com.example.itravel.model.OkhttpModel;
import lyf.com.example.itravel.utlis.JsonAnalysisUtils;
import lyf.com.example.itravel.view.CircleTransform;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 分享Holder
 */

public class ShareHolder extends RecyclerView.ViewHolder {

    private int num;
    private boolean isClooect = false;
    private User user;
    private TravelNotes travelNotes;
    private String account;
    private HashMap<String, String> hashMap = new HashMap<>();

    @BindView(R.id.tv_gender) TextView tvGender;
    @BindView(R.id.iv_user_head) ImageView ivUserHead;
    @BindView(R.id.tv_account) TextView tvAccount;
    @BindView(R.id.tv_time) TextView tvTime;
    @BindView(R.id.iv_travel_photo) ImageView ivPhoto;
    @BindView(R.id.tv_content) TextView tvContent;
    @BindView(R.id.tv_city) TextView tvCity;
    @BindView(R.id.tv_day_num) TextView tvDayNum;
    @BindView(R.id.tv_money) TextView tvMoney;
    @BindView(R.id.tv_clooect_num) TextView tvClooectNum;
    @BindView(R.id.iv_clooect) ImageView ivClooect;

    public ShareHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    /**
     * 绑定数据
     */
    public void bindHolder(TravelNotes travelNotes) {
        this.travelNotes = travelNotes;
        tvTime.setText(travelNotes.getAdd_time().substring(0,10));
        Picasso.with(itemView.getContext()).load(ITravelConstant.URL + travelNotes.getTravel_photo_url())
                .resize(600, 300).into(ivPhoto);
        if (travelNotes.getTravel_content().length() > 16) {
            tvContent.setText(travelNotes.getTravel_content().substring(0, 16) + "...");
        }else {
            tvContent.setText(travelNotes.getTravel_content());
        }
        tvCity.setText(travelNotes.getTravel_city());
        tvDayNum.setText(travelNotes.getTravel_day_num());
        tvMoney.setText(travelNotes.getTravel_money());
        tvClooectNum.setText(travelNotes.getClooect_num());
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        account = travelNotes.getTravel_account();

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
                Toast.makeText(itemView.getContext(), "获取信息失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response, int id) {
                user = JsonAnalysisUtils.parseUserJson(response.toString());
                if (JsonAnalysisUtils.isSuccess) {
                    Picasso.with(itemView.getContext()).load(ITravelConstant.URL + user.getHead_portrait_url())
                            .resize(150, 150).transform(new CircleTransform()).error(R.drawable.default_whith).into(ivUserHead);
                    tvAccount.setText(user.getName());

                    if ("男".equals(user.getGender())) {
                        tvGender.setText("♂");
                        tvGender.setTextColor(Color.parseColor("#0099FF"));
                    }else if("女".equals(user.getGender())) {
                        tvGender.setText("♀");
                        tvGender.setTextColor(Color.parseColor("#FF6666"));
                    }
                }else {
                    Toast.makeText(itemView.getContext(), "获取信息失败", Toast.LENGTH_SHORT).show();
                }

            }
        });

        account = ITravelApplication.getiTravelApplication().getUser().getAccount();

        hashMap.put("account", account);

        /**
         * 发送网络请求
         */
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
                if (JsonAnalysisUtils.isSuccess) {
                    if (user.getClooect_travel_notes() != null) {
                        String [] clooects = user.getClooect_travel_notes().split(" ");
                        for (String clooect: clooects) {
                            if (travelNotes.getId_travel_notes().equals(clooect)) {
                                ivClooect.setImageResource(R.drawable.clooect_light);
                                isClooect = true;
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
     * 响应点击事件，执行修改收藏数
     */
    @OnClick(R.id.iv_clooect) public void clooect() {
        if (isClooect) {
            ivClooect.setImageResource(R.drawable.clooect_nomal);
            num = Integer.parseInt(tvClooectNum.getText().toString()) - 1;
            tvClooectNum.setText(num + "");
            isClooect = false;
        }else {
            ivClooect.setImageResource(R.drawable.clooect_light);
            num = Integer.parseInt(tvClooectNum.getText().toString()) + 1;
            tvClooectNum.setText(num + "");
            isClooect = true;
        }

        hashMap.put("isClooect", isClooect + "");
        hashMap.put("clooect_num", "" + num);
        hashMap.put("id_travel_notes", travelNotes.getId_travel_notes());

        /**
         * 发送网络请求
         */
        OkhttpModel okhttpModel = new OkhttpModel();
        okhttpModel.doGet("updateClooectNum.ao", hashMap, new Callback() {

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
                    if (isClooect) {
                        Toast.makeText(itemView.getContext(), "已收藏", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(itemView.getContext(), "已取消收藏", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * 响应点击事件，跳转至游记信息页面
     */
    @OnClick(R.id.iv_go) public void go() {
        Intent intent = new Intent();
        intent.putExtra("id_travel_notes", travelNotes.getId_travel_notes());
        intent.setClass(itemView.getContext(), TravelNotesActivity.class);
        itemView.getContext().startActivity(intent);
    }

    /**
     * 响应点击事件，跳转至用户详细信息页面
     */
    @OnClick(R.id.iv_user_head) public void clickHead() {
        Intent intent = new Intent();
        intent.putExtra("travel_account", travelNotes.getTravel_account());
        intent.setClass(itemView.getContext(), PersonInfoActivity.class);
        itemView.getContext().startActivity(intent);
    }

}
