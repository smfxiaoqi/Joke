package com.teresa.joke;
//设置界面
import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SettingActivity extends AppCompatActivity {
    private List<String> datas;
    private ListView settingListView;
    private List<String> datas2;
    private ListView settingListView2;
    private SettingAdapter adapter;
    private SettingAdapter adapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        datas=new ArrayList<>();
        datas.add("用户反馈");
        datas.add("更新版本");
        datas.add("关于    ");
        datas.add("清除缓存");
        datas2=new ArrayList<>();
        datas2.add("字体大小");
        datas2.add("字体颜色");
        datas2.add("更换头像");
        datas2.add("更换昵称");
        settingListView=(ListView)findViewById(R.id.setting);
        settingListView2=(ListView)findViewById(R.id.setting2);
        adapter=new SettingAdapter(datas,this,1);
        settingListView.setAdapter(adapter);
        adapter2=new SettingAdapter(datas2,this,2);
        settingListView2.setAdapter(adapter2);
    }
    class SettingAdapter extends BaseAdapter{
        private List<String> datas;
        private LayoutInflater inflater;
private int number;
        public SettingAdapter(List<String> datas,Context context,int number) {
            this.datas = datas;
            this.number=number;
            inflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if(convertView==null){
                convertView=inflater.inflate(R.layout.setting_item2,parent,false);

                holder=new ViewHolder();
                holder.settingIcon=(ImageView)convertView.findViewById(R.id.setting_icon);
                holder.textSetting = (TextView)convertView.findViewById(R.id.textSetting);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }
            String content=datas.get(position);
            holder.textSetting.setText(content);
            if(number==1){
                if(position==0){
                    holder.settingIcon.setImageResource(R.drawable.user_feedback);
                }else if(position==1){
                    holder.settingIcon.setImageResource(R.drawable.user_feedback);
                }else if(position==2){
                    holder.settingIcon.setImageResource(R.drawable.user_feedback);
                }else{
                    holder.settingIcon.setImageResource(R.drawable.user_feedback);
                }
            }else{
                if(position==0){
                    holder.settingIcon.setImageResource(R.drawable.text_large_small);
                }else if(position==1){
                    holder.settingIcon.setImageResource(R.drawable.text_color);
                }else if(position==2){
                    holder.settingIcon.setImageResource(R.drawable.user_change_image);
                }else{
                    holder.settingIcon.setImageResource(R.drawable.user_feedback);
                }
            }
            return convertView;
        }
    }
    class ViewHolder{
        private TextView textSetting;
        private ImageView settingIcon;
    }
    @Override
    protected void onStart() {
        super.onStart();
        NotificationManager manager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(0);
    }
}
