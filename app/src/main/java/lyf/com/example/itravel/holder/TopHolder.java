package lyf.com.example.itravel.holder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lyf.com.example.itravel.ITravelConstant;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.activity.CityIntroduceActivity;
import lyf.com.example.itravel.bean.City;

/**
 * 热门城市Holder
 */

public class TopHolder extends RecyclerView.ViewHolder {

    private City city;

    @BindView(R.id.iv_city_photo) ImageView ivCityPhoto;
    @BindView(R.id.tv_city_name) TextView tvCityName;
    @BindView(R.id.tv_city_address) TextView tvCityAddress;
    @BindView(R.id.tv_think_go_num) TextView tvThinkGoNum;
    @BindView(R.id.tv_top) TextView tvTop;

    public TopHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    /**
     * 绑定数据
     */
    public void bindHolder(City city, int position) {
        this.city = city;
        Picasso.with(itemView.getContext()).load(ITravelConstant.URL + city.getCity_photo_url())
                .resize(ITravelConstant.WITTH, 480).into(ivCityPhoto);
        tvTop.setText("Top" + (position + 1));
        tvCityName.setText(city.getCity_name());
        tvCityAddress.setText(city.getCity_address());
        tvThinkGoNum.setText(city.getThink_go_num());
    }

    /**
     * 响应点击事件，跳转至城市信息页面
     */
    @OnClick(R.id.iv_city_photo) public void clickPhoto() {
        Intent intent = new Intent();
        intent.putExtra("city_name", city.getCity_name());
        intent.setClass(itemView.getContext(), CityIntroduceActivity.class);
        itemView.getContext().startActivity(intent);
    }

}
