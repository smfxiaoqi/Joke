package com.teresa.joke.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Admin on 2015/11/9.
 */
public class JokeUtil {

    public static void toast(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    // -- 选项存储 (记录用户的偏好)--------------------------------
    // 定义访问模式
    private static int MODE = Context.MODE_PRIVATE;
    // 定义SharedPreferences名称,该名称和在Android系统中保存的文件同名
    private static final String USER = "User";
    private static final String SYSTEM_MESSAGE= "SystemMessage";
    private static SharedPreferences.Editor editor;
private static SharedPreferences preferences;
    /**
     * 是否已成功登录
     *
     * @return
     */
    public static boolean isLogined(Context context) {
        boolean flag = false;
        // 读取选项存储中的用户信息(类似于浏览器中的 Cookie)
        preferences =
                context.getSharedPreferences(USER, MODE);

        int id = preferences.getInt("id", 0);
        String username = preferences.getString("username", "");

        if (id > 0 && username.length() > 0) {
            flag = true; // 已登录
        }
        return flag;
    }

    /**
     * 登录成功,保存用户信息到 选项存储
     *
     * @param context
     * @param username
     * @param password
     */
    public static void savePreferences(Context context,
                                       int id, String username, String password, String nickname) {
        // 保存用户信息到选项存储
        preferences =
                context.getSharedPreferences(USER, MODE);
        editor = preferences.edit();
        editor.putInt("id", id);
        editor.putString("username", username);
        editor.putString("nickname", nickname);
        editor.putString("password", password);
        editor.commit();
    }

    /**
     * 把从服务器端传送过来的数据解析，存到选项存储
     * @param messages
     * @param number 消息编号,为了删除的时候方便找到对应的消息
     */
public void saveSystemMessage(Context context,String messages,int number){
    preferences =
            context.getSharedPreferences(SYSTEM_MESSAGE, MODE);
     editor=preferences.edit();
    editor.putString("message" + number, messages);
}
    public void deleteSystemMessage(Context context,int number){
        editor.remove("message" + number);
    }
    /**
     * 返回当前已登录的用户名
     *
     * @param context
     * @return
     */
    public static String getUsername(Context context) {
        preferences =
                context.getSharedPreferences(USER, MODE);
        return preferences.getString("username", "");
    }
    public static String getNickName(Context context) {
        preferences =
                context.getSharedPreferences(USER, MODE);
        return preferences.getString("nickname", "");
    }


    //清除选项存储数据方法
    public static void clearData() {
        preferences.edit().clear().commit();
    }
}
