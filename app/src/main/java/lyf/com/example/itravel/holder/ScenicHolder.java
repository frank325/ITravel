package lyf.com.example.itravel.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import lyf.com.example.itravel.ITravelConstant;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.bean.Scenic;

/**
 * 景点Holder
 */

public class ScenicHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_scenic_photo) ImageView ivScenicPhoto;
    @BindView(R.id.tv_scenic_name) TextView tvScenicName;
    @BindView(R.id.tv_scenic_introduce) TextView tvScenicIntroduce;

    public ScenicHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    /**
     * 绑定数据
     */
    public void bindHolder(Scenic scenic) {
        Picasso.with(itemView.getContext()).load(ITravelConstant.URL + scenic.getScenic_photo_url())
                .resize(ITravelConstant.WITTH, 450).into(ivScenicPhoto);
        tvScenicName.setText(scenic.getScenic_name());
        tvScenicIntroduce.setText("        "+scenic.getScenic_introduce());
    }

}
