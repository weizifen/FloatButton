package com.example.weizifen.floatbutton.Util;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Created by weizifen on 17/2/17.
 */

public class LockUtil {
    public static final int MY_REQUEST_CODE = 9999;
    public static DevicePolicyManager policyManager;
    public static ComponentName componentName;



    /**
     * 锁屏
     */
    public static void lockScreen(Activity activity) {
        //获取设备管理服务
        policyManager = (DevicePolicyManager) activity.getSystemService(Context.DEVICE_POLICY_SERVICE);
        //AdminReceiver 继承自 DeviceAdminReceiver
        componentName = new ComponentName(activity, AdminReceiver.class);
        boolean active = policyManager.isAdminActive(componentName);
        if (!active) {   //若无权限
            activeManage(activity);//去获得权限
        } else {
            policyManager.lockNow();//直接锁屏
        }
        //killSelf ，锁屏之后就立即kill掉我们的Activity，避免资源的浪费;
//        killSelf();
    }
    /**
     * 激活设备
     */
    private static void activeManage(Activity activity) {
        //启动设备管理(隐式Intent) - 在AndroidManifest.xml中设定相应过滤器
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        //权限列表
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        //描述(additional explanation)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "激活后才可以使用锁屏功能 ^.^ ");
        activity.startActivityForResult(intent, MY_REQUEST_CODE);
    }

    /**
     * kill自己
     */
    private static void killSelf() {
        //killMyself ，锁屏之后就立即kill掉我们的Activity，避免资源的浪费;
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
