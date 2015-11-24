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
    private static final File previewTempFile=new File(Environment.getExternalStorageDirectory()
    +"/joke/userImage/previewTempFile.jpg");
    private static int MODE = Context.MODE_PRIVATE;
    // 定义SharedPreferences名称,该名称和在Android系统中保存的文件同名
    private static final String PREFERENCE_NAME = "JokeSettings";

    /**
     * 是否已成功登录
     *
     * @return
     */
    public static boolean isLogined(Context context) {
        boolean flag = false;
        // 读取选项存储中的用户信息(类似于浏览器中的 Cookie)
        SharedPreferences preferences =
                context.getSharedPreferences(PREFERENCE_NAME, MODE);

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
                                       int id, String username, String nickname, String password) {
        // 保存用户信息到选项存储
        SharedPreferences preferences =
                context.getSharedPreferences(PREFERENCE_NAME, MODE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("id", id);
        editor.putString("username", username);
        editor.putString("nickname", nickname);
        editor.putString("password", password);
        editor.commit();
    }

    /**
     * 返回当前已登录的用户名
     *
     * @param context
     * @return
     */
    public static String getUsername(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences(PREFERENCE_NAME, MODE);
        return preferences.getString("username", "");
    }
    //保存用户头像到本地
    public static void saveUserImg(Bitmap userImage){
if(previewTempFile!=null){
    previewTempFile.delete();
}else{
    try {
        previewTempFile.createNewFile();
    } catch (IOException e) {
        e.printStackTrace();
    }
    FileOutputStream fout=null;
    try {
        fout=new FileOutputStream(previewTempFile);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    userImage.compress(Bitmap.CompressFormat.JPEG,100,fout);
    try {
        fout.flush();
        fout.close();
    } catch (IOException e) {
        e.printStackTrace();
    }

}
    }
    //返回用户头像
    public static Bitmap getUserImage(){
        Bitmap userBitmap= BitmapFactory.decodeFile(previewTempFile.getPath());
        return userBitmap;
    }
}
