package com.opensource.coponent.zookeeper.curator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

/**
 * created by zhanglong and since  2019/12/29  12:50 下午
 *
 * @description: zookeeper分布式锁
 */
public class ZkDistributedLocker {

    private CuratorFramework curatorFramework;

    public ZkDistributedLocker( CuratorFramework curatorFramework ) {
        this.curatorFramework = curatorFramework;
    }
    /**
     * @param key  锁名称
     * @param runnable 执行锁业务
     */
    public void doLock( String key, Runnable runnable ) {
        doLock(key, 0, null, runnable);
    }
    /**
     * @param key  锁名称
     * @param waitTime 锁最大等待时间，超过等待时间自动释放执行业务
     * @param timeUnit  等待时间单位，不传默认秒
     * @param runnable 执行锁业务
     */
    public void doLock( String key, long waitTime, TimeUnit timeUnit, Runnable runnable ) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("key cloud not be blank");
        }
        key = key.startsWith("/") ? key : "/" + key;
        final InterProcessMutex lock = new InterProcessMutex(curatorFramework, key);
        try {
            if (waitTime > 0) {
                lock.acquire(waitTime, timeUnit == null ? TimeUnit.SECONDS : timeUnit);
            } else {
                lock.acquire();
            }
            runnable.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                lock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void main( String[] args ) {
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        final UnitedConfiguration unitedConfiguration = new UnitedConfiguration();
        executorService.execute(() -> {
            final ZkDistributedLocker zkDistributedLocker = unitedConfiguration
                .initZkDistributedLocker();
            zkDistributedLocker.doLock("zhanglong", () -> System.out.println("zhanglong"));
        });
        executorService.execute(() -> {
            final ZkDistributedLocker zkDistributedLocker = unitedConfiguration
                .initZkDistributedLocker();
            zkDistributedLocker.doLock("zhanglong", () -> System.out.println("zhanglong"));
        });
    }
}
