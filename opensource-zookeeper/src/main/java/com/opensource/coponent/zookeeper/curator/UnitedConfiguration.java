package com.opensource.coponent.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * created by zhanglong and since  2019/12/29  11:23 …œŒÁ
 *
 * @author zhanglong
 * @description: √Ë ˆ
 */
public class UnitedConfiguration {

    public UnitedConfiguration() {
        initZookeeperProperties();
        initCuratorFramework();
    }

    private ZookeeperProperties zookeeperProperties;

    private CuratorFramework curatorFramework;

    public ZookeeperProperties initZookeeperProperties() {
        zookeeperProperties = new ZookeeperProperties();
        return zookeeperProperties;
    }

    public RetryPolicy initRetryPolicy() {
        return new ExponentialBackoffRetry(zookeeperProperties.getBaseSleepTimeMs(),
            zookeeperProperties.getMaxRetries());
    }

    public CuratorFramework initCuratorFramework() {
       curatorFramework = CuratorFrameworkFactory
            .newClient(zookeeperProperties.getConnectString(),
                zookeeperProperties.getSessionTimeoutMs(),
                zookeeperProperties.getConnectionTimeoutMs(), initRetryPolicy());
        curatorFramework.start();
        return curatorFramework;
    }

    public ZkOperationBase initZkOperationBase() {
        return new ZkOperationBase(initCuratorFramework());
    }
    public ZkDistributedLocker initZkDistributedLocker() {
        return new ZkDistributedLocker(initCuratorFramework());
    }

}
