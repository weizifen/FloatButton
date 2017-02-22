package com.example.weizifen.floatbutton;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.utils.Utils;
import com.example.weizifen.floatbutton.Other.FinalInfo;
import com.example.weizifen.floatbutton.Util.Flash;
import com.example.weizifen.floatbutton.Util.LockUtil;
import com.example.weizifen.floatbutton.Util.ShotUtil;


/**
 * Created by weizifen on 17/2/15.
 */

public class FloatBigWindow extends LinearLayout {
    /**
     * 记录大悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录大悬浮窗的高度
     */
    public static int viewHeight;
        /*=====================截图相关================*/

    /*闪光灯开关*/
    private boolean choose;



    private Button One;
    private Button  Two;
    private Button Three;
    private static final String TAG = "FloatBigWindow";
    public FloatBigWindow(final Context context) {
        super(context);
        Utils.init(context);

        LayoutInflater.from(context).inflate(R.layout.big_window,this);
        View view=findViewById(R.id.float_big_windos);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        One=(Button)findViewById(R.id.ONE);

        Two=(Button)findViewById(R.id.two);

        Three=(Button)findViewById(R.id.Three);

        One.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                LockUtil.lockScreen(MainActivity.instance);
                FloatWindowManager.removeBigWindow(context);

            }
        });

        Two.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ShotUtil.requestCapturePermission(MainActivity.instance);
                FloatWindowManager.removeBigWindow(context);
                Intent intent=new Intent(FinalInfo.JIETU);
                context.sendBroadcast(intent);


            }
        });
        choose=true;



        /*=================================闪光灯=====================================*/
        final Flash flash=new Flash();

        Three.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                if (choose){
                    flash.open();
                    flash.on();
                    choose=false;
                }else if (choose==false){
                    flash.off();
                    flash.close();
                    choose=true;

                }
            }
        });

    }
            /*=================================闪光灯=====================================*/




}
