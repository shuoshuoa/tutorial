package com.example.wangs.miniplan.JianKong;


import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.wangs.miniplan.DoPlan.DoPlan;
import com.example.wangs.miniplan.JianKong.models.AndroidAppProcess;
import com.example.wangs.miniplan.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangshuo on 16/2/29.
 */

public class ServiceBlog extends Service {

    public static final String LOGTAG = "SERVICE";
    ScheduledExecutorService executor;

    //当初始化时回调
    public void onCreate() {
        super.onCreate();
        Log.d(LOGTAG, "service created");
    }

    public int onStartCommand(final Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(LOGTAG, "service run");
        executeFixedRate();

        return START_NOT_STICKY;
    }

    private void jianKong() {
        List<TaskInfo> list = getTaskInfos1(this);
        Log.d("shuoshuoshuo", "==========");
        for (int i=0 ; i<list.size() ; i++) {
            if (list.get(i).getName().equals("MiniPlan"))
            {
                return ;
            }
        }
        for (int i=0 ; i<list.size() ; i++) {
            if (list.get(i).getName().equals("新浪微博") || list.get(i).getName().equals("皇室战争") || list.get(i).getName().equals("网易公开课") ||
                    list.get(i).getName().equals("优酷") || list.get(i).getName().equals("QQ") || list.get(i).getName().equals("微信")) {
                Log.d("shuoshuoshuo", "-------");
                Log.d("shuoshuoshuo", list.get(i).getName());
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                intent.setClass(getApplication(), DoPlan.class);
                startActivity(intent);
                break;
            }
        }
    }


    public void executeFixedRate() {
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        jianKong();
                        Log.d("ServiceBlog", "run is run");
                        try {
                            Thread.sleep(4000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                },
                0,
                1,
                TimeUnit.SECONDS);
    }

    //服务实例销毁时回调
    public void onDestroy() {
        Log.d("ServiceBlog", "销毁service");
        //获取系统AlarmManager实例
        executor.shutdown();
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }


    //获取系统运行的进程信息
    public List<TaskInfo> getTaskInfos1(Context context) {
        // 应用程序管理器
        ActivityManager am = (ActivityManager) context
                .getSystemService(context.ACTIVITY_SERVICE);

        // 应用程序包管理器
        PackageManager pm = context.getPackageManager();

        // 获取正在运行的程序信息, 就是以下粗体的这句代码,获取系统运行的进程要使用这个方法，需要加载
        // 　import com.jaredrummler.android.processes.ProcessManager;
        //　import com.jaredrummler.android.processes.models.AndroidAppProcess;  这两个包, 这两个包附件可以下载

        List<AndroidAppProcess> processInfos = ProcessManager.getRunningForegroundApps(this);

        List<TaskInfo> taskinfos = new ArrayList<TaskInfo>();
        // 遍历运行的程序,并且获取其中的信息
        for (AndroidAppProcess processInfo : processInfos) {
            if (processInfo.foreground) {
                TaskInfo taskinfo = new TaskInfo();
                // 应用程序的包名
                String packname = processInfo.name;
                taskinfo.setPackname(packname);
                // 湖区应用程序的内存 信息
                android.os.Debug.MemoryInfo[] memoryInfos = am
                        .getProcessMemoryInfo(new int[]{processInfo.pid});
                long memsize = memoryInfos[0].getTotalPrivateDirty() * 1024L;
                taskinfo.setMemsize(memsize);
                try {
                    // 获取应用程序信息
                    ApplicationInfo applicationInfo = pm.getApplicationInfo(packname, 0);
                    Drawable icon = applicationInfo.loadIcon(pm);
                    taskinfo.setIcon(icon);
                    String name = applicationInfo.loadLabel(pm).toString();
                    taskinfo.setName(name);
                    if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        // 用户进程
                        taskinfo.setUserTask(true);
                    } else {
                        // 系统进程
                        taskinfo.setUserTask(false);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    // 系统内核进程 没有名称
                    taskinfo.setName(packname);
                    Drawable icon = context.getResources().getDrawable(
                            R.drawable.ic_add_white_18dp);
                    taskinfo.setIcon(icon);
                }
                if (taskinfo != null) {
                    taskinfos.add(taskinfo);
                }
            }
        }
        return taskinfos;
    }

}

