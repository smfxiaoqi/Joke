package com.teresa.joke;
//我要投稿

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
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class SubmitActivity extends AppCompatActivity {
    private EditText submitContent;
    private Button btnSubmit;
    private String username;
    private LinearLayout submitLayout;

    private static final int TAKE_PHOTO = 0;
    private static final int CHOOSE_PHOTO = 1;
    int count = 0;
    int width=0;
    int height=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        ButterKnife.inject(this);
        submitContent = (EditText) findViewById(R.id.submit_content);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        submitLayout = (LinearLayout) findViewById(R.id.container);

    }

    @OnClick(R.id.imageButton)
    public void imageButton() {
        if (count < 5) {
            //打开相册或者拍照
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
                                            .getExternalStorageDirectory() + "/submit", "submit_image.jpg"));
                                    // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                                    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                    //openCameraIntent.putExtra("count",count);
                                    startActivityForResult(openCameraIntent, TAKE_PHOTO);
                                    break;

                                case CHOOSE_PHOTO:
                                    Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                    openAlbumIntent.setType("image/*");
                                    //openAlbumIntent.putExtra("count", count);
                                    startActivityForResult(openAlbumIntent, CHOOSE_PHOTO);
                                    break;
                            }
                        }
                    });
            builder.create().show();
        } else {
              Toast.makeText(this, "最多只能上传五张图......", Toast.LENGTH_SHORT).show();
        }
    }

    public ImageView createImageView() {
        //新建ImageView
        submitLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ImageView image = new ImageView(this);
lp1.gravity= Gravity.CENTER_HORIZONTAL;
        submitLayout.addView(image, lp1);
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    // 将保存在本地的图片取出并缩小后显示在界面上
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment
                            .getExternalStorageDirectory() + "/submit/submit_image.jpg");
                    if(bitmap.getWidth()>bitmap.getHeight()){
                        width=900;
                        height=700;
                    }else{
                        width=700;
                        height=900;
                    }
                    Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, width, height);
                    // 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                    bitmap.recycle();
ImageView imageT=createImageView();
                    submitContent.setVisibility(View.GONE);
imageT.setImageBitmap(newBitmap);

                    // 将处理过的图片显示在界面上，并保存到本地

                    //Toast.makeText(getApplicationContext(), String.valueOf(data.getIntExtra("count", 6)), Toast.LENGTH_SHORT).show();
//

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
                    String filename = sdf.format(new Date());

                    ImageTools.savePhotoToSDCard(newBitmap, Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                            .getAbsolutePath()
                            + "/Camera/submit", "IMG_" + filename);

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

                            if(photo.getWidth()>photo.getHeight()){
                                width=900;
                                height=700;
                            }else{
                                width=700;
                                height=900;
                            }
                            // 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                           Bitmap smallBitmap = ImageTools.zoomBitmap(photo, width, height);
                            // 释放原始图片占用的内存，防止out of memory异常发生

                            submitContent.setVisibility(View.GONE);
                            ImageView imageC = createImageView();
                            imageC.setImageBitmap(smallBitmap);
                            photo.recycle();
                            count++;
                            // 将处理过的图片显示在界面上，并保存到本地

                            // Toast.makeText(getApplicationContext(), String.valueOf(data.getIntExtra("count",6)), Toast.LENGTH_SHORT).show();
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

