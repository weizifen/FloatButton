package com.example.weizifen.floatbutton.Util;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by weizifen on 17/2/17.
 */

public class AdminReceiver extends DeviceAdminReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Log.e("AdminReceiver","接收到广播~");
    }
}
