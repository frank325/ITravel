package lyf.com.example.itravel.holder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.activity.MyClooectActivity;
import lyf.com.example.itravel.activity.MyShareActivity;

/**
 * Created by Administrator on 2017/8/10.
 */

public class PersonHolder extends RecyclerView.ViewHolder {

    private int position;
    @BindView(R.id.tv_info) TextView tvInfo;
    @BindView(R.id.iv_info) ImageView ivInfo;

    public PersonHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bindHolder(String info, int position) {
        this.position = position;
        tvInfo.setText(info);
        switch (position) {
            case 0:
                ivInfo.setImageResource(R.drawable.my_share);
                break;
            case 1:
                ivInfo.setImageResource(R.drawable.my_clooect);
                break;
        }
    }

    @OnClick({R.id.ll_person_my, R.id.tv_info, R.id.iv_info}) public void gomy() {
        Intent intent = new Intent();
        switch (position) {
            case 0:
                intent.setClass(itemView.getContext(), MyShareActivity.class);
                break;
            case 1:
                intent.setClass(itemView.getContext(), MyClooectActivity.class);
                break;
        }
        itemView.getContext().startActivity(intent);
    }
}
