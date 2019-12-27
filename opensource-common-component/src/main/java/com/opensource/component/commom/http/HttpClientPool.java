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
 * created by zhanglong and since  2019/12/27  1:58 下午
 * @description: 定义pool
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
        //默认为Socket配置
        SocketConfig defaultSocketConfig = SocketConfig.custom()
            //tcp 包延迟优化,true
            .setTcpNoDelay(httpClientPoolProperties.isTcpNoDelay()).build();
        manager.setDefaultSocketConfig(defaultSocketConfig);
        //设置整个连接池的最大连接数,500
        manager.setMaxTotal(httpClientPoolProperties.getMaxTotal());
        //每个路由的默认最大连接，每个路由实际最大连接数由DefaultMaxPerRoute控制，而MaxTotal是整个池子的最大数 500
        // 设置过小无法支持大并发(ConnectionPoolTimeoutException) Timeout waiting for connection from pool
        manager.setDefaultMaxPerRoute(httpClientPoolProperties.getDefaultMaxPerRoute());
        // 每个路由的最大连接数
        // 在从连接池获取连接时，连接不活跃多长时间后需要进行一次验证，默认为2s,默认设置 5*1000
        manager.setValidateAfterInactivity(httpClientPoolProperties.getValidateAfterInactivity());
        //默认请求配置
        RequestConfig defaultRequestConfig = RequestConfig.custom()
            //设置连接超时时间，2s,2*1000
            .setConnectTimeout(httpClientPoolProperties.getConnectTimeout())
            //设置等待数据超时时间，5s 5*1000
            .setSocketTimeout(httpClientPoolProperties.getSocketTimeout())
            //设置从连接池获取连接的等待超时时间,2000
            .setConnectionRequestTimeout(httpClientPoolProperties.getConnectionRequestTimeout())
            .build();
        //创建
        HttpClientBuilder httpClientBuilder = HttpClients.custom()
            //连接池不是共享模式,true
            .setConnectionManagerShared(httpClientPoolProperties.isConnectionManagerShared());
        //定期回收空闲连接 60
        httpClientBuilder
            .evictIdleConnections(httpClientPoolProperties.getEvictIdleConnectionsTime(),
                TimeUnit.SECONDS);
        if (httpClientPoolProperties.isEvictExpiredConnections()) {
            //定期回收过期连接 true
            httpClientBuilder.evictExpiredConnections();
        }
        if (httpClientPoolProperties.getConnectionTimeToLive() > 0) {
            httpClientBuilder
                //连接存活时间，如果不设置，则根据长连接信息决定 60
                .setConnectionTimeToLive(httpClientPoolProperties.getConnectionTimeToLive()
                    , TimeUnit.SECONDS);
        }
        if (httpClientPoolProperties.getRetryCount() > 0) {
            httpClientBuilder.setRetryHandler(
                new DefaultHttpRequestRetryHandler(
                    //设置重试次数，默认是3次，当前是禁用掉（根据需要开启） 0
                    httpClientPoolProperties.getRetryCount(), false));
        }
        //设置默认请求配置
        this.httpClientBuilder = httpClientBuilder
            .setDefaultRequestConfig(defaultRequestConfig)
            .setConnectionManager(manager)
            //连接重用策略，即是否能keepAlive
            .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
            //长连接配置，即获取长连接生产多长时间
            .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE);

    }

    public CloseableHttpClient fetchHttpClient() {
        return httpClientBuilder.build();
    }


}
