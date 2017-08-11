package lyf.com.example.itravel.holder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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
import lyf.com.example.itravel.activity.LoginActivity;
import lyf.com.example.itravel.activity.MyShareActivity;
import lyf.com.example.itravel.activity.PersonInfoActivity;
import lyf.com.example.itravel.activity.TravelNotesActivity;
import lyf.com.example.itravel.bean.TravelNotes;
import lyf.com.example.itravel.bean.User;
import lyf.com.example.itravel.model.OkhttpModel;
import lyf.com.example.itravel.utlis.JsonAnalysisUtils;
import lyf.com.example.itravel.utlis.SPUtils;
import lyf.com.example.itravel.view.CircleTransform;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/8.
 */

public class MyShareHolder extends RecyclerView.ViewHolder {

    private int position;
    private TravelNotes travelNotes;
    private String account;
    private HashMap<String, String> hashMap = new HashMap<>();

    @BindView(R.id.iv_travel_photo) ImageView ivPhoto;
    @BindView(R.id.tv_content) TextView tvContent;
    @BindView(R.id.tv_city) TextView tvCity;
    @BindView(R.id.tv_day_num) TextView tvDayNum;
    @BindView(R.id.tv_money) TextView tvMoney;
    @BindView(R.id.tv_clooect_num) TextView tvClooectNum;
    @BindView(R.id.tv_delete) public TextView tvDelete;

    public MyShareHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bindHolder(TravelNotes travelNotes, int position) {
        this.position = position;
        this.travelNotes = travelNotes;
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
    }

    @OnClick(R.id.iv_go) public void go() {
        Intent intent = new Intent();
        intent.putExtra("id_travel_notes", travelNotes.getId_travel_notes());
        intent.setClass(itemView.getContext(), TravelNotesActivity.class);
        itemView.getContext().startActivity(intent);
    }

}
