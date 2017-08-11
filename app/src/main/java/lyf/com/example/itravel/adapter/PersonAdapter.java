package lyf.com.example.itravel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import lyf.com.example.itravel.R;
import lyf.com.example.itravel.holder.PersonHolder;
import lyf.com.example.itravel.holder.SettingHolder;

/**
 * Created by Administrator on 2017/8/8.
 */

public class PersonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private String[] info = new String[2];

    public void addArray(String[] info) {
        for (int i = 0; i < info.length ; i++) {
            this.info[i] = info[i];
        }
    }

    public PersonAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PersonHolder(mLayoutInflater.inflate(R.layout.item_person_my, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PersonHolder) holder).bindHolder(info[position], position);
    }

    @Override
    public int getItemCount() {
        return info.length;
    }
}

