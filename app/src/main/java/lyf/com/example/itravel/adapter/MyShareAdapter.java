package lyf.com.example.itravel.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lyf.com.example.itravel.R;
import lyf.com.example.itravel.bean.TravelNotes;
import lyf.com.example.itravel.holder.MyShareHolder;
import lyf.com.example.itravel.model.OkhttpModel;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 我的分享RecyclerView适配器
 */

public class MyShareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private String account;
    private HashMap<String, String> hashMap = new HashMap<>();
    private LayoutInflater mLayoutInflater;

    private List<TravelNotes> travelNotes = new ArrayList<>();

    public MyShareAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(List<TravelNotes> list) {
        travelNotes.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyShareHolder(mLayoutInflater.inflate(R.layout.item_my_share, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((MyShareHolder)holder).bindHolder(travelNotes.get(position));

        /**
         * 监听点击事件，弹出窗口
         */
        ((MyShareHolder) holder).tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mLayoutInflater.getContext()).setTitle("系统提示")//设置对话框标题
                        .setMessage("确定要删除吗？")//设置显示的内容
                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮

                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                account = travelNotes.get(position).getTravel_account();

                                hashMap.put("account", account);
                                hashMap.put("id_travel_notes", travelNotes.get(position).getId_travel_notes());

                                /**
                                 * 发送网络请求
                                 */
                                OkhttpModel okhttpModel = new OkhttpModel();
                                okhttpModel.doGet("deleTravelNotesInfo.ao", hashMap, new Callback() {

                                    @Override
                                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                                        return response.body().string();
                                    }

                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        Toast.makeText(mLayoutInflater.getContext(), "删除失败", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onResponse(Object response, int id) {
                                        Toast.makeText(mLayoutInflater.getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                                        if("删除成功".equals(response.toString())) {
                                            travelNotes.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(0, travelNotes.size());
                                        }
                                    }
                                });
                            }

                        }).setNegativeButton("取消",null).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return travelNotes.size();
    }
}
