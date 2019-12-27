package com.opensource.component.dinging;


import com.opensource.component.dinging.configuration.DingingProperties;
import com.opensource.component.dinging.message.BaseDingMessage;
import com.opensource.component.dinging.message.LinkMessage;
import com.opensource.component.dinging.message.MarkdownMessage;
import com.opensource.component.dinging.message.TextMessage;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * created by zhanglong and since  2019/12/27  1:43
 *
 * @description: 描述
 */
public class DindingProvider {

    private RestTemplate restTemplate;

    private DingingProperties dingingProperties;

    public DindingProvider( RestTemplate restTemplate, DingingProperties dingingProperties ) {
        this.restTemplate = restTemplate;
        this.dingingProperties = dingingProperties;
    }

    /**
     * 发送普通文本消息
     *
     * @param content 消息内容
     * @param atMobiles 被@人的手机号
     */
    public void sendTextMessage( String content, String[] atMobiles ) {
        TextMessage textMessage = new TextMessage(content, atMobiles);
        sendContent(textMessage);
    }

    /**
     * 发送link类型消息
     *
     * @param title 消息标题
     * @param text 消息内容。如果太长只会部分展示
     * @param picUrl 图片URL
     * @param messageUrl 点击消息跳转的URL
     */
    public void sendLinkMessage( String title, String text, String picUrl,
        String messageUrl ) {
        LinkMessage linkMessage = new LinkMessage(title, text, picUrl, messageUrl);
        sendContent(linkMessage);
    }

    /**
     * 发送markdown类型消息
     *
     * @param title 首屏会话透出的展示内容
     * @param text markdown格式的消息
     * @param atMobiles 被@人的手机号(在text内容里要有@手机号)
     */
    public void sendMarkdownMessage( String title, String text,
        String[] atMobiles ) {
        MarkdownMessage markdownMessage = new MarkdownMessage(title, text, atMobiles);
        sendContent(markdownMessage);
    }


    private <T extends BaseDingMessage> void sendContent( T dingMessage ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(dingMessage.toMessageMap(), headers);
        final String[] accessTokens = dingingProperties.getAccessTokens().split(",");
        for (String accessToken : accessTokens) {
            restTemplate
                .postForEntity(dingingProperties.getDingingUrl() + "?access_token=" + accessToken,
                    entity, Void.class);
        }
    }


}
