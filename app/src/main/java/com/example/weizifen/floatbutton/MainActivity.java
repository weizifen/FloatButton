package com.example.weizifen.floatbutton;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.weizifen.floatbutton.Service.FloatBallService;
import com.example.weizifen.floatbutton.Util.AccessibilityUtil;

public class MainActivity extends AppCompatActivity {

    private Button mBtnStart;
    private Button mBtnQuit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ball_view);
        initView();
        if (Build.VERSION.SDK_INT>23)
        {
            if (!Settings.canDrawOverlays(this))
            {
                Intent intent=new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                /*栈内复用模式,(activity有四种模式,P27  --安卓开发艺术探索)*/
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent,1);
                Toast.makeText(this, "Please allow FloatBall for top", Toast.LENGTH_SHORT).show();



            }

        }
    }


    /*
    * 初始化
    * */
    private void initView()
    {
        mBtnStart=(Button)findViewById(R.id.btn_start);
        mBtnQuit=(Button)findViewById(R.id.btn_quit);
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAccessibility();
                Intent intent=new Intent(MainActivity.this, FloatBallService.class);
                Bundle data=new Bundle();
                data.putInt("type",FloatBallService.TYPE_ADD);
                intent.putExtras(data);
                startService(intent);
            }
        });
        mBtnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, FloatBallService.class);
                Bundle data=new Bundle();
                data.putInt("type",FloatBallService.TYPE_DEL);
                intent.putExtras(data);
                startService(intent);
            }
        });


    }


    /*
    * 判断是否开启辅助功能
    * */
    private void checkAccessibility(){

        if (!AccessibilityUtil.isAccessibilitySettingsOn(this))
        {
            Intent startAccessibility=new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(startAccessibility);
            Toast.makeText(this,"请开启悬浮球辅助功能",Toast.LENGTH_SHORT).show();
        }
    }
}
