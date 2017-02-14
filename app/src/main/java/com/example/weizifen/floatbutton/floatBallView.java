package com.example.weizifen.floatbutton;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.weizifen.floatbutton.Util.AccessibilityUtil;

import java.lang.reflect.Field;

/**
 * Created by weizifen on 17/2/14.
 */

public class floatBallView extends LinearLayout {
    private ImageView mImgBall;
    private ImageView mImgBigBall;
    private ImageView mImgBg;
    private AccessibilityService mService;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;

    /*震动*/
    private Vibrator vibrator;
    private long[] mPattern = {0, 100};
    /*触摸倾斜*/

    private int mCurrentMode;

    private final static int MODE_NONE = 0x000;
    private final static int MODE_DOWN = 0x001;
    private final static int MODE_UP = 0x002;
    private final static int MODE_LEFT = 0x003;
    private final static int MODE_RIGHT = 0x004;
    private final static int MODE_MOVE = 0x005;
    private final static int MODE_GONE = 0x006;

    private int mStatusBarHeight;


    private final static int OFFSET = 30;

    private float mBigBallX;
    private float mBigBallY;

    private int mOffsetToParent;
    private int mOffsetToParentY;


    private boolean mIsLongTouch;

    private boolean mIsTouching;

    private float mTouchSlop;
    private final static long LONG_CLICK_LIMIT = 300;
    private final static long REMOVE_LIMIT = 1500;
    private final static long CLICK_LIMIT = 200;


    private long mLastDownTime;
    private float mLastDownX;
    private float mLastDownY;





    public floatBallView(Context context) {
        super(context);
        mService=(AccessibilityService)context;
        vibrator=(Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
        mWindowManager=(WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            initView();
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        inflate(getContext(), R.layout.ball, this);
        mImgBall = (ImageView) findViewById(R.id.img_ball);
        mImgBigBall = (ImageView) findViewById(R.id.img_big_ball);
        mImgBg = (ImageView) findViewById(R.id.img_bg);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mCurrentMode = MODE_NONE;

        mStatusBarHeight = getStatusBarHeight();
        mOffsetToParent = dip2px(25);
        mOffsetToParentY = mStatusBarHeight + mOffsetToParent;
        mImgBigBall.post(new Runnable() {
            @Override
            public void run() {
                mBigBallX = mImgBigBall.getX();
                mBigBallY = mImgBigBall.getY();
            }
        });
        
        
        
        

        mImgBg.setOnTouchListener(new OnTouchListener() {
            private static final String TAG = "floatBallView";
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    /*单击*/
                    case MotionEvent.ACTION_DOWN:
                        mIsTouching = true;
                        mImgBall.setVisibility(INVISIBLE);
                        mImgBigBall.setVisibility(VISIBLE);
                        mLastDownTime = System.currentTimeMillis();
                        mLastDownX = motionEvent.getX();
                        mLastDownY = motionEvent.getY();
                        Log.d(TAG, "onTouch: "+mLastDownTime+"       "+mLastDownX+"       "+mLastDownY);
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                /*长按执行的东西*/
                                if (isLongTouch()) {
                                    mIsLongTouch = true;
                                    vibrator.vibrate(mPattern, -1);
                                }
                            }
                        }, LONG_CLICK_LIMIT);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!mIsLongTouch && isTouchSlop(motionEvent)) {
                            return true;
                        }
                        if (mIsLongTouch && (mCurrentMode == MODE_NONE || mCurrentMode == MODE_MOVE)) {
                            /*1111*/
                            mLayoutParams.x = (int) (motionEvent.getRawX() - mOffsetToParent);
                            mLayoutParams.y = (int) (motionEvent.getRawY() - mOffsetToParentY);
                            mWindowManager.updateViewLayout(floatBallView.this, mLayoutParams);
                            mBigBallX = mImgBigBall.getX();
                            mBigBallY = mImgBigBall.getY();
                            mCurrentMode = MODE_MOVE;
                        } else {
                            doGesture(motionEvent);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        mIsTouching = false;
                        if (mIsLongTouch) {
                            mIsLongTouch = false;
                        } else if (isClick(motionEvent)) {
                            AccessibilityUtil.doBack(mService);
                        } else {
                            doUp();
                        }
                        mImgBall.setVisibility(VISIBLE);
                        mImgBigBall.setVisibility(INVISIBLE);
                        mCurrentMode = MODE_NONE;
                        break;
                }
                return true;
            }
        });



//        mImgBg.setOnClickListener(new OnClickListener() {
//            private static final String TAG = "floatBallView";
//            @Override
//            public void onClick(View view) {
//
//                Log.d(TAG, "点击了");
//                AccessibilityUtil.doBack(mService);
//            }
//        });
    }

    public void setLayoutParams(WindowManager.LayoutParams params) {
        mLayoutParams = params;
    }

        /**
         * 手指抬起后，根据当前模式触发对应功能
         */
    private void doUp() {
        switch (mCurrentMode) {
            case MODE_LEFT:
            case MODE_RIGHT:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    AccessibilityUtil.doLeftOrRightSlide(mService);
                }
                break;
            case MODE_DOWN:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    AccessibilityUtil.doPullDown(mService);
                }
                break;
            case MODE_UP:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    AccessibilityUtil.doUpSlide(mService);
                }
                break;


        }
        mImgBigBall.setX(mBigBallX);
        mImgBigBall.setY(mBigBallY);
    }









    /**
     * 判断是否为单击
     *
     */
    private boolean isClick(MotionEvent event) {
        float offsetX = Math.abs(event.getX() - mLastDownX);
        float offsetY = Math.abs(event.getY() - mLastDownY);
        long time = System.currentTimeMillis() - mLastDownTime;

        if (offsetX < mTouchSlop * 2 && offsetY < mTouchSlop * 2 && time < CLICK_LIMIT) {
            return true;
        } else {
            return false;
        }
    }

      /*方向手势上下左右*/
    private void doGesture(MotionEvent motionEvent)
    {
        float offsetX = motionEvent.getX() - mLastDownX;
        float offsetY = motionEvent.getY() - mLastDownY;

        if (Math.abs(offsetX) < mTouchSlop && Math.abs(offsetY) < mTouchSlop) {
            return;
        }
        if (Math.abs(offsetX) > Math.abs(offsetY)) {
            if (offsetX > 0) {
                if (mCurrentMode == MODE_RIGHT) {
                    return;
                }
                mCurrentMode = MODE_RIGHT;
                mImgBigBall.setX(mBigBallX + OFFSET);
                mImgBigBall.setY(mBigBallY);
            } else {
                if (mCurrentMode == MODE_LEFT) {
                    return;
                }
                mCurrentMode = MODE_LEFT;
                mImgBigBall.setX(mBigBallX - OFFSET);
                mImgBigBall.setY(mBigBallY);
            }
        } else {
            if (offsetY > 0) {
                if (mCurrentMode == MODE_DOWN || mCurrentMode == MODE_GONE) {
                    return;
                }
                mCurrentMode = MODE_DOWN;
                mImgBigBall.setX(mBigBallX);
                mImgBigBall.setY(mBigBallY + OFFSET);
                //如果长时间保持下拉状态，将会触发移除悬浮球功能
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mCurrentMode == MODE_DOWN && mIsTouching) {
                            toRemove();
                            mCurrentMode = MODE_GONE;
                        }
                    }
                }, REMOVE_LIMIT);
            } else {
                if (mCurrentMode == MODE_UP) {
                    return;
                }
                mCurrentMode = MODE_UP;
                mImgBigBall.setX(mBigBallX);
                mImgBigBall.setY(mBigBallY - OFFSET);
            }
        }

    }

    /**
     * 移除悬浮球----->
     */
    private void toRemove() {
        vibrator.vibrate(mPattern, -1);
        FloatWindowManager.removeBallView(getContext());
    }


    /**
     * 判断是否是轻微滑动
     *
     */
    private boolean isTouchSlop(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (Math.abs(x - mLastDownX) < mTouchSlop && Math.abs(y - mLastDownY) < mTouchSlop) {
            return true;
        }
        return false;
    }

    /*判断点击事件是否为长点击*/
    private boolean isLongTouch() {
        long time = System.currentTimeMillis();
        if (mIsTouching && mCurrentMode == MODE_NONE && (time - mLastDownTime >= LONG_CLICK_LIMIT)) {
            return true;
        }
        return false;
    }


  /*获取状态栏高度*/
    private int getStatusBarHeight(){
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }





    /*将dip(dp)转化成px*/
    public int dip2px(float dip) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dip, getContext().getResources().getDisplayMetrics()
        );
    }

}
