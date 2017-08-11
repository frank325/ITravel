package lyf.com.example.itravel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import lyf.com.example.itravel.R;
import lyf.com.example.itravel.bean.City;
import lyf.com.example.itravel.holder.HomePageHolder;

/**
 * 首页RecyclerView适配器
 */

public class HomePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private LayoutInflater mLayoutInflater;

    private List<City> citys = new ArrayList<>();

    public HomePageAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(List<City> list) {
        citys.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomePageHolder(mLayoutInflater.inflate(R.layout.item_home_page, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((HomePageHolder)holder).bindHolder(citys.get(position));
    }

    @Override
    public int getItemCount() {
        return citys.size();
    }
}
