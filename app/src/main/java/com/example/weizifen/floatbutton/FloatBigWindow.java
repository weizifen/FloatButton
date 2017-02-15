package com.example.weizifen.floatbutton;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

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

    public FloatBigWindow(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.big_window,this);
        View view=findViewById(R.id.float_big_windos);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        One=(Button)findViewById(R.id.ONE);
        Back=(Button)findViewById(R.id.back);

        Back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                FloatWindowManager.removeBigWindow(getContext());
            }
        });
    }
}
