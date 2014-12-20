package com.android.threeday.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by user on 2014/12/17.
 */
public class LockView extends FrameLayout{
    private static final int ROWS = 3;
    private static final int COLUMNS = 3;
    private static final int NUM = ROWS * COLUMNS;

    private LockViewDrawLayer mLockViewDrawLayer;
    private View[] mNumberViews;
    private Point[] mNumberPointCenters;
    private ArrayList<Integer> mLockNumbers = new ArrayList<Integer>(NUM);
    private OnLockListener mOnLockListener;

    private float mNumberSureRadioSquare;
    private float mDensity = 1f;

    public LockView(Context context) {
        super(context);
        initView( );
    }
     public LockView(Context context, AttributeSet attrs) {
        super(context, attrs);
         initView( );
    }

    public LockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView( );
    }

    public LockView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView( );
    }

    public void setDensity(float density){
        this.mDensity = density;
        this.mLockViewDrawLayer.setMoveEdgePix(this.mLockViewDrawLayer.getMoveEdgePix( ) * density);
    }

    private void initNumberPoints( ){
        float width = getWidth() / ROWS;
        float height = getHeight() / COLUMNS;
        float xRadio = width / 2;
        float yRadio = height / 2;
        this.mNumberPointCenters = new Point[NUM];
        float x;
        float y;
        int index;
        for(int i = 0; i < ROWS; i++){
            y = height * (i + 1) - yRadio;
            for(int j = 0; j < COLUMNS; j++){
                x = width * (j + 1) - xRadio;
                index = i * ROWS + j;
                this.mNumberPointCenters[index] = new Point(x, y, index);
            }
        }
    }

    public void setOnLockListener(OnLockListener onLockListener){
        this.mOnLockListener = onLockListener;
    }

    private void initView( ){
        Context context = getContext();
        LinearLayout numbersContainer = new LinearLayout(context);
        numbersContainer.setOrientation(LinearLayout.VERTICAL);
        this.mNumberViews = new View[ROWS * COLUMNS];
        LinearLayout linearLayout;
        int index;
        for(int i = 0; i < ROWS; i++){
            linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            for(int j = 0; j < COLUMNS; j++){
                index = ROWS * i + j;
                this.mNumberViews[index] = new View(context);
                linearLayout.addView(this.mNumberViews[index], new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            }
            numbersContainer.addView(linearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        }
        addView(numbersContainer, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        this.mLockViewDrawLayer = new LockViewDrawLayer(context);
        addView(this.mLockViewDrawLayer, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void clear( ){
        clearLockNumbers();
        this.mLockViewDrawLayer.clear( );
    }

    private void addNumber(int number){
        this.mLockNumbers.add(number);
    }

    private boolean isLockStart( ){
        return !this.mLockNumbers.isEmpty();
    }

    private boolean isNumberExit(int number){
        return this.mLockNumbers.contains(number);
    }

    private void clearLockNumbers( ){
        this.mLockNumbers.clear();
    }

    public void setNumberViewBackgroundResource(int resId){
        for(View view : this.mNumberViews){
            view.setBackgroundResource(resId);
        }
    }

    public void setDrawLineColor(int color){
        this.mLockViewDrawLayer.setLineColor(color);
    }

    public void setNumberViewMargin(int margin){
        LinearLayout.LayoutParams layoutParams;
        for(View view : this.mNumberViews){
            layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            layoutParams.setMargins(margin, margin, margin, margin);
        }
    }

    private void setLineColor(int color){
        this.mLockViewDrawLayer.setLineColor(color);
    }

    private Point getSureNumberPoint(float x, float y){
        float disX;
        float disY;
        for(Point point : this.mNumberPointCenters){
            disX = point.mX - x;
            disY = point.mY - y;
            if((disX * disX + disY * disY) < this.mNumberSureRadioSquare){
                return point;
            }
        }
        return null;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(this.mNumberPointCenters == null){
            initNumberPoints( );
            float numberWidth = this.mNumberViews[0].getWidth();
            float value =  numberWidth / 6;
            this.mNumberSureRadioSquare = value * value;
            this.mLockViewDrawLayer.setNumberPointRadio(numberWidth / 8);
            this.mLockViewDrawLayer.setLineWidth(numberWidth / 15);
        }
        return false;
    }

    private class LockViewDrawLayer extends View{
        private static final int DEFAULT_COLOR = Color.BLACK;

        private Path mPath;
        private Path mLinePath;
        private Paint mPaint;
        private float mCurrentNumberX;
        private float mCurrentNumberY;
        private int mCurrentNumber;
        private float mMoveX;
        private float mMoveY;
        private float mRadio;
        private float mMoveEdgePix = 5f;

        public LockViewDrawLayer(Context context) {
            super(context);
            initData( );
        }

        private void initData( ){
            this.mPath = new Path();
            this.mLinePath = new Path();
            this.mPaint = new Paint();
            this.mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            this.mPaint.setAntiAlias(true);
            this.mPaint.setDither(true);
            setLineColor(DEFAULT_COLOR);
        }

        private float getMoveEdgePix( ){
            return this.mMoveEdgePix;
        }

        private void setMoveEdgePix(float value){
            this.mMoveEdgePix = value;
        }

        private void setLineColor(int color){
            this.mPaint.setColor(color);
        }

        private void setLineWidth(float width){
            this.mPaint.setStrokeWidth(width);
        }

        private void setNumberPointRadio(float radio){
            this.mRadio = radio;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(this.mPath, this.mPaint);
            canvas.drawPath(this.mLinePath, this.mPaint);
        }

        private void addSureNumber(float x, float y, int number){
            this.mCurrentNumberX = x;
            this.mCurrentNumberY = y;
            this.mCurrentNumber = number;
            this.mPath.moveTo(x, y);
            this.mPath.addCircle(x, y, this.mRadio, Path.Direction.CW);

            LockView.this.addNumber(number);
        }

        private void onActionEnd( ){
            this.mLinePath.reset();
            invalidate();
            if(LockView.this.mOnLockListener != null){
                LockView.this.mOnLockListener.onLockSet(mLockNumbers);
            }
        }

        private void clear( ){
            this.mPath.reset();
            this.mLinePath.reset();
            invalidate();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    clearLockNumbers();
                    float x = event.getX();
                    float y = event.getY();
                    this.mMoveX = x;
                    this.mMoveY = y;
                    this.mPath.reset();
                    this.mLinePath.reset();
                    Point point = getSureNumberPoint(x, y);
                    if(point != null){
                        addSureNumber(point.mX, point.mY, point.mPosition);
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    float moveX = event.getX();
                    float moveY = event.getY();
                    if(Math.abs(moveX - this.mMoveX) > this.mMoveEdgePix || Math.abs(moveY - this.mMoveY) > this.mMoveEdgePix){
                        this.mMoveX = moveX;
                        this.mMoveY = moveY;
                        Point movePoint = getSureNumberPoint(moveX, moveY);
                        if(movePoint != null && !isNumberExit(movePoint.mPosition)){
                            if(LockView.this.isLockStart()){
                                this.mPath.moveTo(this.mCurrentNumberX, this.mCurrentNumberY);
                                this.mPath.lineTo(movePoint.mX, movePoint.mY);
                            }
                            addSureNumber(movePoint.mX, movePoint.mY, movePoint.mPosition);
                            this.mLinePath.reset();
                        }else{
                            if(LockView.this.isLockStart()){
                                this.mLinePath.reset();
                                this.mLinePath.moveTo(this.mCurrentNumberX, this.mCurrentNumberY);
                                this.mLinePath.lineTo(moveX, moveY);
                            }
                        }
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    onActionEnd();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    onActionEnd();
                    break;
            }
            return true;
        }
    }

    private class Point{
        private float mX;
        private float mY;
        private int mPosition;

        private Point(float x, float y, int position){
            this.mX = x;
            this.mY = y;
            this.mPosition = position;
        }

    }

    public interface OnLockListener{
        public void onLockSet(ArrayList<Integer> lockNumbers);
    }
}
