package com.opensource.component.commom.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * created by zhanglong and since  2019/12/27  5:24 下午
 * @description: 使用pool处理httpclient
 */
public class HttpClientUtils {

    public static String doGet( String url, Map<String, String> params, Header... headers ) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, Charsets.UTF_8));
            }
            HttpGet httpGet = new HttpGet(url);
            if (null != headers && headers.length > 0) {
                httpGet.setHeaders(headers);
            }
            return submit(httpGet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static String doPost( String url, Map<String, String> params, Header... headers ) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        try {
            List<NameValuePair> pairs = null;
            if (params != null && !params.isEmpty()) {
                pairs = new ArrayList<>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
            }
            HttpPost httpPost = new HttpPost(url);
            if (null != headers && headers.length > 0) {
                httpPost.setHeaders(headers);
            }
            if (pairs != null && pairs.size() > 0) {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, Charsets.UTF_8));
            }
            return submit(httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String submit( HttpUriRequest request ) throws IOException {
        CloseableHttpResponse response = HttpClientPool.DEFAULT.fetchHttpClient()
            .execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            request.abort();
            throw new RuntimeException("HttpClient,error status code :" + statusCode);
        }
        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity != null) {
            result = EntityUtils.toString(entity, Charsets.UTF_8);
        }
        EntityUtils.consume(entity);
        response.close();
        return result;
    }
}
