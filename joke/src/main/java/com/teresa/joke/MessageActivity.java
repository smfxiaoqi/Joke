package com.teresa.joke;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.teresa.joke.pojo.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.ButterKnife;
import butterknife.InjectView;

//系统消息
public class MessageActivity extends AppCompatActivity {
private List<Message> dataMessages;//系统消息数据集合
    private MessageAdapter adapter;
    private ListView listMessage;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        listMessage=(ListView)findViewById(R.id.listMessage);
        dataMessages=new ArrayList<>();
        dataMessages.add(new Message("投稿通过啦",false,1));
        dataMessages.add(new Message("有人评论啦",false,2));
        dataMessages.add(new Message("有新版本啦",false,3));
       adapter=new MessageAdapter(dataMessages,this);
  listMessage.setAdapter(adapter);
        //注册上下文菜单
//        registerForContextMenu(listMessage);
//点击一项弹出一个对话框显示所有内容
        listMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  Toast.makeText(getApplicationContext(), dataMessages.get(position), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(getCurrentFocus().getContext());
                builder.setMessage(dataMessages.get(position).getMessageContent());
                AlertDialog dialog = builder.create();
                dialog.show();
                dataMessages.get(position).setIfRead(true);
                adapter.notifyDataSetChanged();

            }
        });



    }
class MessageAdapter extends BaseAdapter{
private List<Message> datasMessage;
    private LayoutInflater inflater;


    public MessageAdapter(List<Message> datasMessage, Context context) {
        this.datasMessage = datasMessage;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return datasMessage.size();
    }

    @Override
    public Object getItem(int position) {
        return datasMessage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.messsage_item,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Message message=datasMessage.get(position);
        holder.messageContent.setText(message.getMessageContent());
       // holder.messageNumber.setText(message.getMessageNumber());
        if(message.isIfRead()){
            holder.red.setVisibility(View.GONE);
        }
        return convertView;
    }
    class ViewHolder{
        @InjectView(R.id.message_content)
        TextView messageContent;
        @InjectView(R.id.message_number)
        TextView messageNumber;
        @InjectView(R.id.red)
        ImageView red;
        public ViewHolder(View view) {
            ButterKnife.inject(this,view);
        }
    }
}
}
