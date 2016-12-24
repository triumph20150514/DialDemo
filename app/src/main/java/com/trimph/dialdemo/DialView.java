package com.trimph.dialdemo;

import android.animation.ArgbEvaluator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * author: Trimph
 * data: 2016/12/22.
 * description: 刻度盘视图
 */

public class DialView extends View {

    public Paint loopPaint; //圆环画笔
    public Paint originPaint; //空圆环画笔
    public Paint mPaint; //进度画笔
    public Paint kPaint; //刻度
    public Paint outPaint; //外层画笔
    public Paint whitePaint;

    public int outDantance = 50;
    public int loopDantance = outDantance * 2;
    public int kDantance = outDantance * 3;

    public RectF mRect = new RectF();

    public RectF wRect = new RectF();

    public RectF loopRect = new RectF();

    public RectF outRect = new RectF();

    public RectF kRect = new RectF();

    public int Resouse;
    public Bitmap mBitmap;
    public int mWidth, mHeight;

    public DialView(Context context) {
        this(context, null);
    }

    public DialView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.dialView);
        Resouse = typedArray.getInteger(R.styleable.dialView_bg_state, R.mipmap.ic_launcher);
        typedArray.recycle();

        init();
    }

    public int[] ints = {
//            R.color.loop_first,
            Color.argb(255, 9, 234, 194),
            Color.argb(255, 10, 166, 248),
            Color.argb(255, 91, 106, 255)};

//            R.color.loop_second,
//            R.color.loop_third};


    public int[] origin = {
//            R.color.loop_first,
            Color.argb(255, 228, 242, 253),
            Color.argb(255, 239, 250, 255),
            Color.argb(255, 228, 242, 253)
    };

    /**
     * 外层阴影
     */
    public int[] outColors = {
//            R.color.loop_first,
            Color.argb(255, 210, 249, 247),
            Color.argb(255, 202, 229, 247),
            Color.argb(255, 229, 222, 254)
    };

    // 最外层圆环渐变色环颜色
    private final int[] mColors = new int[]{
            0xFFFF0000,
            0xFFFFD600,
            0xFF00FF00
    };

    public float currentAngle;
    public float angle;

    public float getCurrentAngle() {
        return currentAngle;
    }

    public void setCurrentAngle(float currentAngle) {
        this.currentAngle = currentAngle;
        startAnim();
    }

    private void init() {

        //最外层圆环渐变画笔设置
        loopPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //设置圆环渐变色渲染
        loopPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        float[] position = {0.1f, 0.2f, 0.3f};
        Shader mShader = new SweepGradient(mWidth / 2, mHeight / 2, ints, position);
        Matrix matrix = new Matrix();
        matrix.setRotate(-183, mWidth / 2, mHeight / 2);
        mShader.setLocalMatrix(matrix);

        loopPaint.setShader(mShader);
        loopPaint.setAntiAlias(true);
        loopPaint.setStyle(Paint.Style.STROKE);
//        loopPaint.setAlpha(150);
        loopPaint.setStrokeCap(Paint.Cap.ROUND);
        loopPaint.setStrokeWidth(40f);


        //刻度
        kPaint = new Paint();
        kPaint.setColor(Color.parseColor("#AED7F8"));
        kPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        kPaint.setAlpha(150);
        kPaint.setStrokeCap(Paint.Cap.ROUND);
        kPaint.setStrokeWidth(5f);


        //
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置圆环渐变色渲染
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        float[] position1 = {0.1f, 0.2f, 0.3f};
        Shader mShader1 = new SweepGradient(mWidth / 2, mHeight / 2, outColors, position1);
        Matrix matrix1 = new Matrix();
        matrix1.setRotate(-153, mWidth / 2, mHeight / 2);
        mShader1.setLocalMatrix(matrix1);
        mPaint.setShader(mShader1);
//        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAlpha(150);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(20f);

        //外层画笔
        outPaint = new Paint();
        outPaint.setColor(Color.parseColor("#6DBEFD"));
        outPaint.setStyle(Paint.Style.STROKE);
        outPaint.setAlpha(150);
        outPaint.setStrokeCap(Paint.Cap.ROUND);
        outPaint.setStrokeWidth(7f);


        //空心圆环画笔
        originPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置圆环渐变色渲染
        originPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        float[] originPosition = {0.1f, 0.2f, 0.3f};
        Shader originShader = new SweepGradient(mWidth / 2, mHeight / 2, origin, originPosition);
        Matrix originMatrix = new Matrix();
        //旋转到开始渐变的位置
        originMatrix.setRotate(-180, mWidth / 2, mHeight / 2);
        originShader.setLocalMatrix(originMatrix);
        originPaint.setShader(originShader);
        originPaint.setColor(Color.parseColor("#E4F2FD"));
        originPaint.setStyle(Paint.Style.STROKE);
        originPaint.setAlpha(150);
        originPaint.setStrokeCap(Paint.Cap.ROUND);
        originPaint.setStrokeWidth(40f);


        /**
         * 纯白
         */
        whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStyle(Paint.Style.STROKE);
        whitePaint.setAlpha(200);
        whitePaint.setStrokeCap(Paint.Cap.ROUND);
        whitePaint.setStrokeWidth(30f);

        //画图
        mBitmap = BitmapFactory.decodeResource(getResources(), Resouse);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("DialView:", "onMeasure");
//        setMeasuredDimension(getDialMearsureWidth(widthMeasureSpec), getDialMearsureHeight(heightMeasureSpec));
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.getLayoutParams();
        Log.e("Margin:", marginLayoutParams.topMargin + "");
        Log.e("DialView:", "onLayout");

        mHeight = mHeight + marginLayoutParams.topMargin * 2;
        mHeight = mHeight - marginLayoutParams.bottomMargin * 2;

    }

    private int getDialMearsureHeight(int heightMeasureSpec) {

        int result;
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);

        if (mode == MeasureSpec.EXACTLY) {

        } else if (mode == MeasureSpec.UNSPECIFIED) {

        } else {  //MeasureSpec.AT_MOST

        }
        return 0;
    }

    private int getDialMearsureWidth(int widthMeasureSpec) {


        return 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("DialView:", "onSizeChanged");
        mWidth = w - getPaddingLeft() - getPaddingRight();
        mHeight = h - getPaddingBottom() - getPaddingTop();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.e("mWidth", mWidth + "　ｍHeight" + mHeight);
        canvas.translate(mWidth / 2, mHeight / 2);
        canvas.drawPoint(mWidth / 2, mHeight / 2, loopPaint);

        if (mWidth > mHeight) {
            mHeight = mWidth;
        }
        /**
         * 空心
         */
        mRect.set(-mWidth / 2 + outDantance * 2, -mWidth / 2 + outDantance * 2,
                mWidth / 2 - outDantance * 2, mWidth / 2 - outDantance * 2);
        canvas.drawArc(mRect, 0, -180, false, originPaint);

        canvas.drawPoint(-mWidth / 2 + outDantance * 2, -mHeight / 2 + outDantance * 2, loopPaint);
        canvas.drawPoint(mWidth / 2 - outDantance * 2, mWidth / 2 - outDantance * 2, loopPaint);
        /**
         * 白色
         */
        wRect.set(-mWidth / 2 + outDantance * 2 + 20, -mWidth / 2 + outDantance * 2 + 20, mWidth / 2 - outDantance * 2 - 20, mWidth / 2 - outDantance * 2 - 20);
        canvas.drawArc(mRect, 0, -180, false, whitePaint);

        //最外边的
        outRect.set(-mWidth / 2 + outDantance, -mWidth / 2 + outDantance, mWidth / 2 - outDantance, mWidth / 2 - outDantance);
        canvas.drawArc(outRect, 2, -184, false, outPaint);


//        //最外边的阴影
        outRect.set(-mWidth / 2 + outDantance - 10, -mWidth / 2 + outDantance - 10, mWidth / 2 - outDantance + 10, mWidth / 2 - outDantance + 10);

        canvas.drawArc(outRect, -30, -120, false, mPaint);


        /**
         * 刻度圆
         */
        kRect.set(-mWidth / 2 + kDantance, -mWidth / 2 + kDantance, mWidth / 2 - kDantance, mWidth / 2 - kDantance);
//        canvas.drawArc(kRect, 0, -180, false, outPaint);
//        drawLine(canvas);

        //刻度
        drawLine2(canvas);

        //最外层两个拐
        drawOutLine(canvas);


        /**
         * 画进度
         */
        mRect.set(-mWidth / 2 + outDantance * 2, -mWidth / 2 + outDantance * 2, mWidth / 2 - outDantance * 2, mWidth / 2 - outDantance * 2);
        canvas.drawArc(mRect, -180, angle, false, loopPaint);

        initBitmap(canvas);
    }

    /**
     * 绘制图
     *
     * @param canvas
     */
    private void initBitmap(Canvas canvas) {
        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setTranslate(-w / 2, -h);
        canvas.rotate(angle, 0, 0);

        canvas.drawBitmap(mBitmap, matrix, outPaint);
    }

    /**
     * 最外层拐角
     *
     * @param canvas
     */
    private void drawOutLine(Canvas canvas) {

        int radius = -mWidth / 2 + outDantance;

        canvas.save();
        float startX = (int) (radius * 0.9);
        float endX = (int) (radius * 1.06);
        canvas.drawLine(radius, 16, endX, 16, outPaint);
        canvas.drawLine(-radius, 16, -endX, 16, outPaint);
        canvas.restore();
    }

    /**
     * 绘制刻度线
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        int radius = mWidth / 2 - kDantance;
        canvas.save();
        float startX = (int) (radius * 0.9);
        float inStartX = (int) (radius * 0.95);
        float endX = radius;
        float nuber = 180 / 13;
        int n = 13 * 5;
        float nubers = 180 / 13 / 13 + nuber;
        for (int i = 0; i < 14; i++) {
            Log.e("Y1::" + i + "::::", Math.ceil(startX * Math.sin(i * 14)) + " y2::" + Math.ceil(radius * Math.sin(i * 14)));
            canvas.drawLine(startX, 0, endX, 0, outPaint);
            canvas.rotate(-nubers);
//                canvas.drawLine(startX,0, endX, 0, outPaint);
        }
        canvas.restore();
    }

    /*
    * 绘制刻度线
     *
     * @param canvas
     */
    private void drawLine2(Canvas canvas) {
        int radius = mWidth / 2 - kDantance;
        canvas.save();
        float startX = (int) (radius * 0.9);
        float inStartX = (int) (radius * 0.95);
        float endX = radius;
        int n = 13 * 5 + 14;
        float nuber = 180 / n;

        float angle = 12 * nuber / n + nuber;

        for (int i = 0; i < n; i++) {
            if (i % 6 == 0) {
                canvas.drawLine(startX, 0, endX, 0, kPaint);
            } else {
                canvas.drawLine(inStartX, 0, endX, 0, kPaint);
            }
            canvas.rotate(-angle);
//                canvas.drawLine(startX,0, endX, 0, outPaint);
        }
        canvas.restore();
    }


    /**
     * 动画开始
     */
    public void startAnim() {
        Log.e("current start", currentAngle + "what");
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (currentAngle == 0) {
                    Log.e("current", currentAngle + "旋转为0");
                }
                Log.e("current before:", getCurrentAngle() + "");
                Log.e("current value:", animation.getAnimatedValue() + "");
                angle = getCurrentAngle() * (float) animation.getAnimatedValue();
                Log.e("current", currentAngle + "");
                invalidate();
            }
        });
        valueAnimator.setInterpolator(new SpringInterpolator());
        valueAnimator.start();

      /*
        ValueAnimator colorsAnim = ValueAnimator.ofArgb(mColors);
        colorsAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.e("Colors", animation.getAnimatedValue() + "");
//                whitePaint.setColor((int) animation.getAnimatedValue());
//                invalidate();
            }
        });
        colorsAnim.setDuration(2000);
        colorsAnim.setInterpolator(new LinearInterpolator());
        colorsAnim.start();*/
    }


    public float getDptoSp(float size) {
        float density = getResources().getDisplayMetrics().density;

        return 0;
    }

}
