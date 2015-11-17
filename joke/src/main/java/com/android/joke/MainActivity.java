package com.android.joke;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.joke.util.QQUtil;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tablayout;
    private ViewPager viewPager;
    //private int[] tabImg;

    public static Tencent mTencent;
    // QQ用户信息
    private UserInfo mInfo;

private MyFragmentAdapter adapter;
    private DrawerLayout drawerLayout;
    private ImageView userImg;
    private TextView username;
    private TextFragment textFragment;
    private ImageFragment imageFragment;
    private List<Fragment> fragments;
    private List<String> titles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.gerenxinxi);
   toolbar.setNavigationOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           drawerLayout.openDrawer(GravityCompat.START);
       }
   });
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        // 抽屉布局
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // 导航视图
        NavigationView navViewLogin = (NavigationView) findViewById(R.id.nav_view);
        if (navViewLogin != null) {
            setDrawerContent(navViewLogin);
        }

        tablayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        textFragment = new TextFragment();
        imageFragment = new ImageFragment();
        fragments.add(textFragment);
        fragments.add(imageFragment);

        titles.add("文字");
        titles.add("图片");

        tablayout.addTab(tablayout.newTab().setText(titles.get(0)));
        tablayout.addTab(tablayout.newTab().setText(titles.get(1)));
        adapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments, this, titles);
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    private void setDrawerContent(NavigationView navViewLogin) {
       mTencent=LoginActivity.mTencent;
        userImg=(ImageView)navViewLogin.findViewById(R.id.imgUsername);
        username=(TextView)navViewLogin.findViewById(R.id.tvUsername);

        updateUserImg();

        // -- 左侧抽屉导航视图 菜单 ------------------------------
        // 导航视图中的菜单选中事件
        navViewLogin.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    // 匹配菜单的选项 (其它选项的处理 省略)
                    case R.id.nav_login:
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        break;
                    case R.id.nav_register:
                        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                        break;
                }
                // 选择后自动关闭左侧抽屉
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    private void updateUserImg() {
        if (mTencent!=null&&mTencent.isSessionValid()){
            IUiListener listener=new IUiListener() {
                @Override
                public void onError(UiError e) {

                }
                @Override
                public void onComplete(final Object response) {

                    Message msg=new Message();
                    msg.obj=response;
                    msg.what=0;
                    mHandler.sendMessage(msg);
                    new Thread(){
                        @Override
                        public void run() {
                            JSONObject json=(JSONObject)response;
                            if (json.has("figureurl")){
                                Bitmap bitmap=null;
                                try {
                                    bitmap= QQUtil.getbitmap(json.getString("figureurl_qq_2"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Message msg=new Message();
                                msg.obj=bitmap;
                                msg.what=1;
                                mHandler.sendMessage(msg);
                            }
                        }
                    }.start();

                }

                @Override
                public void onCancel() {

                }
            };

           mInfo=new UserInfo(this,mTencent.getQQToken());
            mInfo.getUserInfo(listener);
        }
    }
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0){
                JSONObject response=(JSONObject)msg.obj;
                if (response.has("nickname")){
                    try {
                    username.setVisibility(View.VISIBLE);
                    username.setText(response.getString("nickname"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else if (msg.what==1){
                Bitmap bitmap=(Bitmap)msg.obj;
                userImg.setImageBitmap(bitmap);
                userImg.setVisibility(View.VISIBLE);
            }
        }
    };
}