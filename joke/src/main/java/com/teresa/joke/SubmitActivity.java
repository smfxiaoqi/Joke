package com.teresa.joke;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.teresa.joke.util.JokeUtil;
import com.teresa.joke.util.MyJsonObjectRequest;
import com.teresa.joke.util.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SubmitActivity extends AppCompatActivity {
    private EditText submitContent;
    private Button btnSubmit;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        submitContent = (EditText) findViewById(R.id.submit_content);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

    }

    public void submit(View view) {
        username = JokeUtil.getUsername(getApplicationContext());
        String content = submitContent.getText().toString();
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("joketext", content);

        MyJsonObjectRequest request = new MyJsonObjectRequest(Request.Method.POST,
                VolleyUtil.getAbsoluteUrl("userAction_addPreCheck"),
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jo) {
                        try {
                            String statusRegister = jo.getString("number");
                            int status = Integer.parseInt(statusRegister);
                            if (status > 0) {
                                JokeUtil.toast(getApplicationContext(), "投稿成功");
                            } else {
                                JokeUtil.toast(getApplicationContext(), "投稿失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                JokeUtil.toast(getApplicationContext(), R.string.net_error);
            }
        });
        VolleyUtil.getInstance(this).addToRequestQueue(request, "submit_req");
    }
    @Override
    protected void onStop() {
        super.onStop();
        VolleyUtil.getInstance(this).cancelRequests("submit_req");
    }
}

