package com.teresa.joke;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.teresa.joke.util.ImageTools;
import com.teresa.joke.util.JokeUtil;
import com.teresa.joke.util.MyJsonObjectRequest;
import com.teresa.joke.util.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

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


    private static final int TAKE_PHOTO = 0;
    private static final int CHOOSE_PHOTO = 1;

    private CircleImageView login_image_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
login_image_user=(CircleImageView)findViewById(R.id.login_image_user);
        String protocol = "<font color=" + "\"" + "#AAAAAA" + "\">" + "点击"
                + "\"" + "注册" + "\"" + "按钮,即表示你同意" + "</font>" + "<u>"
                + "<font color=" + "\"" + "#576B95" + "\">" + "《段王爷软件许可及服务协议》"
                + "</font>" + "</u>";

        tvProtocol.setText(Html.fromHtml(protocol));
    }

    @OnClick(R.id.login_image_user)
public void upload_image_user(){
    // 显示相片操作(0 拍照 / 1 选择相片)
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("图片来源:");
    builder.setNegativeButton("取消", null);
    builder.setItems(new String[]{"拍照", "相册"},
            new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case TAKE_PHOTO:
                            Intent openCameraIntent = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            Uri imageUri = Uri.fromFile(new File(Environment
                                    .getExternalStorageDirectory(), "image.jpg"));
                            // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(openCameraIntent, TAKE_PHOTO);
                            break;

                        case CHOOSE_PHOTO:
                            Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            openAlbumIntent.setType("image/*");
                            startActivityForResult(openAlbumIntent, CHOOSE_PHOTO);
                            break;
                    }
                }
            });
    builder.create().show();
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    // 将保存在本地的图片取出并缩小后显示在界面上
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment
                            .getExternalStorageDirectory() + "/image.jpg");
                    Bitmap newBitmap = ImageTools.zoomBitmap(bitmap,300,300);
                    // 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                    bitmap.recycle();

                    // 将处理过的图片显示在界面上，并保存到本地
                    login_image_user.setImageBitmap(newBitmap);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
                    String filename = sdf.format(new Date());

                    ImageTools.savePhotoToSDCard(newBitmap, Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                            .getAbsolutePath()
                            + "/Camera", "IMG_" + filename);

                    break;

                case CHOOSE_PHOTO:
                    ContentResolver resolver = getContentResolver();
                    // 照片的原始资源地址
                    Uri originalUri = data.getData();
                    try {
                        // 使用ContentProvider通过URI获取原始图片
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,
                                originalUri);
                        if (photo != null) {
                            // 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            Bitmap smallBitmap = ImageTools.zoomBitmap(photo,300,300);
                            // 释放原始图片占用的内存，防止out of memory异常发生
                            photo.recycle();
                            login_image_user.setImageBitmap(smallBitmap);

                        }
                    } catch (FileNotFoundException e) {
                        Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(this, "读取文件,出错啦", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }
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
            params.put("password", password);
            params.put("nickname", txtNickname.getText().toString());

            MyJsonObjectRequest request=new MyJsonObjectRequest(Request.Method.POST,
                    VolleyUtil.getAbsoluteUrl("userAction_addUser"),
                    params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jo) {
                            try {
                                String statusRegister=jo.getString("number");
                                int status=Integer.parseInt(statusRegister);
                                if(status>0){
                                    JokeUtil.toast(getApplicationContext(),"注册成功");
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                }else{
                                    JokeUtil.toast(getApplicationContext(),"此用户已注册");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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

