package com.example.weizifen.floatbutton;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.weizifen.floatbutton.Service.FloatBallService;
import com.example.weizifen.floatbutton.Service.FloatWindowsService;
import com.example.weizifen.floatbutton.Util.AccessibilityUtil;
import com.example.weizifen.floatbutton.Util.LockUtil;

public class MainActivity extends AppCompatActivity {
    public static Context context;
    public static Activity instance;
    private Button mBtnStart;
    private Button mBtnQuit;



    public static final int REQUEST_MEDIA_PROJECTION = 18;


    /*===========锁屏=========*/
//    private static final int MY_REQUEST_CODE = 9999;
//    private DevicePolicyManager policyManager;
//    private ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ball_view);
        initView();
        requestCapturePermission();



        if (instance==null)
        {
            instance =this;
        }
        if (context ==null)
        {
            context=this;
        }
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

    /*=========================截图相关=================================*/
//    private void DevicePolicyManager(){
//        //获取设备管理服务
//        policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//        //AdminReceiver 继承自 DeviceAdminReceiver
//        componentName = new ComponentName(this, AdminReceiver.class);
//
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MEDIA_PROJECTION && resultCode == RESULT_OK&& data != null)
        {
            FloatWindowsService.setResultData(data);
            startService(new Intent(getApplicationContext(), FloatWindowsService.class));

        }
        if (requestCode == LockUtil.MY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (!LockUtil.policyManager.isAdminActive(LockUtil.componentName)) {   //若无权限
            }
            else {
                LockUtil.policyManager.lockNow();//直接锁屏
            }
        } else {

        }
    }


    public void requestCapturePermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //5.0 之后才允许使用屏幕截图

            return;
        }

        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager)
               getSystemService(Context.MEDIA_PROJECTION_SERVICE);
               startActivityForResult(
                mediaProjectionManager.createScreenCaptureIntent(),
                REQUEST_MEDIA_PROJECTION);
    }
//    /**
//     * 锁屏
//     */
//    private static final String TAG = "MainActivity";
//    private void lockScreen() {
//        boolean active = policyManager.isAdminActive(componentName);
//        if (!active) {   //若无权限
//            Log.d(TAG, "lockScreen: ");
//            activeManage();//去获得权限
//        } else {
//            policyManager.lockNow();//直接锁屏
//        }
//        //killSelf ，锁屏之后就立即kill掉我们的Activity，避免资源的浪费;
//        killSelf();
//    }
//    /**
//     * 激活设备
//     */
//    private void activeManage() {
//        //启动设备管理(隐式Intent) - 在AndroidManifest.xml中设定相应过滤器
//        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//        //权限列表
//        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
//        //描述(additional explanation)
//        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "激活后才可以使用锁屏功能 ^.^ ");
//        startActivityForResult(intent, MY_REQUEST_CODE);
//    }
//    /**
//     * kill自己
//     */
//    private void killSelf() {
//        //killMyself ，锁屏之后就立即kill掉我们的Activity，避免资源的浪费;
//        android.os.Process.killProcess(android.os.Process.myPid());
//    }

}
