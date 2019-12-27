package com.opensource.component.commom.http;

import com.fasterxml.jackson.core.type.TypeReference;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;

/**
 * created by zhanglong and since  2019/12/27  1:58 ����
 * @description: ����pool
 */
public class HttpClientPool {

    public static final HttpClientPool DEFAULT = new HttpClientPool(new HttpClientPoolProperties());

    private final HttpClientBuilder httpClientBuilder;

    public HttpClientPool( HttpClientPoolProperties httpClientPoolProperties ) {
        Registry registry = RegistryBuilder.create()
            .register("http", PlainConnectionSocketFactory.INSTANCE)
            .register("https", SSLConnectionSocketFactory
                .getSystemSocketFactory()).build();
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager(
            registry);
        //Ĭ��ΪSocket����
        SocketConfig defaultSocketConfig = SocketConfig.custom()
            //tcp ���ӳ��Ż�,true
            .setTcpNoDelay(httpClientPoolProperties.isTcpNoDelay()).build();
        manager.setDefaultSocketConfig(defaultSocketConfig);
        //�����������ӳص����������,500
        manager.setMaxTotal(httpClientPoolProperties.getMaxTotal());
        //ÿ��·�ɵ�Ĭ��������ӣ�ÿ��·��ʵ�������������DefaultMaxPerRoute���ƣ���MaxTotal���������ӵ������ 500
        // ���ù�С�޷�֧�ִ󲢷�(ConnectionPoolTimeoutException) Timeout waiting for connection from pool
        manager.setDefaultMaxPerRoute(httpClientPoolProperties.getDefaultMaxPerRoute());
        // ÿ��·�ɵ����������
        // �ڴ����ӳػ�ȡ����ʱ�����Ӳ���Ծ�೤ʱ�����Ҫ����һ����֤��Ĭ��Ϊ2s,Ĭ������ 5*1000
        manager.setValidateAfterInactivity(httpClientPoolProperties.getValidateAfterInactivity());
        //Ĭ����������
        RequestConfig defaultRequestConfig = RequestConfig.custom()
            //�������ӳ�ʱʱ�䣬2s,2*1000
            .setConnectTimeout(httpClientPoolProperties.getConnectTimeout())
            //���õȴ����ݳ�ʱʱ�䣬5s 5*1000
            .setSocketTimeout(httpClientPoolProperties.getSocketTimeout())
            //���ô����ӳػ�ȡ���ӵĵȴ���ʱʱ��,2000
            .setConnectionRequestTimeout(httpClientPoolProperties.getConnectionRequestTimeout())
            .build();
        //����
        HttpClientBuilder httpClientBuilder = HttpClients.custom()
            //���ӳز��ǹ���ģʽ,true
            .setConnectionManagerShared(httpClientPoolProperties.isConnectionManagerShared());
        //���ڻ��տ������� 60
        httpClientBuilder
            .evictIdleConnections(httpClientPoolProperties.getEvictIdleConnectionsTime(),
                TimeUnit.SECONDS);
        if (httpClientPoolProperties.isEvictExpiredConnections()) {
            //���ڻ��չ������� true
            httpClientBuilder.evictExpiredConnections();
        }
        if (httpClientPoolProperties.getConnectionTimeToLive() > 0) {
            httpClientBuilder
                //���Ӵ��ʱ�䣬��������ã�����ݳ�������Ϣ���� 60
                .setConnectionTimeToLive(httpClientPoolProperties.getConnectionTimeToLive()
                    , TimeUnit.SECONDS);
        }
        if (httpClientPoolProperties.getRetryCount() > 0) {
            httpClientBuilder.setRetryHandler(
                new DefaultHttpRequestRetryHandler(
                    //�������Դ�����Ĭ����3�Σ���ǰ�ǽ��õ���������Ҫ������ 0
                    httpClientPoolProperties.getRetryCount(), false));
        }
        //����Ĭ����������
        this.httpClientBuilder = httpClientBuilder
            .setDefaultRequestConfig(defaultRequestConfig)
            .setConnectionManager(manager)
            //�������ò��ԣ����Ƿ���keepAlive
            .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
            //���������ã�����ȡ�����������೤ʱ��
            .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE);

    }

    public CloseableHttpClient fetchHttpClient() {
        return httpClientBuilder.build();
    }


}
