package lyf.com.example.itravel.holder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import lyf.com.example.itravel.activity.UpdateHeadPortraitActivity;
import lyf.com.example.itravel.activity.UpdateUserInfoActivity;
import lyf.com.example.itravel.model.OkhttpModel;
import lyf.com.example.itravel.view.CircleTransform;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 用户信息Holder
 */

public class UserInfoHolder extends RecyclerView.ViewHolder {

    private HashMap<String, String> hashMap = new HashMap<>();
    private String account;
    private String data, dataName;
    private int position;
    private AlertDialog ad;

    @BindView(R.id.rl_user_info) RelativeLayout rlUserInfo;
    @BindView(R.id.tv_data_name) TextView tvDataName;
    @BindView(R.id.tv_data) TextView tvData;
    @BindView(R.id.iv_head_portrait) ImageView ivHeadPortrait;
    @BindView(R.id.iv_go) ImageView ivGo;

    public UserInfoHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    /**
     * 绑定数据
     */
    public void bindHolder(String userInfo, int position) {
        this.data = userInfo;
        this.position = position;

        switch (position) {
            case 0:
                Picasso.with(itemView.getContext()).load(ITravelConstant.URL + userInfo)
                        .resize(150, 150).transform(new CircleTransform())
                        .error(R.drawable.default_whith).into(ivHeadPortrait);
                dataName = "head_portrait_url";
                tvDataName.setText("头像");
                ivHeadPortrait.setVisibility(View.VISIBLE);
                break;
            case 1:
                tvDataName.setText("帐号");
                tvData.setText(userInfo);
                ivGo.setVisibility(View.INVISIBLE);
                break;
            case 2:
                dataName = "name";
                tvDataName.setText("昵称");
                tvData.setText(userInfo);
                break;
            case 3:
                dataName = "gender";
                tvDataName.setText("性别");
                tvData.setText(userInfo);
                break;
            case 4:
                dataName = "signature";
                tvDataName.setText("个性签名");
                if (userInfo.length() > 13) {
                    tvData.setText(userInfo.substring(0,13) + "...");
                }else {
                    tvData.setText(userInfo);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 响应点击事件，执行修改用户信息
     */
    @OnClick({R.id.rl_user_info, R.id.iv_go}) public void updateUserInfo() {
        Intent intent = new Intent();
        intent.putExtra("data", data);
        intent.putExtra("dataName", dataName);

        switch (position) {
            case 0:
                intent.setClass(itemView.getContext(), UpdateHeadPortraitActivity.class);
                itemView.getContext().startActivity(intent);
                break;
            case 2:
                intent.setClass(itemView.getContext(), UpdateUserInfoActivity.class);
                itemView.getContext().startActivity(intent);
                break;
            case 3:
                final String[] s = {"男", "女", "取消"}; //Dialog普通列表数据

                ad = new android.app.AlertDialog.Builder(itemView.getContext())  //创建Dialog普通列表
                        .setItems(s, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        data = "男";
                                        updateInfo();
                                        break;
                                    case 1:
                                        data = "女";
                                        updateInfo();
                                        break;
                                    default:
                                        break;
                                }
                                dialog.dismiss(); //关闭Dialog
                            }
                        }).create();  //创建
                ad.show(); //关闭显示
                break;
            case 4:
                intent.setClass(itemView.getContext(), UpdateUserInfoActivity.class);
                itemView.getContext().startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 修改用户信息
     */
    public void updateInfo() {
        account = ITravelApplication.getiTravelApplication().getUser().getAccount();

        hashMap.put("account", account);
        hashMap.put("data", data);
        hashMap.put("dataName", dataName);

        /**
         * 发送网络请求
         */
        OkhttpModel okhttpModel = new OkhttpModel();
        okhttpModel.doGet("updateUserInfo.do", hashMap, new Callback() {

            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(itemView.getContext(), "修改失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response, int id) {
                Toast.makeText(itemView.getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                tvData.setText(data);
            }
        });
    }

}
