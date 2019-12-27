package com.opensource.component.commom.http;

import java.util.Objects;

/**
 * created by zhanglong and since  2019/12/27  5:11 下午
 *
 * @description: pool的基本参数
 */
public class HttpClientPoolProperties {

    private boolean tcpNoDelay = true;

    private int maxTotal = 500;

    private int defaultMaxPerRoute = 500;

    private int validateAfterInactivity = 10000;

    private int connectTimeout = 10000;

    private int socketTimeout = 15000;

    private int connectionRequestTimeout = 2000;

    private boolean connectionManagerShared = true;

    private long evictIdleConnectionsTime = 60;

    private boolean isEvictExpiredConnections = true;

    private long connectionTimeToLive = -1;

    private int retryCount = -1;

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    public void setTcpNoDelay( boolean tcpNoDelay ) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal( int maxTotal ) {
        this.maxTotal = maxTotal;
    }

    public int getDefaultMaxPerRoute() {
        return defaultMaxPerRoute;
    }

    public void setDefaultMaxPerRoute( int defaultMaxPerRoute ) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }

    public int getValidateAfterInactivity() {
        return validateAfterInactivity;
    }

    public void setValidateAfterInactivity( int validateAfterInactivity ) {
        this.validateAfterInactivity = validateAfterInactivity;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout( int connectTimeout ) {
        this.connectTimeout = connectTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout( int socketTimeout ) {
        this.socketTimeout = socketTimeout;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout( int connectionRequestTimeout ) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public boolean isConnectionManagerShared() {
        return connectionManagerShared;
    }

    public void setConnectionManagerShared( boolean connectionManagerShared ) {
        this.connectionManagerShared = connectionManagerShared;
    }

    public long getEvictIdleConnectionsTime() {
        return evictIdleConnectionsTime;
    }

    public void setEvictIdleConnectionsTime( long evictIdleConnectionsTime ) {
        this.evictIdleConnectionsTime = evictIdleConnectionsTime;
    }

    public boolean isEvictExpiredConnections() {
        return isEvictExpiredConnections;
    }

    public void setEvictExpiredConnections( boolean evictExpiredConnections ) {
        isEvictExpiredConnections = evictExpiredConnections;
    }

    public long getConnectionTimeToLive() {
        return connectionTimeToLive;
    }

    public void setConnectionTimeToLive( long connectionTimeToLive ) {
        this.connectionTimeToLive = connectionTimeToLive;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount( int retryCount ) {
        this.retryCount = retryCount;
    }

    @Override
    public boolean equals( Object o ) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HttpClientPoolProperties that = (HttpClientPoolProperties) o;
        return tcpNoDelay == that.tcpNoDelay &&
            maxTotal == that.maxTotal &&
            defaultMaxPerRoute == that.defaultMaxPerRoute &&
            validateAfterInactivity == that.validateAfterInactivity &&
            connectTimeout == that.connectTimeout &&
            socketTimeout == that.socketTimeout &&
            connectionRequestTimeout == that.connectionRequestTimeout &&
            connectionManagerShared == that.connectionManagerShared &&
            evictIdleConnectionsTime == that.evictIdleConnectionsTime &&
            isEvictExpiredConnections == that.isEvictExpiredConnections &&
            connectionTimeToLive == that.connectionTimeToLive &&
            retryCount == that.retryCount;
    }

    @Override
    public int hashCode() {
        return Objects
            .hash(tcpNoDelay, maxTotal, defaultMaxPerRoute, validateAfterInactivity, connectTimeout,
                socketTimeout, connectionRequestTimeout, connectionManagerShared,
                evictIdleConnectionsTime, isEvictExpiredConnections, connectionTimeToLive,
                retryCount);
    }

    @Override
    public String toString() {
        return "HttpClientPoolProperties{" +
            "tcpNoDelay=" + tcpNoDelay +
            ", maxTotal=" + maxTotal +
            ", defaultMaxPerRoute=" + defaultMaxPerRoute +
            ", validateAfterInactivity=" + validateAfterInactivity +
            ", connectTimeout=" + connectTimeout +
            ", socketTimeout=" + socketTimeout +
            ", connectionRequestTimeout=" + connectionRequestTimeout +
            ", connectionManagerShared=" + connectionManagerShared +
            ", evictIdleConnectionsTime=" + evictIdleConnectionsTime +
            ", isEvictExpiredConnections=" + isEvictExpiredConnections +
            ", connectionTimeToLive=" + connectionTimeToLive +
            ", retryCount=" + retryCount +
            '}';
    }
}
