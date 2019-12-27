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
public class TextMessage extends BaseDingMessage {

    /**
     * 文本消息的具体内容
     */
    private String content;

    /**
     * 可以通过群成员的绑定手机号来艾特具体的群成员
     */
    private String[] atMobiles;

    /**
     * 是否艾特所有人 也可以设置isAtAll=true来艾特所有人
     */
    private boolean isAtAll;

    public TextMessage( String content ) {
        this.content = content;
        this.msgtype = MessageType.text;
    }

    public TextMessage( String content, String[] atMobiles ) {
        this.content = content;
        this.atMobiles = atMobiles;
        this.msgtype = MessageType.text;
    }

    public TextMessage( String content, boolean isAtAll ) {
        this.content = content;
        this.isAtAll = isAtAll;
        this.msgtype = MessageType.text;
    }

    @Override
    public Map<String, Object> toMessageMap() {

        if (StringUtils.isEmpty(this.content) || MessageType.text != msgtype) {
            throw new IllegalArgumentException("please check the necessary parameters!");
        }

        HashMap<String, Object> resultMap = new HashMap<>(8);
        resultMap.put("msgtype", this.msgtype);

        HashMap<String, String> textItems = new HashMap<>(8);
        textItems.put("content", this.content);
        resultMap.put("text", textItems);

        HashMap<String, Object> atItems = new HashMap<>(8);
        atItems.put("atMobiles", this.atMobiles);
        atItems.put("isAtAll", this.isAtAll);
        resultMap.put("at", atItems);

        return resultMap;
    }

}
