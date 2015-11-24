package com.teresa.joke;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.teresa.joke.util.JokeUtil;
import com.teresa.joke.util.QQUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tablayout;
    private ViewPager viewPager;
    //private int[] tabImg;

    private MyFragmentAdapter adapter;
    private DrawerLayout drawerLayout;
    private ImageView userImg;
    private TextView username;
    private TextFragment textFragment;
    private ImageFragment imageFragment;
    private List<Fragment> fragments;
    private List<String> titles;
    private String nav_username;
    private Bitmap nav_userImage;
    private NavigationView navView;

    public static Tencent mTencent;
    // QQ用户信息
    private UserInfo mInfo;
private boolean status;//登录状态
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mTencent = LoginActivity.mTencent;
        updateUserImg();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setTitle("");
        //BitmapDrawable bd=new BitmapDrawable(nav_userImage);
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

        navView = (NavigationView) findViewById(R.id.nav_view);


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
        navView = (NavigationView) findViewById(R.id.nav_view);
        //判断用户是否登录过
        if (JokeUtil.isLogined(getApplicationContext())) {
            //登陆过。加载已登录导航
             status=true;
            //从本地文件获取用户头像
            //nav_userImage=JokeUtil.getUserImage();
            //userImg.setImageBitmap(nav_userImage);
            //navView.inflateMenu(R.menu.menu_logined_nav);
        } else {
            status=false;
            //navView.inflateMenu(R.menu.drawer_view);
        }
        Log.i("MainActivity",String.valueOf(status)+"**********");
        setDrawerContent(navView, status);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    //初始化抽屉登录布局
    private void setDrawerContent(final NavigationView navView, final boolean status) {
        View view = View.inflate(this, R.layout.login_nav, navView);
        userImg = (ImageView) view.findViewById(R.id.imgUsername);
        username = (TextView) view.findViewById(R.id.tvUsername);
        if(status==true||getIntent().getBooleanExtra("ifLogined",false)==true){
            navView.getMenu().setGroupVisible(R.id.nav_view,false);
            navView.inflateMenu(R.menu.menu_logined_nav);
        }else if(status==false||getIntent().getBooleanExtra("ifLogined",false)==false){
            navView.getMenu().setGroupVisible(R.id.nav_view,false);
            navView.inflateMenu(R.menu.drawer_view);
       }
        // -- 左侧抽屉导航视图 菜单 ------------------------------
        // 导航视图中的菜单选中事件
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (status == false) {
                    switch (menuItem.getItemId()) {
                        // 匹配菜单的选项 (其它选项的处理 省略)
                        case R.id.nav_login:
                            //点击登录，跳到登录页面，返回一个是否成功登录的值，采用回调函数
                            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivityForResult(loginIntent, 100);
                            break;
                        case R.id.nav_register:
                            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                            break;
                        case R.id.nav_logout:
                            mTencent.logout(getApplicationContext());
                            JokeUtil.savePreferences(getApplicationContext(), 0, null, null, null);
                            finish();
                            break;
                    }
                    return true;
                }
                // 选择后自动关闭左侧抽屉
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100) {
            if (resultCode == RESULT_OK) {
                status = data.getBooleanExtra("qqLogined", false);
                Log.i("MainActivity", String.valueOf(status) + "qq success");
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("ifLogined",status);
                startActivity(intent);
            }
        }
    }

    private void updateUserImg() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {

                @Override
                public void onError(UiError e) {

                }

                @Override
                public void onComplete(final Object response) {
                    Log.v("MainActivity", "My QQ Info:\n" + response.toString());

                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                    new Thread() {

                        @Override
                        public void run() {
                            JSONObject json = (JSONObject) response;
                            if (json.has("figureurl")) {
                                Bitmap bitmap = null;
                                try {
                                    bitmap = QQUtil.getbitmap(json.getString("figureurl_qq_2"));
                                    //JokeUtil.saveUserImg(bitmap);
                                } catch (JSONException e) {

                                }
                                Message msg = new Message();
                                msg.obj = bitmap;
                                msg.what = 1;
                                mHandler.sendMessage(msg);
                            }
                        }

                    }.start();
                }

                @Override
                public void onCancel() {

                }
            };
            mInfo = new UserInfo(this, mTencent.getQQToken());
            mInfo.getUserInfo(listener);

        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONObject response = (JSONObject) msg.obj;
                if (response.has("nickname")) {
                    try {
                        nav_username = response.getString("nickname");

                        username.setText(nav_username);
                        JokeUtil.savePreferences(getApplicationContext(), 1, nav_username, null, null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (msg.what == 1) {
                nav_userImage = (Bitmap) msg.obj;
                userImg.setImageBitmap(nav_userImage);

            }
        }

    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            ExitDialog(MainActivity.this).show();
        }
        return super.onKeyDown(keyCode, event);
    }

    private Dialog ExitDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.cry);
        builder.setTitle("真的离开吗？");
        builder.setMessage("确定要退出程序吗?");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        finish();
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        return builder.create();
    }
}