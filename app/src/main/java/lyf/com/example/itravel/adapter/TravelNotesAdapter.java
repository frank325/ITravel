package lyf.com.example.itravel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import lyf.com.example.itravel.R;
import lyf.com.example.itravel.bean.TravelNotes;
import lyf.com.example.itravel.holder.TravelNotesHolder;

/**
 * 游记RecyclerView适配器
 */

public class TravelNotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private LayoutInflater mLayoutInflater;

    private List<TravelNotes> travelNotes = new ArrayList<>();

    public TravelNotesAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(List<TravelNotes> list) {
        travelNotes.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TravelNotesHolder(mLayoutInflater.inflate(R.layout.item_share, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TravelNotesHolder)holder).bindHolder(travelNotes.get(position));
    }

    @Override
    public int getItemCount() {
        return travelNotes.size();
    }

}
