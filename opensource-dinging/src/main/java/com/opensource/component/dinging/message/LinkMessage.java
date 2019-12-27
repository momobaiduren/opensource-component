package com.opensource.component.dinging.message;

import com.opensource.component.dinging.message.enums.MessageType;
import java.util.HashMap;
import java.util.Map;
import org.springframework.util.StringUtils;

/**
 * created by zhanglong and since  2019/12/27  10:03 下午
 *
 * @description: 描述
 */
public class LinkMessage extends BaseDingMessage {

    /**
     * 消息简介
     */
    private String text;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 封面图片URL
     */
    private String picUrl;

    /**
     * 消息跳转URL
     */
    private String messageUrl;

    public LinkMessage( String title, String text, String messageUrl ) {
        this.text = text;
        this.title = title;
        this.messageUrl = messageUrl;
        this.msgtype = MessageType.link;
    }

    public LinkMessage( String title, String text, String messageUrl, String picUrl ) {
        this.text = text;
        this.title = title;
        this.picUrl = picUrl;
        this.messageUrl = messageUrl;
        this.msgtype = MessageType.link;
    }


    @Override
    public Map<String, Object> toMessageMap() {

        if (StringUtils.isEmpty(this.messageUrl) || StringUtils.isEmpty(this.title) ||
            StringUtils.isEmpty(this.text) || MessageType.link != msgtype) {
            throw new IllegalArgumentException("please check the necessary parameters!");
        }

        Map<String, Object> resultMap = new HashMap<>(8);
        resultMap.put("msgtype", this.msgtype);
        HashMap<String, String> linkItems = new HashMap<>(8);
        linkItems.put("title", this.title);
        linkItems.put("text", this.text);
        linkItems.put("picUrl", this.picUrl);
        linkItems.put("messageUrl", this.messageUrl);
        resultMap.put("link", linkItems);

        return resultMap;
    }

    public String getText() {
        return text;
    }

    public void setText( String text ) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl( String picUrl ) {
        this.picUrl = picUrl;
    }

    public String getMessageUrl() {
        return messageUrl;
    }

    public void setMessageUrl( String messageUrl ) {
        this.messageUrl = messageUrl;
    }
}
