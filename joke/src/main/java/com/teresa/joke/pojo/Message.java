package com.teresa.joke.pojo;

/**系统消息
 * Created by Administrator on 15-11-27.
 */
public class Message {
    //系统消息内容
    private String messageContent;
    //系统消息是否已读
    private boolean ifRead=false;
    //系统消息编号
    private int messageNumber=1;

    public Message(String messageContent, boolean ifRead, int messageNumber) {
        this.messageContent = messageContent;
        this.ifRead = ifRead;
        this.messageNumber = messageNumber;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public boolean isIfRead() {
        return ifRead;
    }

    public void setIfRead(boolean ifRead) {
        this.ifRead = ifRead;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
    }
}
