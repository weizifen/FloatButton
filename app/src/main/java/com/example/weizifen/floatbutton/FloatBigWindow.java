package com.example.weizifen.floatbutton;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.blankj.utilcode.utils.Utils;
import com.example.weizifen.floatbutton.Util.LockUtil;


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

    private Button One;
    private Button  Back;
    private static final String TAG = "FloatBigWindow";
    public FloatBigWindow(final Context context) {
        super(context);
        Utils.init(context);

        LayoutInflater.from(context).inflate(R.layout.big_window,this);
        View view=findViewById(R.id.float_big_windos);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        One=(Button)findViewById(R.id.ONE);

        Back=(Button)findViewById(R.id.back);
        One.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                LockUtil.lockScreen(MainActivity.instance);
                FloatWindowManager.removeBigWindow(context);

            }
        });

        Back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            FloatWindowManager.removeBigWindow(context);
//                Intent intent=new Intent(context,ScreenShot.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//
//                context.startActivity(intent);

                FloatWindowManager.createScreenWindow(context);
            }
        });
    }





    /*截图功能*/
    public static void shootLoacleView(Activity activity,Context context)
    {
//        FloatWindowManager.GetandSaveCurrentImage(context,activity);
    }





}
