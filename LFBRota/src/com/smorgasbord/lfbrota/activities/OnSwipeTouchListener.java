package com.smorgasbord.lfbrota.activities;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class OnSwipeTouchListener extends SimpleOnGestureListener {

    static final String logTag = "OnSwipeTouchListener";

    static final int MIN_DISTANCE = 100;
    private float downX, downY, upX, upY;

    protected MotionEvent mLastOnDownEvent = null;

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    @Override
    public boolean onDown(MotionEvent e) {
        mLastOnDownEvent = e;
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = true;
        try {

            if (e1 == null)
                e1 = mLastOnDownEvent;
            if (e1 == null || e2 == null)
                return false;

            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight();
                        result = false;
                    } else {
                        onSwipeLeft();
                        result = false;
                    }
                }
            } else {
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                        result = false;
                    } else {
                        onSwipeTop();
                        result = false;
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }

}
