package lyf.com.example.itravel.holder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lyf.com.example.itravel.ITravelApplication;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.activity.LoginActivity;
import lyf.com.example.itravel.activity.UserInfoActivity;
import lyf.com.example.itravel.activity.UserSafeActivity;
import lyf.com.example.itravel.bean.User;
import lyf.com.example.itravel.utlis.SPUtils;

/**
 * 设置Holder
 */

public class SettingHolder extends RecyclerView.ViewHolder {

    private int position;

    @BindView( R.id.iv_go) ImageView ivGo;
    @BindView(R.id.tv_info) TextView tvInfo;

    public SettingHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    /**
     * 绑定数据
     */
    public void bindHolder(String info, int position) {
        this.position = position;
        tvInfo.setText(info);
        if (position == 2) {
            ivGo.setImageResource(R.drawable.login_out);
            ivGo.setMaxHeight(40);
            ivGo.setMaxWidth(40);
        }
    }

    /**
     * 响应点击事件
     */
    @OnClick({R.id.ll_go, R.id.iv_go}) public void go() {
        final Intent intent = new Intent();
        switch (position) {
            case 0:
                intent.setClass(itemView.getContext(), UserInfoActivity.class);
                itemView.getContext().startActivity(intent);
                break;
            case 1:
                intent.setClass(itemView.getContext(), UserSafeActivity.class);
                itemView.getContext().startActivity(intent);
                break;
            case 2:
                new AlertDialog.Builder(itemView.getContext()).setTitle("系统提示")//设置对话框标题
                        .setMessage("确定要退出登录吗？")//设置显示的内容
                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮

                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                ITravelApplication.getiTravelApplication().setUser(new User("", ""));

                                SPUtils.remove(itemView.getContext(), "account");
                                SPUtils.remove(itemView.getContext(), "password");

                                intent.setClass(itemView.getContext(), LoginActivity.class);
                                itemView.getContext().startActivity(intent);
                            }

                        }).setNegativeButton("取消",null).show();
                break;
            default:
                break;
        }

    }

}
