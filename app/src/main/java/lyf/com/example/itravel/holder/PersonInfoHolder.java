package lyf.com.example.itravel.holder;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import lyf.com.example.itravel.ITravelConstant;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.activity.MyShareActivity;
import lyf.com.example.itravel.activity.TravelNotesActivity;
import lyf.com.example.itravel.bean.TravelNotes;
import lyf.com.example.itravel.model.OkhttpModel;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/8.
 */

public class PersonInfoHolder extends RecyclerView.ViewHolder {

    private int position;
    private TravelNotes travelNotes;
    private String account;
    private HashMap<String, String> hashMap = new HashMap<>();

    @BindView(R.id.iv_photo) ImageView ivPhoto;
    @BindView(R.id.tv_content) TextView tvContent;
    @BindView(R.id.tv_city) TextView tvCity;
    @BindView(R.id.tv_day_num) TextView tvDayNum;
    @BindView(R.id.tv_money) TextView tvMoney;
    @BindView(R.id.tv_clooect_num) TextView tvClooectNum;

    public PersonInfoHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bindHolder(TravelNotes travelNotes, int position) {
        this.position = position;
        this.travelNotes = travelNotes;
        Picasso.with(itemView.getContext()).load(ITravelConstant.URL + travelNotes.getTravel_photo_url())
                .resize(160, 110).into(ivPhoto);
        if (travelNotes.getTravel_content().length() > 8) {
            tvContent.setText(travelNotes.getTravel_content().substring(0, 8) + "...");
        }else {
            tvContent.setText(travelNotes.getTravel_content());
        }
        tvCity.setText(travelNotes.getTravel_city());
        tvDayNum.setText(travelNotes.getTravel_day_num() + "天");
        tvMoney.setText(travelNotes.getTravel_money() + "￥");
        tvClooectNum.setText(travelNotes.getClooect_num() + "收藏");
    }

    @OnClick(R.id.iv_photo) public void clickPhoto() {
        Intent intent = new Intent();
        intent.putExtra("id_travel_notes", travelNotes.getId_travel_notes());
        intent.setClass(itemView.getContext(), TravelNotesActivity.class);
        itemView.getContext().startActivity(intent);
    }

}
