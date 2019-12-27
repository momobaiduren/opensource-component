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
public class MarkdownMessage extends BaseDingMessage {

    /**
     * 消息简介
     */
    private String text;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 可以通过群成员的绑定手机号来艾特具体的群成员
     */
    private String[] atMobiles;

    /**
     * 是否艾特所有人 也可以设置isAtAll=true来艾特所有人
     */
    private boolean isAtAll;

    public MarkdownMessage() {
    }

    public MarkdownMessage( String title, String text ) {
        this.text = text;
        this.title = title;
        this.msgtype = MessageType.markdown;
    }

    public MarkdownMessage( String title, String text, String[] atMobiles ) {
        this.text = text;
        this.title = title;
        this.atMobiles = atMobiles;
        this.msgtype = MessageType.markdown;
    }

    public MarkdownMessage( String title, String text, boolean isAtAll ) {
        this.text = text;
        this.title = title;
        this.isAtAll = isAtAll;
        this.msgtype = MessageType.markdown;
    }

    @Override
    public Map<String, Object> toMessageMap() {

        if (StringUtils.isEmpty(this.title) || StringUtils.isEmpty(this.text) ||
            MessageType.markdown != msgtype) {
            throw new IllegalArgumentException("please check the necessary parameters!");
        }

        HashMap<String, Object> resultMap = new HashMap<>(8);
        resultMap.put("msgtype", this.msgtype);
        HashMap<String, String> markdownItems = new HashMap<>(8);
        markdownItems.put("title", this.title);
        markdownItems.put("text", this.text);
        resultMap.put("markdown", markdownItems);
        HashMap<String, Object> atItems = new HashMap<>(8);
        atItems.put("atMobiles", this.atMobiles);
        atItems.put("isAtAll", this.isAtAll);
        resultMap.put("at", atItems);

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

    public String[] getAtMobiles() {
        return atMobiles;
    }

    public void setAtMobiles( String[] atMobiles ) {
        this.atMobiles = atMobiles;
    }

    public boolean isAtAll() {
        return isAtAll;
    }

    public void setAtAll( boolean atAll ) {
        isAtAll = atAll;
    }
}
