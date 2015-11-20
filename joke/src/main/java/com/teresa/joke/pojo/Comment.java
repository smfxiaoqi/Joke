package com.teresa.joke.pojo;

/**
 * Created by Administrator on 15-11-18.
 */
public class Comment {
    //评论者
    private String commenter;
    //评论内容
    private String commenterContent;
    //被回复者
    private String replyied;
    //评论者头像
    private int commenterImage;

    public Comment() {
    }

    public Comment(int commenterImage, String replyied, String commenterContent, String commenter) {
        this.commenterImage = commenterImage;
        this.replyied = replyied;
        this.commenterContent = commenterContent;
        this.commenter = commenter;
    }

    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public String getCommenterContent() {
        return commenterContent;
    }

    public void setCommenterContent(String commenterContent) {
        this.commenterContent = commenterContent;
    }

    public String getReplyied() {
        return replyied;
    }

    public void setReplyied(String replyied) {
        this.replyied = replyied;
    }

    public int getCommenterImage() {
        return commenterImage;
    }

    public void setCommenterImage(int commenterImage) {
        this.commenterImage = commenterImage;
    }
}
