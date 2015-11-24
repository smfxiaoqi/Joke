package com.teresa.joke.pojo;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 15-11-17.
 */
public class MyCardView extends CardView{
    //卡片用户头像
    private int card_image_username;
    //卡片用户名
    private String card_tv_username;
    //卡片发布时间
    private String card_tv_submitTime;
    //卡片文字内容
    private String cardview_content;
    //卡片里的点赞数、评论数
    private int countGood;
    private int countComment;
    //内容是图片的卡片
    private int card_image_content;

    public int getCard_image_content() {
        return card_image_content;
    }

    public void setCard_image_content(int card_image_content) {
        this.card_image_content = card_image_content;
    }

    public int getCountGood() {
        return countGood;
    }

    public void setCountGood(int countGood) {
        this.countGood = countGood;
    }

    public int getCountComment() {
        return countComment;
    }

    public void setCountComment(int countComment) {
        this.countComment = countComment;
    }

    public MyCardView(Context context) {
        super(context);
    }

    public int getCard_image_username() {
        return card_image_username;
    }

    public void setCard_image_username(int card_image_username) {
        this.card_image_username = card_image_username;
    }

    public String getCard_tv_username() {
        return card_tv_username;
    }

    public void setCard_tv_username(String card_tv_username) {
        this.card_tv_username = card_tv_username;
    }

    public String getCard_tv_submitTime() {
        return card_tv_submitTime;
    }

    public void setCard_tv_submitTime(String card_tv_submitTime) {
        this.card_tv_submitTime = card_tv_submitTime;
    }

    public String getCardview_content() {
        return cardview_content;
    }

    public void setCardview_content(String cardview_content) {
        this.cardview_content = cardview_content;
    }

}
