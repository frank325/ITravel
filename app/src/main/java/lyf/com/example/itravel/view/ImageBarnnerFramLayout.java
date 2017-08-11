package lyf.com.example.itravel.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.List;

import lyf.com.example.itravel.ITravelConstant;
import lyf.com.example.itravel.R;

/**
 * 轮播图片底部圆点布局
 */

public class ImageBarnnerFramLayout extends FrameLayout implements ImageBarnnerViewGroup.ImageBarnnerViewGroupLisnner {

    private ImageBarnnerViewGroup imageBarnnerViewGroup;
    private LinearLayout linearLayout;
    public int index;

    public ImageBarnnerFramLayout(@NonNull Context context) {
        super(context);
        initImageBarnnerViewGroup();
        initDotLinearlayout();
    }

    public ImageBarnnerFramLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initImageBarnnerViewGroup();
        initDotLinearlayout();
    }

    public ImageBarnnerFramLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initImageBarnnerViewGroup();
        initDotLinearlayout();
    }

    /**
     * 整体布局
     */
    public void addBitmaps(String [] urls) {
        for (int i = 0; i < urls.length; i++) {
            addBitmapToImageBarnnerViewGroup(urls[i]);
            addDotToLinearlayout();
        }
    }

    /**
     * 添加底部圆点布局
     */
    private void addDotToLinearlayout() {
        ImageView iv = new ImageView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 5, 5, 5);
        iv.setLayoutParams(lp);
        iv.setImageResource(R.drawable.dot_nomal);
        linearLayout.addView(iv);
    }

    /**
     * 添加图片
     */
    private void addBitmapToImageBarnnerViewGroup(String url) {
        ImageView iv = new ImageView(getContext());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        Picasso.with(getContext()).load(ITravelConstant.URL + url).resize(1000, 400).into(iv);
        imageBarnnerViewGroup.addView(iv);
    }

    /**
     * 初始化 自定义图片轮播功能核心类
     */
    private void initImageBarnnerViewGroup() {
        imageBarnnerViewGroup = new ImageBarnnerViewGroup(getContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        imageBarnnerViewGroup.setLayoutParams(lp);
        imageBarnnerViewGroup.setBarnnerViewGroupLisnner(this);
        addView(imageBarnnerViewGroup);
    }

    /**
     * 初始化 底部圆点布局
     */
    private void initDotLinearlayout() {
        linearLayout = new LinearLayout(getContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,30);
        lp.setMargins(0, 0, 15, 10);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.RIGHT);

        addView(linearLayout);

        FrameLayout.LayoutParams layoutParams = (LayoutParams) linearLayout.getLayoutParams();
        layoutParams.gravity = Gravity.BOTTOM;

        linearLayout.setLayoutParams(layoutParams);
    }

    /**
     * 底部原点的切换
     */
    @Override
    public void selectImage(int index) {
        this.index = index;
        int count = linearLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            ImageView iv = (ImageView) linearLayout.getChildAt(i);
            if (i == index) {
                iv.setImageResource(R.drawable.dot_select);
            }else {
                iv.setImageResource(R.drawable.dot_nomal);
            }
        }
    }
    
}
