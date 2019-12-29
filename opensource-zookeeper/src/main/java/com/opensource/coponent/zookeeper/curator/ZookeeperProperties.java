package com.opensource.coponent.zookeeper.curator;

import java.util.Objects;

/**
 * created by zhanglong and since  2019/12/29  11:40 ����
 *
 * @description: ����
 */
public class ZookeeperProperties {
    /**
     * description ���Եȴ�ʱ��
     */
    private int baseSleepTimeMs = 1000;
    /**
     * description ���Դ���
     */
    private int maxRetries = 3;

    /**
     * description �������ӵ�ַ
     */
    private String connectString = "localhost:2181";
    /**
     * description �Ự��ʱʱ��
     */
    private int sessionTimeoutMs = 30000;
    /**
     * description ���ӳ�ʱʱ��
     */
    private int connectionTimeoutMs = 6000;

    public int getBaseSleepTimeMs() {
        return baseSleepTimeMs;
    }

    public void setBaseSleepTimeMs( int baseSleepTimeMs ) {
        this.baseSleepTimeMs = baseSleepTimeMs;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries( int maxRetries ) {
        this.maxRetries = maxRetries;
    }

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString( String connectString ) {
        this.connectString = connectString;
    }

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs( int sessionTimeoutMs ) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs( int connectionTimeoutMs ) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    @Override
    public boolean equals( Object o ) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ZookeeperProperties that = (ZookeeperProperties) o;
        return baseSleepTimeMs == that.baseSleepTimeMs &&
            maxRetries == that.maxRetries &&
            sessionTimeoutMs == that.sessionTimeoutMs &&
            connectionTimeoutMs == that.connectionTimeoutMs &&
            Objects.equals(connectString, that.connectString);
    }

    @Override
    public int hashCode() {
        return Objects
            .hash(baseSleepTimeMs, maxRetries, connectString, sessionTimeoutMs,
                connectionTimeoutMs);
    }

    @Override
    public String toString() {
        return "ZookeeperProperties{" +
            "baseSleepTimeMs=" + baseSleepTimeMs +
            ", maxRetries=" + maxRetries +
            ", connectString='" + connectString + '\'' +
            ", sessionTimeoutMs=" + sessionTimeoutMs +
            ", connectionTimeoutMs=" + connectionTimeoutMs +
            '}';
    }
}
