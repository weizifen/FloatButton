package com.example.weizifen.floatbutton.Util;

import android.app.Activity;
import android.content.Context;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.weizifen.floatbutton.MainActivity;

/**
 * Created by weizifen on 17/2/20.
 */

public class ShotUtil {
    public static final int REQUEST_MEDIA_PROJECTION = 18;

    public static MediaProjectionManager mediaProjectionManager;

    private static final String TAG = "ShotUtil";

    public static void  requestCapturePermission(Activity activity) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //5.0 之后才允许使用屏幕截图

            return;
        }

         mediaProjectionManager = (MediaProjectionManager)
                activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        activity.startActivityForResult(
                mediaProjectionManager.createScreenCaptureIntent(),
                REQUEST_MEDIA_PROJECTION);
        Log.d(TAG, "requestCapturePermission: ");
    }
}
