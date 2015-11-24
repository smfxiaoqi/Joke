package com.teresa.joke;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view=View.inflate(this,R.layout.activity_splash,null);
        setContentView(view);
        AlphaAnimation animation=new AlphaAnimation(0.3f,1.0f);
        animation.setDuration(1500);
        view.startAnimation(animation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
               Class<?> cls=null;

                cls=MainActivity.class;
                startActivity(new Intent(SplashActivity.this,cls));
                finish();
            }
        }).start();
    }
}
