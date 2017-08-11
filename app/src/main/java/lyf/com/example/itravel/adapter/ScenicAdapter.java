package lyf.com.example.itravel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import lyf.com.example.itravel.R;
import lyf.com.example.itravel.bean.Scenic;
import lyf.com.example.itravel.holder.ScenicHolder;

/**
 * 景点RecyclerView适配器
 */

public class ScenicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private LayoutInflater mLayoutInflater;

    private List<Scenic> scenics = new ArrayList<>();

    public ScenicAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(List<Scenic> list) {
        scenics.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScenicHolder(mLayoutInflater.inflate(R.layout.item_scenic, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ScenicHolder)holder).bindHolder(scenics.get(position));
    }

    @Override
    public int getItemCount() {
        return scenics.size();
    }

}
