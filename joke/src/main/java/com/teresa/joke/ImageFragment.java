package com.teresa.joke;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import com.teresa.joke.util.ImageTools;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {
    private RecyclerView imageRecyclerView;
    private List<MyCardView> imageCardDatas;
    private ImageCardAdapter imageCardAdapter;

    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_image, container, false);
        imageCardDatas = new ArrayList<>();
        initData();
        imageRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_image);
        imageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        imageCardAdapter = new ImageCardAdapter();
        imageRecyclerView.setAdapter(imageCardAdapter);
        return view;
    }
    private void initData() {
        MyCardView imageCard1 = new MyCardView(getActivity());
        MyCardView imageCard2 = new MyCardView(getActivity());
        MyCardView imageCard3 = new MyCardView(getActivity());
        MyCardView imageCard4=new MyCardView(getActivity());

        imageCard1.setCard_image_username(R.drawable.lock);
        imageCard1.setCard_tv_submitTime("2011-5-16");
        imageCard1.setCard_tv_username("haha");
        imageCard1.setCountGood(345);
        imageCard1.setCountComment(112);
        imageCard1.setCard_image_content(R.drawable.card1);

        imageCard2.setCard_image_username(R.drawable.jiesuo);
        imageCard2.setCard_tv_submitTime("2016-5-6");
        imageCard2.setCard_tv_username("hengheng");
        imageCard2.setCountGood(569);
        imageCard2.setCountComment(3);
        imageCard2.setCard_image_content(R.drawable.card2);

        imageCard3.setCard_image_username(R.drawable.gerenxinxi);
        imageCard3.setCard_tv_submitTime("2013-8-6");
        imageCard3.setCard_tv_username("heihei");
        imageCard3.setCountGood(50);
        imageCard3.setCountComment(20);
        imageCard3.setCard_image_content(R.drawable.card3);

        imageCard4.setCard_image_username(R.drawable.lock);
        imageCard4.setCard_tv_submitTime("2011-5-16");
        imageCard4.setCard_tv_username("haha");
        imageCard4.setCountGood(345);
        imageCard4.setCountComment(112);
        imageCard4.setCard_image_content(R.drawable.card4);

        imageCardDatas.add(imageCard1);
        imageCardDatas.add(imageCard2);
        imageCardDatas.add(imageCard3);
        imageCardDatas.add(imageCard4);
    }
public class ImageCardAdapter extends RecyclerView.Adapter<ImageCardAdapter.MyHoldView>{
    @Override
    public MyHoldView onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHoldView myViewHolder = new MyHoldView(LayoutInflater.from(getActivity()).
                inflate(R.layout.item_image_card_recycler, parent, false));
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(final MyHoldView holder,final int position) {
        holder.card_tv_username.setText(imageCardDatas.get(position).getCard_tv_username());
        holder.card_image_username.setImageResource(imageCardDatas.get(position).getCard_image_username());
        holder.card_tv_submitTime.setText(imageCardDatas.get(position).getCard_tv_submitTime());
        holder.cardview_image_content.setImageResource(imageCardDatas.get(position).getCard_image_content());
        //当前点赞数
        final int currentCountGood = imageCardDatas.get(position).getCountGood();
        holder.countGood.setText(String.valueOf(currentCountGood));
        //当前评论数
        int currentCountComment = imageCardDatas.get(position).getCountComment();
        holder.countComment.setText(String.valueOf(currentCountComment));
        //点赞监听
        holder.good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击之后countGood+1，图片改变
                //发送给服务器的数据+1，界面上的数据+1
                imageCardDatas.get(position).setCountGood(currentCountGood + 1);
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
//        holder.comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                //设置此登录用户的头像
//                // builder.setIcon(R.drawable.manng);
//                ////设置此登录用户的用户名
//                //builder.setTitle("username");
//                AlertDialog dialogComment = builder.create();
//                dialogComment.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//                dialogComment.show();
//                Window windowComment = dialogComment.getWindow();
//                windowComment.setContentView(R.layout.dialog_comment);
//                ImageButton ok = (ImageButton) windowComment.findViewById(R.id.ok);
//                ImageView comment_image_username= (ImageView) windowComment.findViewById(R.id.comment_username_image);
//                TextView comment_tv_username= (TextView) windowComment.findViewById(R.id.comment_tv_username);
//                comment_image_username.setImageResource(R.drawable.biaoqing);
//                comment_tv_username.setText("Teresa");
//                final EditText contentComment = (EditText) windowComment.findViewById(R.id.tvContent);
//                ok.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //点击ok，评论的数据封装传入数据库，同时在评论页面显示出来
//                        //先测试
//
//                        String scontentComment=contentComment.getText().toString();
//                        contentComment.requestFocus();
//                        Toast.makeText(getActivity(), "您的评论为" + scontentComment, Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return imageCardDatas.size();
    }

    class MyHoldView extends RecyclerView.ViewHolder{
        CardView cardview_image;
        ImageView card_image_username;
        TextView card_tv_username;
        TextView card_tv_submitTime;
        ImageView cardview_image_content;
        ImageButton good;
        ImageButton comment;
        ImageButton collect;
        TextView countGood;
        TextView countComment;
        public MyHoldView(View itemView) {
            super(itemView);
            cardview_image = (CardView) itemView.findViewById(R.id.cardview_image);
            card_image_username = (ImageView) cardview_image.findViewById(R.id.card_image_username);
            card_tv_username = (TextView) cardview_image.findViewById(R.id.card_tv_username);
            card_tv_submitTime = (TextView) cardview_image.findViewById(R.id.card_tv_submitTime);
            cardview_image_content = (ImageView) cardview_image.findViewById(R.id.cardview_image_content);
            good = (ImageButton) cardview_image.findViewById(R.id.good);
            comment = (ImageButton) cardview_image.findViewById(R.id.comment);
            collect = (ImageButton) cardview_image.findViewById(R.id.collect);
            countGood = (TextView) cardview_image.findViewById(R.id.countGood);
            countComment = (TextView) cardview_image.findViewById(R.id.countComment);
        }
    }
}

}
