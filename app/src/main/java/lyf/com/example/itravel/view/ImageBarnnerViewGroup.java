package lyf.com.example.itravel.view;

import android.content.Context;

import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义图片轮播功能核心类
 */

public class ImageBarnnerViewGroup extends ViewGroup {

    private int children, childHeight, childWidth;
    private int x, index; //x：第一次按下的位置横坐标 index：每张图片的索引值
    private boolean isAuto = true; //默认开启自动轮播

    private Timer timer = new Timer();
    private TimerTask task;
    private Scroller scroller;

    /**
     * 监听图片变换
     */
    public interface ImageBarnnerViewGroupLisnner {
        void selectImage(int index); //设置显示的图片索引值
    }
    private ImageBarnnerViewGroupLisnner barnnerViewGroupLisnner;
    public void setBarnnerViewGroupLisnner(ImageBarnnerViewGroupLisnner barnnerViewGroupLisnner) {
        this.barnnerViewGroupLisnner = barnnerViewGroupLisnner;
    }
    public ImageBarnnerViewGroupLisnner getBarnnerViewGroupLisnner() {
        return barnnerViewGroupLisnner;
    }

    /**
     * 判断是否进行图片自动轮播
     */
    private Handler autoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: //需要图片自动轮播

                    if (++index >= children) { //最后一张图，从第一张开始滑动
                        index = 0;
                    }

                    scrollTo(childWidth * index, 0);
                    barnnerViewGroupLisnner.selectImage(index);
                    break;
            }
        }
    };

    /**
     * 开始自动轮播
     */
    private void startAuto() {
        isAuto = true;
    }

    /**
     * 停止自动轮播
     */
    private void stopAuto() {
        isAuto = false;
    }

    public ImageBarnnerViewGroup(Context context) {
        super(context);
        initObj();
    }

    public ImageBarnnerViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initObj();
    }

    public ImageBarnnerViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initObj();
    }

    private void initObj() {
        scroller = new Scroller(getContext());

        task = new TimerTask() {
            @Override
            public void run() {
                if (isAuto) {
                    autoHandler.sendEmptyMessage(0); //开始图片轮播
                }
            }
        };

        timer.schedule(task, 100, 3000); //每3秒换一张图片
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), 0);
            invalidate();
        }
    }

    /**
     * 测量
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        children = getChildCount(); //求出子视图的个数

        if(children == 0) {
            setMeasuredDimension(0, 0);
        }else {
            measureChildren(widthMeasureSpec, heightMeasureSpec); //测量子视图的高度和宽度

            /**
             * 求出ViewGroup的高度和宽度
             */
            View view = getChildAt(0);
            childWidth = view.getMeasuredWidth(); //子视图宽度
            childHeight = view.getMeasuredHeight(); //子视图宽度
            int width = view.getMeasuredWidth() * children; //所有子视图宽度和
            setMeasuredDimension(width, childHeight);
        }
    }

    /**
     * 实现布局
     * @param change 当ViewGroup布局位置发生改变时为true
     */
    @Override
    protected void onLayout(boolean change, int l, int t, int r, int b) {
        if(change) {
            int leftMargin = 0;

            for (int i = 0; i < children; i++){
                View view = getChildAt(i);

                view.layout(leftMargin, 0, leftMargin + childWidth, childHeight);
                leftMargin += childWidth;
            }
        }
    }

    /**
     * 容器的拦截方法
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: //用户按下的一瞬间
                stopAuto(); //停止轮播
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                x = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE: //用户按下之后在屏幕上移动的过程
                int moveX = (int) event.getX();
                int distance = moveX - x;
                scrollBy(-distance, 0);
                x = moveX;
                break;
            case MotionEvent.ACTION_UP: //用户抬起的一瞬间
                int scrollX = getScrollX();
                index = (scrollX + childWidth / 2) / childWidth;
                if (index < 0) { //最左边图片
                    index = 0;
                }else if (index > children - 1) { //最右边图片
                    index = children - 1;
                }

                int dx = index *childWidth - scrollX;

                scroller.startScroll(scrollX, 0, dx, 0);
                postInvalidate();
                barnnerViewGroupLisnner.selectImage(index);

                startAuto(); //开始轮播
                break;
            default:
                break;
        }
        return true; //已经处理好事件
    }
}
