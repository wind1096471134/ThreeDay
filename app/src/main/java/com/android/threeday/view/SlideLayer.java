package com.android.threeday.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by user on 2014/12/13.
 */
public class SlideLayer extends ViewPager{
    private View mMainView;
    private View mFirstView;
    private OnLayerSlideListener mOnLayerSlideListener;
    private PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if(position == 0){
                mFirstView = new View(getContext());
                mFirstView.setBackgroundColor(Color.WHITE);
                container.addView(mFirstView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return mFirstView;
            }else if(position == 1){
                container.addView(mMainView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return mMainView;
            }
            return super.instantiateItem(container, position);
        }
    };
    private SimpleOnPageChangeListener mOnPageChangeListener = new SimpleOnPageChangeListener(){
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(position == 0){
                float alpha = positionOffset < 0 ? 0 : (positionOffset > 1 ? 1 : positionOffset);
                mFirstView.setAlpha(alpha);
                if(alpha == 0){
                    setVisibility(GONE);
                    if(mOnLayerSlideListener != null){
                        mOnLayerSlideListener.onLayerSlide();
                    }
                }
            }
        }
    };

    public SlideLayer(Context context) {
        super(context);
        init( );
    }

    public SlideLayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init( );
    }

    private void init( ){
    }

    public void setMainView(View mainView){
        this.mMainView = mainView;
        setAdapter(this.mPagerAdapter);
        setOnPageChangeListener(this.mOnPageChangeListener);
        setCurrentItem(1, false);
    }

    public void setOnLayerSlideListener(OnLayerSlideListener onLayerSlideListener){
        this.mOnLayerSlideListener = onLayerSlideListener;
    }

    public interface OnLayerSlideListener{
        public void onLayerSlide( );
    }
}
