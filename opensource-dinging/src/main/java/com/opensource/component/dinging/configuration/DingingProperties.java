package com.opensource.component.dinging.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

/**
 * created by zhanglong and since  2019/12/27  10:09 下午
 *
 * @description: 描述
 */
@ConfigurationProperties(prefix = "opensource.component.dinging")
public class DingingProperties {

    private String dingingUrl = "https://oapi.dingtalk.com/robot/send";

    private String accessTokens;

    public String getDingingUrl() {
        if (StringUtils.isEmpty(accessTokens)) {
            throw new NullPointerException("dingingUrl could not be blank");
        }
        return dingingUrl;
    }

    public void setDingingUrl( String dingingUrl ) {
        this.dingingUrl = dingingUrl;
    }

    public String getAccessTokens() {
        if (StringUtils.isEmpty(accessTokens)) {
            throw new NullPointerException(
                "access_tokens must be at least of one value, more is split by ','");
        }
        return accessTokens;
    }

    public void setAccessTokens( String accessTokens ) {
        this.accessTokens = accessTokens;
    }
}
