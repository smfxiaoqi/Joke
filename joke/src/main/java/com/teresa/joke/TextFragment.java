package com.teresa.joke;



import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.teresa.joke.pojo.MyCardView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TextFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<MyCardView> cardDatas;
    private TextCardAdapter cardAdapter;

    public TextFragment() {
        // Required empty public constructor
    }


    private void initData() {
        MyCardView card1 = new MyCardView(getActivity());
        MyCardView card2 = new MyCardView(getActivity());
        MyCardView card3 = new MyCardView(getActivity());
        card1.setCard_image_username(R.drawable.lock);
        card1.setCard_tv_submitTime("2011-5-16");
        card1.setCard_tv_username("haha");
        card1.setCountGood(345);
        card1.setCountComment(112);
        card1.setCardview_content("“十一”前几天在家上网，" +
                "一好久不联系的大学同学，" +
                "突然间qq、微信都在线了，" +
                "还给我发了个祝福短信。" +
                "第一反应就是这货要结婚了，" +
                "果断编了个理由回他，" +
                "“哥们，我‘十一’订婚，" +
                "你来不来参加我的订婚宴啊？”" +
                "果不其然，他回：“不好意思，我‘十一’结婚，" +
                "看样子你也来不了了。”～省了500大洋。");
        card2.setCard_image_username(R.drawable.jiesuo);
        card2.setCard_tv_submitTime("2016-5-6");
        card2.setCard_tv_username("hengheng");
        card2.setCountGood(569);
        card2.setCountComment(3);
        card2.setCardview_content("和男朋友一起走，" +
                "喜欢手搂着他的腰，" +
                "顺便扯着他的衣服。" +
                "有一天散步中，他忽然说：" +
                "“别扯我的衣服了好不好？”" +
                "我不悦，说：“你和我说话就不能加个宝贝？”" +
                "然后他说：“别扯我的宝贝衣服好不好？”我：“……”");
        card3.setCard_image_username(R.drawable.gerenxinxi);
        card3.setCard_tv_submitTime("2013-8-6");
        card3.setCard_tv_username("heihei");
        card3.setCountGood(50);
        card3.setCountComment(20);
        card3.setCardview_content("一个新手去收高利贷，" +
                "他把借条拿出来，笑着说：" +
                "“白纸黑字明明白白地写着你欠我 100万！" +
                "难道你想赖帐。”" +
                "人家表示确实没有那么多钱，" +
                "" +
                "他威胁道：“哼哼！别怪我没提醒你！" +
                "明天再交不出钱，你的房子就像它一样。”" +
                "他掏出打火机就把借条烧了……-");
        MyCardView card4=new MyCardView(getActivity());
        card4.setCard_image_username(R.drawable.lock);
        card4.setCard_tv_submitTime("2011-5-16");
        card4.setCard_tv_username("haha");
        card4.setCountGood(345);
        card4.setCountComment(112);
        card4.setCardview_content("“十一”前几天在家上网，" +
                "一好久不联系的大学同学，" +
                "突然间qq、微信都在线了，" +
                "还给我发了个祝福短信。" +
                "第一反应就是这货要结婚了，" +
                "果断编了个理由回他，" +
                "“哥们，我‘十一’订婚，" +
                "你来不来参加我的订婚宴啊？”" +
                "果不其然，他回：“不好意思，我‘十一’结婚，" +
                "看样子你也来不了了。”～省了500大洋。");
        cardDatas.add(card1);
        cardDatas.add(card2);
        cardDatas.add(card3);
        cardDatas.add(card4);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        cardDatas = new ArrayList<>();
        initData();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cardAdapter = new TextCardAdapter();
        recyclerView.setAdapter(cardAdapter);
        return view;
    }


    private class TextCardAdapter extends RecyclerView.Adapter<TextCardAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(getActivity()).
                    inflate(R.layout.item_text_card_recycler, parent, false));
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.card_tv_username.setText(cardDatas.get(position).getCard_tv_username());
            holder.card_image_username.setImageResource(cardDatas.get(position).getCard_image_username());
            holder.card_tv_submitTime.setText(cardDatas.get(position).getCard_tv_submitTime());
            holder.cardview_content.setText(cardDatas.get(position).getCardview_content());
            //当前点赞数
            final int currentCountGood = cardDatas.get(position).getCountGood();
            holder.countGood.setText(String.valueOf(currentCountGood));
            //当前评论数
            int currentCountComment = cardDatas.get(position).getCountComment();
            holder.countComment.setText(String.valueOf(currentCountComment));
            //点赞监听
            holder.good.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击之后countGood+1，图片改变
                    //发送给服务器的数据+1，界面上的数据+1
                    cardDatas.get(position).setCountGood(currentCountGood + 1);
                    Toast.makeText(getActivity(), String.valueOf(currentCountGood), Toast.LENGTH_SHORT).show();
                    holder.countGood.setText(String.valueOf(currentCountGood + 1));
                    holder.good.setImageResource(R.drawable.goodlater);
                }
            });
            //收藏监听
            holder.collect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //用户收藏了此段子，将数据封装存储到服务器
                    //图片颜色改变
                    holder.collect.setImageResource(R.drawable.collentlater);
                }
            });
            //点击评论弹出一个对话框
            holder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    //设置此登录用户的头像
                   // builder.setIcon(R.drawable.manng);
                    ////设置此登录用户的用户名
                    //builder.setTitle("username");
                    AlertDialog dialogComment = builder.create();
                    dialogComment.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    dialogComment.show();
                    Window windowComment = dialogComment.getWindow();
                    windowComment.setContentView(R.layout.dialog_comment);
                    ImageButton ok = (ImageButton) windowComment.findViewById(R.id.ok);
                    ImageView comment_image_username= (ImageView) windowComment.findViewById(R.id.comment_username_image);
                    TextView comment_tv_username= (TextView) windowComment.findViewById(R.id.comment_tv_username);
                    comment_image_username.setImageResource(R.drawable.biaoqing);
                    comment_tv_username.setText("Teresa");
                    final EditText contentComment = (EditText) windowComment.findViewById(R.id.tvContent);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //点击ok，评论的数据封装传入数据库，同时在评论页面显示出来
                            //先测试

                            String scontentComment=contentComment.getText().toString();
                            contentComment.requestFocus();
                            Toast.makeText(getActivity(), "您的评论为" + scontentComment, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

        }

        @Override
        public int getItemCount() {
            return cardDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            CardView cardView;
            ImageView card_image_username;
            TextView card_tv_username;
            TextView card_tv_submitTime;
            TextView cardview_content;
            ImageButton good;
            ImageButton comment;
            ImageButton collect;
            TextView countGood;
            TextView countComment;

            public MyViewHolder(View itemView) {
                super(itemView);
                cardView = (CardView) itemView.findViewById(R.id.cardview);
                card_image_username = (ImageView) cardView.findViewById(R.id.card_image_username);
                card_tv_username = (TextView) cardView.findViewById(R.id.card_tv_username);
                card_tv_submitTime = (TextView) cardView.findViewById(R.id.card_tv_submitTime);
                cardview_content = (TextView) cardView.findViewById(R.id.cardview_content);
                good = (ImageButton) cardView.findViewById(R.id.good);
                comment = (ImageButton) cardView.findViewById(R.id.comment);
                collect = (ImageButton) cardView.findViewById(R.id.collect);
                countGood = (TextView) cardView.findViewById(R.id.countGood);
                countComment = (TextView) cardView.findViewById(R.id.countComment);

            }
        }
    }
}
