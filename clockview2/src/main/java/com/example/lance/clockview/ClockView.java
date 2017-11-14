package com.example.lance.clockview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Lance on 2017/11/13.
 */

public class ClockView extends View {
    private Context mContext;
    private Paint mPaint;

    public ClockView(Context context) {
        super(context);
        mContext = context;
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context,attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureLength(widthMeasureSpec),measureLength(heightMeasureSpec));
    }

    private int measureLength(int measureSpec) {
        int result;
        int specSize = MeasureSpec.getSize(measureSpec);
        int specMode = MeasureSpec.getMode(measureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else{
            result = 300;
            if(specMode == MeasureSpec.AT_MOST){
                result = Math.min(result,specSize);
            }
        }
        return result;
    }

    private float mSecondDegree = 0;//秒针的度数;
    private float mMinDegree = 0;   //分针的度数;
    private float mHourDegree = 0;  //时针的度数;

    private Timer mTimer = new Timer();

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            //具体的定时任务逻辑;
            if(mSecondDegree == 360){
                mSecondDegree = 0;
            }
            if(mMinDegree == 360){
                mMinDegree = 0;
            }
            if(mHourDegree == 360){
                mHourDegree = 0;
            }

            mSecondDegree = mSecondDegree + 6;
            mMinDegree = mMinDegree + 0.1f;
            mHourDegree = mHourDegree + 1.0f/240;
            /**
             * 这个方法用来执行onDraw（）方法让画布重绘;
             * invalidate()也会执行onDraw（）方法；
             * 两者的区别：
             * invalidate要在主线程中调用，而postInvalidate要在子线程中调用的；
             * 开启一个定时器，相当于开启了一个子线程，所以调用postInvalidate方法;*/
            postInvalidate();
        }
    };

    /**
     * 开启定时器
     * */
    public void start(){
        mTimer.schedule(task,0,1000);
    }

    /**
     * 设置时间;
     * */
    private boolean mIsNight = false;
    public void setTime(int hour,int min,int second){
        if(hour >= 24 || hour < 0|| min >= 60|| min <0 || second >=60 || second<0 ){
            Toast.makeText(getContext(),"时间不合法", Toast.LENGTH_SHORT).show();
            return;
        }
        if(hour >= 12){
            mIsNight = true;
            mHourDegree = (hour + min * 1.0f/60f + second * 1.0f/3600f -12)*30f;
        }else{
            mIsNight = false;
            mHourDegree = (hour + min * 1.0f/60f + second * 1.0f/3600f)*30f;
        }
        mMinDegree = (min + second * 1.0f/60f)*6f;
        mSecondDegree = second * 6f;
        invalidate();
    }

    /**
     * 计算总共的秒数;
     * */
    private float mTotalSecond;
    public float getTimeTotalSecond(){
        if(mIsNight){
            mTotalSecond = mHourDegree * 120 +12 *3600;
            return mTotalSecond;
        }else{
            mTotalSecond = mHourDegree * 120;
            return mTotalSecond;
        }
    }

    /**
     * 获取小时；
     * */
    public int getHour(){
        return (int)(getTimeTotalSecond() / 3600);
    }

    /**
     * 获取分钟;
     * */
    public int getMin(){
        return (int)((getTimeTotalSecond() - getHour() * 3600)/60);
    }

    /**
     * 获取秒数；
     * */
    public int getSecond(){
        return (int)((getTimeTotalSecond() - getHour() * 3600 - getMin() * 60));
    }


    /**
     * 在xml中自定义的样式;
     * */
    //边框样式;
    private int type;
    private static final int STROKE = 0;    //边框;
    private static final int FILL = 1;      //表盘；
    //时钟边框的颜色;
    private int borderColor;
    //时钟边框大小；
    private int borderSize;
    //秒针的颜色;
    private int secondColor;
    //秒针的大小;
    private int secondSize;
    //分针的颜色；
    private int minColor;
    //分针的大小;
    private int minSize;
    //时针的颜色；
    private int hourColor;
    //时针的大小;
    private int hourSize;
    //刻度颜色;
    private int lineColor;

    private void init(Context context, AttributeSet attrs){
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ClockView);
        type = ta.getInt(R.styleable.ClockView_borderType,STROKE);
        borderColor = ta.getColor(R.styleable.ClockView_borderColor, Color.BLACK);
        borderSize = ta.getInt(R.styleable.ClockView_borderSize,2);
        secondColor = ta.getColor(R.styleable.ClockView_secondColor, Color.RED);
        secondSize = ta.getInt(R.styleable.ClockView_secondSize,2);
        minColor = ta.getColor(R.styleable.ClockView_minColor, Color.BLACK);
        minSize = ta.getInt(R.styleable.ClockView_minSize,4);
        hourColor = ta.getColor(R.styleable.ClockView_hourColor, Color.BLACK);
        hourSize = ta.getInt(R.styleable.ClockView_hourSize,7);
        lineColor = ta.getInt(R.styleable.ClockView_lineColor, Color.BLACK);
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画具体内容;
        mPaint = new Paint();
        //抗锯齿;
        mPaint.setAntiAlias(true);
        if(type == 0){
            mPaint.setStyle(Paint.Style.STROKE);
        }else if(type == 1){
            mPaint.setStyle(Paint.Style.FILL);
        }

        //画边框；
        mPaint.setColor(borderColor);
        mPaint.setStrokeWidth(borderSize);
        canvas.drawCircle(getWidth()/2,getHeight()/2,getWidth()/3,mPaint);

        //画中心点;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(5);
        canvas.drawPoint(getWidth()/2,getHeight()/2,mPaint);

        //画刻度线;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(lineColor);
        mPaint.setStrokeWidth(1);
        canvas.translate(getWidth()/2,getHeight()/2);
        for(int i = 0;i<360;i++){
            if(i % 30 == 0){
                canvas.drawLine(getWidth()/3 - 25,0,getWidth()/3,0,mPaint);
            }else if(i % 6 == 0){
                canvas.drawLine(getWidth()/3 - 14,0,getWidth()/3,0,mPaint);
            }else{
                canvas.drawLine(getWidth()/3 - 9,0,getWidth()/3,0,mPaint);
            }
            canvas.rotate(1);
        }

        canvas.save();
        //画刻度;
        mPaint.setTextSize(25);
        mPaint.setStyle(Paint.Style.STROKE);
        for(int i =0;i<12;i++){
            if(i == 0){
                drawNum(canvas,i*30,12+"",mPaint);
            }else{
                drawNum(canvas,i*30,i+"",mPaint);
            }
        }
        canvas.restore();

        //画秒针;
        canvas.save();
        mPaint.setColor(secondColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(secondSize);
        canvas.rotate(mSecondDegree);
        canvas.drawLine(0,0,0,-190,mPaint);
        canvas.restore();

        //画分针
        canvas.save();
        mPaint.setColor(minColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(minSize);
        canvas.rotate(mMinDegree);
        canvas.drawLine(0,0,0,-130,mPaint);
        canvas.restore();

        //画时针;
        canvas.save();
        mPaint.setColor(hourColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(hourSize);
        canvas.rotate(mHourDegree);
        canvas.drawLine(0,0,0,-90,mPaint);
        canvas.restore();
    }

    private void drawNum(Canvas canvas, int degree, String text, Paint paint) {

        Rect textBound = new Rect();
        paint.getTextBounds(text,0,text.length(),textBound);
        canvas.rotate(degree);
        canvas.translate(0,50-getWidth()/3);
        canvas.rotate(-degree);
        canvas.drawText(text,-textBound.width()/2,textBound.height()/2,paint);
        canvas.rotate(degree);
        canvas.translate(0,getWidth()/3-50);
        canvas.rotate(-degree);
    }
}
