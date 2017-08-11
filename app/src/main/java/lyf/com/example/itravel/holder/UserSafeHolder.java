package lyf.com.example.itravel.holder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.activity.UpdatePasswordActivity;

/**
 * 账户安全Holder
 */

public class UserSafeHolder extends RecyclerView.ViewHolder {

    private int position;

    @BindView(R.id.tv_info) TextView tvInfo;

    public UserSafeHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    /**
     * 绑定数据
     */
    public void bindHolder(String info, int position) {
        this.position = position;
        tvInfo.setText(info);
    }

    /**
     * 响应点击事件
     */
    @OnClick({R.id.ll_go, R.id.iv_go}) public void go() {
        Intent intent = new Intent();
        switch (position) {
            case 0:
                intent.setClass(itemView.getContext(), UpdatePasswordActivity.class);
                itemView.getContext().startActivity(intent);
                break;
            default:
                break;
        }
    }

}
