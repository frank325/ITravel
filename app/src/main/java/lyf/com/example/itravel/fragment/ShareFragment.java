package lyf.com.example.itravel.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.callback.Callback;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.adapter.ShareAdapter;
import lyf.com.example.itravel.bean.TravelNotes;
import lyf.com.example.itravel.model.OkhttpModel;
import lyf.com.example.itravel.utlis.JsonAnalysisUtils;
import lyf.com.example.itravel.view.TouchPullView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 分享Fragment
 */

public class ShareFragment extends Fragment {

    private boolean isCanScroll;
    private ShareAdapter shareAdapter;
    private int resunmeNum = 0;
    private boolean isGetInfoSuccess = false;
    private List<TravelNotes> travelNotes;
    private View view;
    private static final float TOUCH_MOVE_MAX_Y = 400;
    private float mTouchMoveStartY = 0;

    @BindView(R.id.rv_share) RecyclerView rvShare;
    @BindView(R.id.touchpull) TouchPullView touchPull;
    @BindView(R.id.tv_refresh) TextView tvRefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_share, container, false);

        ButterKnife.bind(this, view);
        initRecylerView();
        initData();
        initTouchPull();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ++resunmeNum;
        if (resunmeNum != 1) {
            initData();
            initRecylerView();
        }
    }

    /**
     * 初始化RecylerView
     */
    private void initRecylerView() {
        rvShare.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        shareAdapter = new ShareAdapter(getContext());
        rvShare.setAdapter(shareAdapter);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        isGetInfoSuccess = false;
        tvRefresh.setVisibility(View.VISIBLE);
        tvRefresh.setText("正在刷新...");

        /**
         * 发送网络请求
         */
        OkhttpModel okhttpModel = new OkhttpModel();
        okhttpModel.doGet("getAllTravelNotesInfo.ao", new HashMap<String, String>(), new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                tvRefresh.setText("下拉刷新");
            }

            @Override
            public void onResponse(Object response, int id) {
                travelNotes = JsonAnalysisUtils.parseAllTravelNotesJson(response.toString());
                if (JsonAnalysisUtils.isSuccess) {
                    tvRefresh.setVisibility(View.GONE);
                    isGetInfoSuccess = true;
                    shareAdapter.addList(travelNotes);
                    shareAdapter.notifyDataSetChanged();
                }else {
                    tvRefresh.setText("下拉刷新");
                    Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 获取用户点击屏幕之后的操作
     */
    private void initTouchPull() {
        rvShare.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: //用户按下的一瞬间
                        mTouchMoveStartY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE: //用户按下之后在屏幕上移动的过程
                        float y = motionEvent.getY();
                        isCanScroll = rvShare.canScrollVertically(-1); //判断是否在顶部
                        if (!isCanScroll) { //在顶部
                            if (y >= mTouchMoveStartY) {
                                tvRefresh.setVisibility(View.VISIBLE);
                                tvRefresh.setText("下拉刷新");
                                float moveSize = y - mTouchMoveStartY;
                                float progress = moveSize >= TOUCH_MOVE_MAX_Y ? 1 : moveSize / TOUCH_MOVE_MAX_Y;
                                touchPull.setProress(progress);
                                return true;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP: //用户离开屏幕的瞬间
                        float y1 = motionEvent.getY();
                        isCanScroll = rvShare.canScrollVertically(-1);
                        if (!isCanScroll) {
                            if (y1 - mTouchMoveStartY > 350) {
                                initRecylerView();
                                initData();
                                touchPull.release();
                                return false;
                            }
                        }
                        tvRefresh.setVisibility(View.GONE);
                        touchPull.release();
                        break;
                    default:

                        break;
                }
                return !isGetInfoSuccess;
            }
        });
    }

}
