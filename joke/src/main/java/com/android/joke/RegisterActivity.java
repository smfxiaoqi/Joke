package com.android.joke;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.EditText;
import android.widget.TextView;

import com.android.joke.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.joke.util.JokeUtil;
import com.android.joke.util.MyJsonObjectRequest;
import com.android.joke.util.VolleyUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {
        @InjectView(R.id.txtUsername)
        EditText txtUsername;
        @InjectView(R.id.txtNickname)
        EditText txtNickname;
        @InjectView(R.id.txtPassword)
        EditText txtPassword;
        @InjectView(R.id.txtRePassword)
        EditText txtRePassword;
        @InjectView(R.id.tvProtocol)
        TextView tvProtocol;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);
            ButterKnife.inject(this);
            String protocol = "<font color=" + "\"" + "#AAAAAA" + "\">" + "点击"
                    + "\"" + "注册" + "\"" + "按钮,即表示你同意" + "</font>" + "<u>"
                    + "<font color=" + "\"" + "#576B95" + "\">" + "《段王爷软件许可及服务协议》"
                    + "</font>" + "</u>";

            tvProtocol.setText(Html.fromHtml(protocol));
        }

        @OnClick(R.id.btn_register)
        public void registerClick() {
            String password = txtPassword.getText().toString();
            String rePassword = txtRePassword.getText().toString();

            if (!password.equals(rePassword)) {
                JokeUtil.toast(this, R.string.password_error);
                return;
            }
            Map<String,String > params=new HashMap<>();
            params.put("username",txtUsername.getText().toString());
            params.put("password",password);
            params.put("nickname",txtNickname.getText().toString());

            MyJsonObjectRequest request=new MyJsonObjectRequest(Request.Method.POST,
                    VolleyUtil.getAbsoluteUrl("服务器名称"),
                    params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jo) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    JokeUtil.toast(getApplicationContext(),R.string.net_error);
                }
            });
            VolleyUtil.getInstance(this).addToRequestQueue(request, "register_req");
        }

        @Override
        protected void onStop() {
            super.onStop();
            VolleyUtil.getInstance(this).cancelRequests("register_req");
        }
    }

