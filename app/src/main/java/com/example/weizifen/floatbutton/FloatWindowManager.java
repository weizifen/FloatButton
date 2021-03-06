package com.example.weizifen.floatbutton;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.blankj.utilcode.utils.ScreenUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by weizifen on 17/2/14.
 */

public class FloatWindowManager {
    private static WindowManager mWindowManager;
    /**
     * 小悬浮球View的实例
     */
    private static floatBallView mBallView;

    /**
     * 大悬浮窗View的实例
     */
    private static FloatBigWindow bigWindow;
    /**
     * 大悬浮窗View的参数
     */
    private static WindowManager.LayoutParams bigWindowParams;




//
//
//    /**
//     * PagerView的实例
//     */
//    private static PagerViewWindow pagerViewWindowWindow;
//    /**
//     * PagerView的参数
//     */
//    private static WindowManager.LayoutParams pagerViewWindowParams;







    /*添加ADDVIEW*/
    public static void addView(Context context)
    {
        if (mBallView==null)
        {
            WindowManager windowManager=getWindowManager(context);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager(context).getDefaultDisplay().getMetrics(dm);
            int screenWidth=dm.widthPixels;
            int screenHeight=dm.heightPixels;
//            int screenWidth = windowManager.getDefaultDisplay().getWidth();
//            int screenHeight = windowManager.getDefaultDisplay().getHeight();
            mBallView=new floatBallView(context);
            WindowManager.LayoutParams params=new WindowManager.LayoutParams();


            /*
            * type值用于确定悬浮窗的类型，一般设为2002，表示在所有应用程序之上，但在状态栏之下。
            *flags值用于确定悬浮窗的行为，比如说不可聚焦，非模态对话框等等，属性非常多，大家可以查看文档。
            *gravity值用于确定悬浮窗的对齐方式，一般设为左上角对齐，这样当拖动悬浮窗的时候方便计算坐标。
            *x值用于确定悬浮窗的位置，如果要横向移动悬浮窗，就需要改变这个值。
            *y值用于确定悬浮窗的位置，如果要纵向移动悬浮窗，就需要改变这个值。
            *width值用于指定悬浮窗的宽度。
            *height值用于指定悬浮窗的高度。
            */
            params.x=screenWidth;
            params.y=screenHeight/2;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.LEFT | Gravity.TOP;
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
            params.format = PixelFormat.RGBA_8888;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            mBallView.setLayoutParams(params);
            windowManager.addView(mBallView, params);

        }


    }



    /*移除悬浮小球*/
    public static void removeBallView(Context context)
    {
        if (mBallView!=null)
        {
            WindowManager windowManager=getWindowManager(context);
            windowManager.removeView(mBallView);
            mBallView=null;
        }

    }
    /**
     * 创建一个大悬浮窗。位置为屏幕正中间。
     */
    public static void createBigWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager(context).getDefaultDisplay().getMetrics(dm);
        int screenWidth=dm.widthPixels;
        int screenHeight=dm.heightPixels;
        if (bigWindow == null) {
            bigWindow = new FloatBigWindow(context);
            if (bigWindowParams == null) {
                bigWindowParams = new WindowManager.LayoutParams();
                bigWindowParams.x = screenWidth / 2
                        - FloatBigWindow.viewWidth / 2;
                bigWindowParams.y = screenHeight / 2
                        - FloatBigWindow.viewHeight / 2;
                bigWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                bigWindowParams.format = PixelFormat.RGBA_8888;
                bigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                bigWindowParams.width = FloatBigWindow.viewWidth;
                bigWindowParams.height = FloatBigWindow.viewHeight;
            }
            windowManager.addView(bigWindow, bigWindowParams);
        }
    }

    /**
     * 移除一个大悬浮窗。位置为屏幕正中间。
     */
    public static void removeBigWindow(Context context)
    {
        if (bigWindow!=null)
        {
            WindowManager windowManager=getWindowManager(context);
            windowManager.removeView(bigWindow);
            bigWindow=null;
        }
    }
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }



//    /**
//     * 创建一个PagerView悬浮窗。位置为屏幕正中间。
//     */
//    public static void createPagerView(Context context) {
//        WindowManager windowManager = getWindowManager(context);
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager(context).getDefaultDisplay().getMetrics(dm);
//        int screenWidth=dm.widthPixels;
//        int screenHeight=dm.heightPixels;
//        if (pagerViewWindowWindow == null) {
//            pagerViewWindowWindow = new PagerViewWindow(context);
//            if (pagerViewWindowParams == null) {
//                pagerViewWindowParams = new WindowManager.LayoutParams();
//                pagerViewWindowParams.x = screenWidth / 2
//                        - PagerViewWindow.viewWidth / 2;
//                pagerViewWindowParams.y = screenHeight / 2
//                        - PagerViewWindow.viewHeight / 2;
//                pagerViewWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
//                pagerViewWindowParams.format = PixelFormat.RGBA_8888;
//                pagerViewWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
//                pagerViewWindowParams.width = PagerViewWindow.viewWidth;
//                pagerViewWindowParams.height = PagerViewWindow.viewHeight;
//            }
//            windowManager.addView(pagerViewWindowWindow, pagerViewWindowParams);
//        }
//    }
}
