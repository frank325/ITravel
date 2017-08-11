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

import lyf.com.example.itravel.ITravelApplication;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.bean.TravelNotes;
import lyf.com.example.itravel.holder.MyClooectHolder;
import lyf.com.example.itravel.holder.MyShareHolder;
import lyf.com.example.itravel.model.OkhttpModel;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/8.
 */

public class MyClooectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private boolean isClooect;
    private int num;
    private String account;
    private HashMap<String, String> hashMap = new HashMap<>();
    private LayoutInflater mLayoutInflater;

    private List<TravelNotes> travelNotes = new ArrayList<>();

    public MyClooectAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(List<TravelNotes> list) {
        travelNotes.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyClooectHolder(mLayoutInflater.inflate(R.layout.item_my_clooect, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((MyClooectHolder)holder).bindHolder(travelNotes.get(position), position);
        ((MyClooectHolder)holder).tvCloseClooect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mLayoutInflater.getContext()).setTitle("系统提示")//设置对话框标题
                        .setMessage("取消收藏？")//设置显示的内容
                        .setPositiveButton("是",new DialogInterface.OnClickListener() {//添加确定按钮

                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                isClooect = false;
                                num = Integer.parseInt(travelNotes.get(position).getClooect_num()) - 1;
                                account = ITravelApplication.getiTravelApplication().getUser().getAccount();

                                hashMap.put("account", account);
                                hashMap.put("isClooect", isClooect + "");
                                hashMap.put("clooect_num", "" + num);
                                hashMap.put("id_travel_notes", travelNotes.get(position).getId_travel_notes());
                                OkhttpModel okhttpModel = new OkhttpModel();
                                okhttpModel.doGet("updateClooectNum.ao", hashMap, new Callback() {

                                    @Override
                                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                                        return response.body().string();
                                    }

                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        Toast.makeText(mLayoutInflater.getContext(), "错误", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onResponse(Object response, int id) {
                                        if("Success".equals(response.toString())) {
                                            Toast.makeText(mLayoutInflater.getContext(), "已取消收藏", Toast.LENGTH_SHORT).show();
                                            travelNotes.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(0, travelNotes.size());
                                        }
                                    }
                                });
                            }

                        }).setNegativeButton("否",null).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return travelNotes.size();
    }
}
