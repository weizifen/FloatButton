package com.example.weizifen.floatbutton.Service;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.accessibility.AccessibilityEvent;

import com.example.weizifen.floatbutton.FloatWindowManager;

public class FloatBallService extends AccessibilityService {
    public static final int TYPE_ADD = 0;
    public static final int TYPE_DEL = 1;
    public FloatBallService() {
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

    }

    @Override
    public void onInterrupt() {

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle data=intent.getExtras();
        if (data!=null)
        {
            int type=data.getInt("type");
            if (type == TYPE_ADD) {
                FloatWindowManager.addView(this);
            } else {
                FloatWindowManager.removeBallView(this);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
