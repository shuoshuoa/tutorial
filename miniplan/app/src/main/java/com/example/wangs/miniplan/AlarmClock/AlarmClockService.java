package com.example.wangs.miniplan.AlarmClock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.wangs.miniplan.Database.PlanDatabase;

import java.util.Calendar;

/**
 * Created by wangshuo on 16/3/29.
 */
public class AlarmClockService extends Service {

    public static final String TAG = "AlarmClock";

    private AlarmManager am;
    private Calendar calendar;
    private PlanDatabase helper;

    int hour, minute;
    int id;
    String str_hour, str_minute;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        Log.d(TAG, "service created");
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        helper = new PlanDatabase(this);
        Cursor c = helper.query("PlanTable1");
        while (c.moveToNext()) {
//            id = c.getInt(0);
//            String planName = c.getString(1);
            String alarmTime = c.getString(4);
            String isDone = c.getString(5);
            //当任务没有被执行时，就对该任务进行闹钟提醒
            if (isDone.equals(">")) {
                for (int i=0; i<alarmTime.length(); i++)
                {
                    if (alarmTime.substring(i,i+1).equals(":"))
                    {
                        str_hour = alarmTime.substring(0,i).trim();
                        str_minute = alarmTime.substring(i+1,alarmTime.length()).trim();
                    }
                }
                hour = Integer.parseInt(str_hour);
                minute = Integer.parseInt(str_minute);
                calendar = Calendar.getInstance();
                //指定触发闹钟的时间
                //Log.d("alarmTime"+alarmTime,str_hour+":"+str_minute);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
            //    Log.d(TAG,calendar.getTimeInMillis()+"\n"+System.currentTimeMillis());
                if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
                    Intent intent1 = new Intent(this, AlarmReceiver.class);
//                    Log.d(TAG,planName);
//                    intent1.putExtra("planName",planName);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent1, 0);
                    am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
                }
            }
        }
        return START_STICKY;
    }

    public void onDestroy() {
        Log.d(TAG, "kill the service!");
        super.onDestroy();
    }


}
