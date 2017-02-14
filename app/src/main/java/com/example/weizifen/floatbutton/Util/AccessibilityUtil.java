package com.example.weizifen.floatbutton.Util;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * Created by weizifen on 17/2/14.
 */

public class AccessibilityUtil {

    /*判断协助功能是否打开
    * */
    public static boolean isAccessibilitySettingsOn(Context context)
    {
        int  accessibilityEnabled=0;

        try {
            accessibilityEnabled=Settings.Secure.getInt(context.getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }


        if (accessibilityEnabled==1)
        {
            String services=Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {

                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }
        return false;
    }

    /*
    * 单击执行返回功能
    * */
    private static final String TAG = "AccessibilityUtil";
    public static void doBack(AccessibilityService service)
    {
        Log.d(TAG, "doBack: ");
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);



    }

    /*
    * 下拉
    * */
    public static void doPullDown(AccessibilityService service){
        Log.d(TAG, "下拉");
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS);
        
    }
    /*
    * 上滑
    * */
    public static void doUpSlide(AccessibilityService service){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
        }
    }
    /*
    左右滑动
    * */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void doLeftOrRightSlide(AccessibilityService service){
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
    }



}
