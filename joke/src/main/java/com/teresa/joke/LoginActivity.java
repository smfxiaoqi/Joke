package com.teresa.joke;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.teresa.joke.util.JokeUtil;
import com.teresa.joke.util.MyJsonObjectRequest;
import com.teresa.joke.util.QQUtil;
import com.teresa.joke.util.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @InjectView(R.id.edtName)
    EditText edtName;
    @InjectView(R.id.edtPassword)
    EditText edtPassword;
    @InjectView(R.id.ImgPassword)
    ImageView ImgPassword;

    private boolean isChecked = true;
    // 自己的 APP_ID 替换 222222
    public static String mAppid = "222222";
    // 腾讯对象
    public static Tencent mTencent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.inject(this);

        if (mTencent==null){
            mTencent=Tencent.createInstance(mAppid,this);
        }
    }
    @OnClick(R.id.imageButton2)
    public void qqLogin(){
        if (!mTencent.isSessionValid()){
            mTencent.login(this,"all",loginListener);
        }
    }
    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            initOpenidAndToken(values);
        }
    };

    private class BaseUiListener implements IUiListener {
        boolean qqYes=true;
        @Override
        public void onComplete(Object response) {
            if (null == response) {
                QQUtil.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
                 qqYes=false;
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                QQUtil.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
                qqYes=false;
                return;
            }
            // QQUtil.showResultDialog(LoginActivity.this, response.toString(), "登录成功"
            //JokeUtil.savePreferences(getApplicationContext(),1,);

            Intent qqLogin=getIntent();
            qqLogin.putExtra("qqLogined", qqYes);
          //  qqLogin.setData();
            setResult(MainActivity.RESULT_OK, qqLogin);
            LoginActivity.this.finish();
            //startActivity(qqLogin);


        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            QQUtil.toastMessage(LoginActivity.this, "onError: " + e.errorDetail);
            QQUtil.dismissDialog();
        }

        @Override
        public void onCancel() {
            QQUtil.toastMessage(LoginActivity.this, "onCancel: ");
            QQUtil.dismissDialog();
        }
    }

    public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }
    @OnClick(R.id.ImgPassword)
    public void imgClick(){
        if (isChecked){
            ImgPassword.setImageResource(R.drawable.light);
            edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isChecked=false;
        }else {
            ImgPassword.setImageResource(R.drawable.dark);
            edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isChecked=true;
        }
    }
    @OnClick(R.id.btnNoPassWord)
    public void loginClick(){
        String username=edtName.getText().toString();
        String password=edtPassword.getText().toString();

        Map<String,String> params=new HashMap<>();
        params.put("username",username);
        params.put("password",password);

        MyJsonObjectRequest request=new MyJsonObjectRequest(Request.Method.POST,
                VolleyUtil.getAbsoluteUrl("userAction_findUser"), params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if (jsonObject.getInt("id")==0){
                                JokeUtil.toast(getApplicationContext(),R.string.user_invalid);
                            }else {
                                //把用户名、密码、昵称存进选项存储
                                JokeUtil.savePreferences(getApplicationContext(),
                                        jsonObject.getInt("id"),
                                        jsonObject.getString("username"),
                                        jsonObject.getString("password"),
                                        jsonObject.getString("nickname"));
                                //把昵称数据发送给其他Activity进行设置

                                JokeUtil.toast(getApplicationContext(),jsonObject.getString("username")+
                                        jsonObject.getString("password")+jsonObject.getString("nickname")
                                );
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                JokeUtil.toast(getApplicationContext(),R.string.net_error);
            }
        });
        VolleyUtil.getInstance(this).addToRequestQueue(request);
    }

    @Override
    protected void onStop() {
        super.onStop();
        VolleyUtil.getInstance(this).cancelRequests(VolleyUtil.TAG);
    }
}
