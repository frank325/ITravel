package lyf.com.example.itravel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import lyf.com.example.itravel.R;
import lyf.com.example.itravel.holder.SettingHolder;
import lyf.com.example.itravel.holder.UserInfoHolder;

/**
 * Created by Administrator on 2017/8/8.
 */

public class SettingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private String[] info = new String[3];

    public void addArray(String[] info) {
        for (int i = 0; i < info.length ; i++) {
            this.info[i] = info[i];
        }
    }

    public SettingAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SettingHolder(mLayoutInflater.inflate(R.layout.item_setting, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SettingHolder) holder).bindHolder(info[position], position);
    }

    @Override
    public int getItemCount() {
        return info.length;
    }
}

