package lyf.com.example.itravel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import lyf.com.example.itravel.R;
import lyf.com.example.itravel.holder.UserInfoHolder;

/**
 * Created by Administrator on 2017/8/7.
 */

public class UserInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private String[] userInfo = new String[5];

    public void addArray(String[] user) {
        for (int i = 0; i < user.length ; i++) {
            userInfo[i] = user[i];
        }
    }

    public UserInfoAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserInfoHolder(mLayoutInflater.inflate(R.layout.item_user_info, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((UserInfoHolder) holder).bindHolder(userInfo[position], position);
    }

    @Override
    public int getItemCount() {
        return userInfo.length;
    }
}
