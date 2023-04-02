package com.xxzz.curriculum.read;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;


public class PagerOnTouch implements View.OnTouchListener {
    private int startX;
    private int startY;
    TouchCallback touchCallback;
    public PagerOnTouch(TouchCallback touchCallback) {
        this.touchCallback = touchCallback;
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                startY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int endX = (int) event.getX();
                int endY = (int) event.getY();
                touchCallback.onClick(
                        Math.abs(endX - startX) < 50
                                && Math.abs(endY - startY) < 50);
                break;
        }
        return false;
    }

    public interface TouchCallback {
        public default void onClick(boolean isClick) {
        }
    }
}
