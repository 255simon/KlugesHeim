package com.example.klugesheim;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OnSwipeTouchListener implements View.OnTouchListener {
    private final GestureDetector gestureDetector ;

    public OnSwipeTouchListener(Context con) {
        this.gestureDetector = new GestureDetector(con, new GestureListener());
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener{

        private final int swipeThreshold = 75;
        private final int swipeVelocityThreshold = 75;

        @Override
        public boolean onDown(MotionEvent event){
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
            boolean result = false;
            try{
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if(Math.abs(diffX) > Math.abs(diffY)){
                    if(Math.abs(diffX) > swipeThreshold && Math.abs(velocityX) > swipeVelocityThreshold){
                        if(diffX > 0){
                            onSwipeRight();
                        }
                        else{
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
                else if(Math.abs(diffY) > swipeThreshold && velocityY > swipeVelocityThreshold){
                    if(diffY > 0){
                        onSwipeBottom();
                    }
                    else{
                        onSwipeTop();
                    }
                    result = true;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight(){}

    public void onSwipeLeft(){}

    public void onSwipeBottom(){}

    public void onSwipeTop(){}
}
