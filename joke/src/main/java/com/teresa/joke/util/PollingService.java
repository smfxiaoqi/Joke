package com.teresa.joke.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.teresa.joke.R;
import com.teresa.joke.SettingActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 轮询服务类，通过volley访问网络
 */
public class PollingService extends Service {
    public static final String TAG = "MainActivity";
    public static final String ACTION = "android.intent.action.PollingService";

    public PollingService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new PollingThread().start();
        return START_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    class PollingThread extends Thread{
        @Override
        public void run() {
            super.run();
            Log.i(TAG, "轮询...访问云端获取数据");

            String url = "http://192.168.15.109:8087/JOKES/PushSmsServlet";
            RequestQueue requestQueue= Volley.newRequestQueue(PollingService.this);
            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                    url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        String content = jsonObject.getString("content");
                        int number = jsonObject.getInt("number");
                        showNotification(content);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
Log.i(TAG,"响应错误");
                }
            });
            requestQueue.add(jsonObjectRequest);
        }
    }

    private void showNotification(String content) {
        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder=null;
        Intent intent=new Intent(this, SettingActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder = new NotificationCompat.Builder(this).
                setSmallIcon(R.drawable.gerenxinxi).
                setContentTitle("囧客").
                setContentText(content).
                setContentIntent(pendingIntent);
        Notification notification = builder.build();
        // 通知声音/震动/LED闪光 (低版本建议只使用 DEFAULT_SOUND)
        notification.defaults = Notification.DEFAULT_ALL;
        notificationManager.notify(0, notification);
    }
}
