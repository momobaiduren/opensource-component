package com.opensource.component.dinging.configuration;

import com.sun.deploy.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.tomcat.util.codec.binary.Base64;
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

    private String accessToken;

    private String secret;

    private static final String CHARSET = "UTF-8";

    private static final String SECRET_TYPE = "HmacSHA256";

    public String getDingingUrl() {
        if (StringUtils.isEmpty(dingingUrl)) {
            throw new NullPointerException("dingingUrl could not be blank");
        }
        dingingUrl = dingingUrl + "?access_token=" + getAccessToken();
        if (!StringUtils.isEmpty(secret)) {
            try {
                dingingUrl = dingingUrl + signSecret(System.currentTimeMillis(), secret);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
                throw new IllegalArgumentException("sign secret exception");
            }
        }
        return dingingUrl;
    }

    public void setDingingUrl( String dingingUrl ) {
        this.dingingUrl = dingingUrl;
    }

    public String getAccessToken() {
        if (StringUtils.isEmpty(accessToken)) {
            throw new NullPointerException(
                "access_tokens must be at least of one value, more is split by ','");
        }
        return accessToken;
    }

    public void setAccessToken( String accessToken ) {
        this.accessToken = accessToken;
    }

    public void setSecret( String secret ) {
        this.secret = secret;
    }

    private String signSecret( long timestamp, String secret )
        throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance(SECRET_TYPE);
        mac.init(new SecretKeySpec(secret.getBytes(CHARSET), SECRET_TYPE));
        byte[] signData = mac.doFinal(stringToSign.getBytes(CHARSET));
        final String signSecret = URLEncoder
            .encode(new String(Base64.encodeBase64(signData)), CHARSET);
        return "&timestamp=" + timestamp + "&sign=" + signSecret;
    }
}
