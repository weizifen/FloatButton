package com.example.weizifen.floatbutton.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.weizifen.floatbutton.MyApplication;
import com.example.weizifen.floatbutton.Other.FinalInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by baidu_lishuang10 on 16/3/21.
 */
public class CaptureService extends Service {

    private static final String TAG = "CService";

    private MediaProjectionManager mMpmngr;
    private MediaProjection mMpj;
    private ImageReader mImageReader;
    private String mImageName;
    private String mImagePath;
    private int screenDensity;
    private int windowWidth;
    private int windowHeight;
    private VirtualDisplay mVirtualDisplay;
    private WindowManager wm;
    private JietuReceiver receiver;





    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createEnvironment();
        registerReceiver();
    }
/*-------------创造环境---------------*/
    private void createEnvironment() {
        mImagePath = Environment.getExternalStorageDirectory().getPath() + "/screenshort/";
        Log.d(TAG, mImagePath);
        mMpmngr = ((MyApplication) getApplication()).getMpmngr();
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowWidth = wm.getDefaultDisplay().getWidth();
        windowHeight = wm.getDefaultDisplay().getHeight();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        screenDensity = displayMetrics.densityDpi;
        mImageReader = ImageReader.newInstance(windowWidth, windowHeight, 0x1, 2);

    }

    /*广播注册者*/
    private void registerReceiver(){
        receiver=new JietuReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(FinalInfo.JIETU);
        registerReceiver(receiver,filter);
    }


    /*----------广播------------*/
    class JietuReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(FinalInfo.JIETU)){
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "start startVirtual");
                        startVirtual();
                    }
                }, 500);
                // Handler handler1 = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "start startCapture");
                        startCapture();
                    }
                }, 1000);
                // Handler handler2 = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "start stopVirtual");
                        stopVirtual();
                    }
                }, 1500);
            }
        }
    }



    /*-----------------------------------------------------*/


    private void stopVirtual() {
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
            mVirtualDisplay = null;
        }
    }

    private void startCapture() {
        mImageName = System.currentTimeMillis() + ".png";
        Log.e(TAG, "image name is : " + mImageName);
        Image image = mImageReader.acquireLatestImage();
        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        image.close();

        if (bitmap != null) {
            Log.e(TAG, "bitmap  create success ");
            try {
                File fileFolder = new File(mImagePath);
                if (!fileFolder.exists())
                    fileFolder.mkdirs();
                File file = new File(mImagePath, mImageName);
                if (!file.exists()) {
                    Log.e(TAG, "file create success ");
                    file.createNewFile();
                }
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                Log.e(TAG, "file save success ");
                Toast.makeText(this.getApplicationContext(), "截图成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                sendBroadcast(intent);
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
            }
        }
    }

    private void startVirtual() {
        if (mMpj != null) {
            virtualDisplay();
        } else {
            setUpMediaProjection();
            virtualDisplay();
        }
    }

    private void setUpMediaProjection() {
        int resultCode = ((MyApplication) getApplication()).getResultCode();
        Intent data = ((MyApplication) getApplication()).getResultIntent();
        mMpj = mMpmngr.getMediaProjection(resultCode, data);
    }

    private void virtualDisplay() {
        mVirtualDisplay = mMpj.createVirtualDisplay("capture_screen", windowWidth, windowHeight, screenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mImageReader.getSurface(), null, null);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMpj != null) {
            mMpj.stop();
            mMpj = null;


            unregisterReceiver(receiver);
        }
    }
}
