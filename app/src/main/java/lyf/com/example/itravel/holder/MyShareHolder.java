package lyf.com.example.itravel.holder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lyf.com.example.itravel.ITravelConstant;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.activity.TravelNotesActivity;
import lyf.com.example.itravel.bean.TravelNotes;

/**
 * 我的分享Holder
 */

public class MyShareHolder extends RecyclerView.ViewHolder {

    private TravelNotes travelNotes;

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

    /**
     * 绑定数据
     */
    public void bindHolder(TravelNotes travelNotes) {
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

    /**
     * 响应点击事件，跳转至游记信息页面
     */
    @OnClick(R.id.iv_go) public void go() {
        Intent intent = new Intent();
        intent.putExtra("id_travel_notes", travelNotes.getId_travel_notes());
        intent.setClass(itemView.getContext(), TravelNotesActivity.class);
        itemView.getContext().startActivity(intent);
    }

}
