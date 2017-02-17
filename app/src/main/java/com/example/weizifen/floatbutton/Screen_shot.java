package com.example.weizifen.floatbutton;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by weizifen on 17/2/16.
 */



public class Screen_shot extends LinearLayout {
    /**
     * 记录截图悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录截图悬浮窗的高度
     */
    public static int viewHeight;
    public Screen_shot(final Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.screen_shot,this);
        View view=findViewById(R.id.shot);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        Toast.makeText(context,"蒙版",Toast.LENGTH_SHORT).show();


        /*截图*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                FloatWindowManager.GetandSaveCurrentImage(context,MainActivity.instance);
                FloatWindowManager.removeScreenWindow(context);

            }
        })      .start();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    saveToSD(myShot(MainActivity.instance), context.getFilesDir().getPath().toString(),"abc.png");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }finally {
//                    FloatWindowManager.removeScreenWindow(context);
//
//                }
//
//
//
//            }
//        }).start();



    }


    public Bitmap myShot(Activity activity) {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();

        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;
        Display display = activity.getWindowManager().getDefaultDisplay();

        // 获取屏幕宽和高
        int widths = display.getWidth();
        int heights = display.getHeight();

        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);

        // 去掉状态栏
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
                statusBarHeights, widths, heights - statusBarHeights);

        // 销毁缓存信息
        view.destroyDrawingCache();

        return bmp;
    }

    private static final String TAG = "Screen_shot";
    private void saveToSD(Bitmap bmp, String dirName,String fileName) throws IOException {
        // 判断sd卡是否存在
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File dir = new File(dirName);
            // 判断文件夹是否存在，不存在则创建
            if(!dir.exists()){
                dir.mkdir();
            }

            File file = new File(dirName + fileName);
            // 判断文件是否存在，不存在则创建
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                if (fos != null) {
                    // 第一参数是图片格式，第二个是图片质量，第三个是输出流
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    // 用完关闭
                    fos.flush();
                    fos.close();
                    Log.d(TAG, dirName);
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
