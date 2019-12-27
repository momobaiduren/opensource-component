package com.opensource.component.dinging.message;

import com.opensource.component.dinging.message.enums.MessageType;
import java.util.Map;

/**
 * created by zhanglong and since  2019/12/27  10:23 下午
 *
 * @author mymac
 * @description: 描述
 */
public abstract class BaseDingMessage {

    public MessageType msgtype;

    public abstract Map<String, Object> toMessageMap();

}
