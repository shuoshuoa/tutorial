package com.example.wangs.miniplan.AlarmClock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by wangshuo on 16/3/29.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmClock","now is the time to do plan!");

//        String planName = intent.getStringExtra("planName");
//        Log.d("AlarmClock",planName);
        Intent intent1 = new Intent(context,ShowActivity.class);
//        intent.putExtra("planName",planName);
        intent1.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);

    }
}
