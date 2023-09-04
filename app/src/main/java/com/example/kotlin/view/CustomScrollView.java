package com.example.kotlin.view;

/**
 * Description: <CustomScrollView><br>
 * Author:      mxdl<br>
 * Date:        2023/9/4<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class CustomScrollView extends FrameLayout {

    private Scroller scroller;
    private float lastY;
    private int offsetY;
    private int maxScrollY; // 最大可滚动距离
    private boolean scrollingEnabled = true; // 是否允许滚动

    public CustomScrollView(Context context) {
        super(context);
        init(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        scroller = new Scroller(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 计算最大可滚动距离，这里假设滚动到底部为最大
        maxScrollY = getChildAt(0).getHeight() - getHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!scrollingEnabled) {
            return super.onTouchEvent(event);
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float newY = event.getY();
                int deltaY = (int) (newY - lastY);
                scrollBy(0, -deltaY);
                lastY = newY;
                return true;
            case MotionEvent.ACTION_UP:
                // Use Scroller for smooth scrolling
                if (getScrollY() < 0) {
                    scroller.startScroll(0, getScrollY(), 0, -getScrollY());
                    invalidate();
                } else if (getScrollY() > maxScrollY) {
                    scroller.startScroll(0, getScrollY(), 0, maxScrollY - getScrollY());
                    invalidate();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(0, scroller.getCurrY());
            postInvalidate();
        }
    }

    // 禁止或允许滚动
    public void setScrollingEnabled(boolean enabled) {
        scrollingEnabled = enabled;
    }
}
