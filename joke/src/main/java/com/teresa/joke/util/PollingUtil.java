package com.teresa.joke.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by Administrator on 15-11-30.
 */
public class PollingUtil {
    //轮询工具类（开启/关闭）

    /**
     *
     * @param context
     * @param seconds
     * @param cls
     * @param action
     */
    public static void startPollingService(Context context,int seconds,Class<?> cls,String action){
        //获取AlarmManager系统服务
        AlarmManager alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    //包装需要执行service的intent
        Intent intent=new Intent(context,cls);
        intent.setAction(action);
        PendingIntent pendingIntent=PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //触发服务的起始时间
        long triggerAtTime= SystemClock.elapsedRealtime();
        //使用AlarmManger的setRepeating方法设置定期执行的时间间隔（seconds秒）
        // 和需要执行的Service
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                triggerAtTime, seconds * 1000, pendingIntent);
    }
    public static void stopPollingService(Context context, Class<?> cls,
                                          String action){
        AlarmManager manager = (AlarmManager) context.getSystemService(
                Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pi = PendingIntent.getService(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        //取消正在执行的服务
        manager.cancel(pi);
    }
}
