package com.example.wangs.miniplan.AlarmClock;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by wangshuo on 16/3/29.
 */
public class AlarmAlertWakeLock  {

    private static PowerManager.WakeLock cpuWakeLock;

    static void acuqireCpuWakeLock(Context context) {
        Log.d("shuo","acuqiring cpu wake lock!");
        if (cpuWakeLock != null){
            return ;
        }

        PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);

        cpuWakeLock = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "shuo");
        cpuWakeLock.acquire();

    }

    static void releaseCpuLock() {
        Log.d("shuo","Releasing cpu wake lock");
        if (cpuWakeLock != null) {
            cpuWakeLock.release();
            cpuWakeLock = null;
        }
    }

}
