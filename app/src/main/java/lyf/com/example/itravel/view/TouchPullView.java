package lyf.com.example.itravel.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import lyf.com.example.itravel.R;

/**
 * Created by Administrator on 2017/8/8.
 */

public class TouchPullView extends View{

    private Path mPath = new Path();
    private Paint mPathPaint;
    private Paint mCirclePaint; //画笔

    private float mCircleRadius = 50; //半径
    private float mCirclePointX, mCirclePointY;
    private int mDragHeight = 300; //可拖动高度
    private float mProgress; //进度值
    private int mTargetWidth = 400; //目标宽度
    private int mTargetGravityHeight = 10; //重心点最终高度
    private int mTangentAngle = 105;//角度变换
    private Interpolator mProgressInterpolator = new DecelerateInterpolator();
    private Interpolator mTanentAngleInterpolator;

    private ValueAnimator valueAnimator;
    private Drawable mContent = null;
    private int mContentMargin = 0;

    /**
     * 添加释放操作
     */
    public void release() {
        if (valueAnimator == null) {
            ValueAnimator animator = ValueAnimator.ofFloat(mProgress, 0f);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.setDuration(600);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Object val = valueAnimator.getAnimatedValue();
                    if (val instanceof Float) {
                        setProress((Float) val);
                    }
                }
            });
            valueAnimator = animator;
        }else {
            valueAnimator.cancel();
            valueAnimator.setFloatValues(mProgress, 0f);
        }
        valueAnimator.start();
    }

    public TouchPullView(Context context) {
        super(context);
        init(null);
    }

    public TouchPullView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TouchPullView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TouchPullView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    /**
     * 初始化
     */
    private void init(AttributeSet attrs) {

        /**
         * 得到用户设置的参数
         */
        final  Context context = getContext();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TouchPullView, 0, 0);

        int color = array.getColor(R.styleable.TouchPullView_pColor, Color.BLACK);
        mCircleRadius = array.getDimension(R.styleable.TouchPullView_pRadius, mCircleRadius);
        mDragHeight = array.getDimensionPixelOffset(R.styleable.TouchPullView_pDragHeight, mDragHeight);
        mTangentAngle = array.getInteger(R.styleable.TouchPullView_pTangentAngle, 100);
        mTargetWidth = array.getDimensionPixelOffset(R.styleable.TouchPullView_pTargetWidth, mTargetWidth);
        mTargetGravityHeight = array.getDimensionPixelOffset(R.styleable.TouchPullView_pTargetGravityHeight, mTargetGravityHeight);
        mContent = array.getDrawable(R.styleable.TouchPullView_pContentDrawable);
        mContentMargin = array.getDimensionPixelOffset(R.styleable.TouchPullView_pContentDrawableMargin, 0);

        array.recycle();

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setAntiAlias(true); //锯齿
        p.setDither(true); //防抖动
        p.setStyle(Paint.Style.FILL); //填充
        p.setColor(Color.parseColor("#86D2C6"));
        mCirclePaint = p;

        //初始化路径部分画笔
        p.setAntiAlias(true); //锯齿
        p.setDither(true); //防抖动
        p.setStyle(Paint.Style.FILL); //填充
        p.setColor(Color.parseColor("#86D2C6"));
        mPathPaint = p;

        //切角路径差值器
        mTanentAngleInterpolator  = PathInterpolatorCompat.create(
                (mCircleRadius * 2.0f) / mDragHeight, 90.0f / mTangentAngle);

    }

    /**
     * 设置进度
     * @param progress
     */
    public void setProress(float progress) {
        mProgress = progress;
        requestLayout(); //重新进行测量
    }

    /**
     * 更新路径相关操作
     */
    private void updatePathLayout() {
        final float progress = mProgressInterpolator.getInterpolation(mProgress);

        //可绘制区域高度宽度
        final float w = getValueByLine(getWidth(), mTargetWidth, mProgress);
        final float h = getValueByLine(0, mDragHeight, mProgress);
        final float cPointX = w/2.0f; //x对称轴参数
        final float cRadius = mCircleRadius; //圆的半径
        final float cPointY = h - cRadius; //圆心Y坐标
        final float endControlY = mTargetGravityHeight; //结束Y

        mCirclePointX = cPointX;
        mCirclePointY = cPointY;

        final Path path = mPath; //路径

        //复位
        path.reset();
        path.moveTo(0, 0);

        float lEndPointX, lEndPointY;
        float lControlPointX, lControlPointY;

        float angle = mTangentAngle * mTanentAngleInterpolator.getInterpolation(progress);
        double radian = Math.toRadians(angle); //获取切线弧度
        float x = (float) (Math.sin(radian) * cRadius);
        float y = (float) (Math.cos(radian) * cRadius);

        lEndPointX = cPointX - x;
        lEndPointY = cPointY + y;

        lControlPointY = getValueByLine(0, endControlY, progress); //控制点Y的坐标变化
        float tHeight = lEndPointY - lControlPointY;
        float tWidth = (float) (tHeight / Math.tan(radian));
        lControlPointX = lEndPointX - tWidth;

        path.quadTo(lControlPointX, lControlPointY, lEndPointX, lEndPointY); //贝赛尔曲线
        path.lineTo(cPointX + (cPointX - lEndPointX), lEndPointY); //链接到右边
        path.quadTo(cPointX + cPointX - lControlPointX, lControlPointY, w, 0); //画右边的贝塞尔曲线

        updateContentLayout(cPointX, cPointY, cRadius); //更新内容部分
    }

    private void updateContentLayout(float cx, float cy, float radius) {
        Drawable drawable = mContent;
        if(drawable != null) {
            int margin = mContentMargin;
            int l = (int) (cx - radius + margin);
            int r = (int) (cx + radius - margin);
            int t = (int) (cy - radius + margin);
            int b = (int) (cy + radius - margin);
            drawable.setBounds(l, t, r, b);
        }
    }

    /**
     * 获取当前值
     * @param start 起始
     * @param end 结束
     * @param progress 进度
     * @return
     */
    private float getValueByLine(float start, float end, float progress) {
        return start + (end - start) * progress;
    }

    /**
     * 测量
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 宽度的意图，类型
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int iHeight = (int) ((mDragHeight * mProgress + 0.5f) + getPaddingTop() + getPaddingBottom()) ;
        int iWidth = (int) (2 * mCircleRadius + getPaddingLeft() + getPaddingRight());
        int measureWidth, measureHeight;

        if(widthMode == MeasureSpec.EXACTLY) {
            measureWidth = width; //确切的
        }else if(widthMode == MeasureSpec.AT_MOST) {
            measureWidth = Math.min(iWidth, width); // 最多
        }else {
            measureWidth = iWidth;
        }

        if(heightMode == MeasureSpec.EXACTLY) {
            measureHeight = height; //确切的
        }else if(widthMode == MeasureSpec.AT_MOST) {
            measureHeight = Math.min(iHeight, height); // 最多
        }else {
            measureHeight = iHeight;
        }

        setMeasuredDimension(measureWidth, measureHeight); //设置测量的高度宽度
    }

    /**
     * 当大小改变触发
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updatePathLayout(); //当高度变化时进行路径更新
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int count = canvas.save();
        float tranX = (getWidth() - getValueByLine(getWidth(), mTargetWidth, mProgress)) / 2;
        canvas.translate(tranX, 0);

        //画贝塞尔曲线
        canvas.drawPath(mPath, mPathPaint);

        //画圆
        canvas.drawCircle(mCirclePointX, mCirclePointY, mCircleRadius, mCirclePaint);

        Drawable drawable = mContent;
        if (drawable != null) {
            canvas.save();
            canvas.clipRect(drawable.getBounds()); //剪切矩形区域
            drawable.draw(canvas); //绘制
            canvas.restore();
        }

        canvas.restoreToCount(count);
    }
}
