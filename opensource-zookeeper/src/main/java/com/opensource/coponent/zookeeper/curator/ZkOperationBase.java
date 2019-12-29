package com.opensource.coponent.zookeeper.curator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * created by zhanglong and since  2019/12/29  11:37 上午
 *
 * @description: 所有的path要以路径形式传递
 */
public class ZkOperationBase {

    private CuratorFramework curatorFramework;

    public ZkOperationBase( CuratorFramework curatorFramework ) {
        this.curatorFramework = curatorFramework;
    }
    /**
     * created by zhanglong and since  2019/12/29 12:45 下午
     * @param path 必须以/开始
     */
    public boolean isExistsZKPath(String path) throws Exception {
        Stat stat = curatorFramework.checkExists().forPath(path);
        if (stat == null) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 获取该ZKPath 的data
     * @param path 必须以/开始
     * @return data
     * @throws Exception
     */
    public String getDataFromZKPath(String path) throws Exception {
        // 当没有该Path, 直接返回
        if (!isExistsZKPath(path)) {
            return null;
        }
        return new String(curatorFramework.getData().forPath(path), StandardCharsets.UTF_8);
    }

    /**
     * 创建Zkpath 下面的数据
     * 1.当该Path下没有数据时，则创建新的
     * 2.当其有值则更新
     *
     * @param path 必须以/开始
     * @param data 数据
     * @param createMode 数据节点类型
     * @throws Exception
     */
    public void setDataForZKPath(String path, String data, CreateMode createMode) throws Exception {
        // 当没有该path，则新增一个; 否则更新
        if (!isExistsZKPath(path)) {
            curatorFramework.create().creatingParentsIfNeeded().withMode(createMode).forPath(path, data.getBytes());
        } else {
            updateDataForZKPath(path, data, createMode);
        }
    }

    /**
     *
     * 创建Zkpath 下面的数据
     * 1.当该Path下没有数据时，则创建新的
     * 2.当其有值则添加到尾部
     *
     * @param path 必须以/开始
     * @param data 数据
     * @param createMode 数据节点类型
     * @throws Exception
     */
    public void appendDataForZKPath(String path, String data, CreateMode createMode) throws Exception {
        // 当没有该path，则新增一个; 否则更新
        if (!isExistsZKPath(path)) {
            curatorFramework.create().creatingParentsIfNeeded().withMode(createMode).forPath(path, data.getBytes());
        } else {
            String sourceData = getDataFromZKPath(path);
            StringBuffer sb = new StringBuffer();
            sb.append(sourceData).append(data);
            updateDataForZKPath(path, sb.toString(), createMode);
        }
    }

    /**
     * 当其没有改路径时，则创建
     *
     * @param path 以/开始
     * @param createMode 数据节点类型
     * @throws Exception
     */
    public void createIfNotExistsZKPath(String path, CreateMode createMode) throws Exception {

        Stat stat = curatorFramework.checkExists().forPath(path);
        if (stat == null) {
            curatorFramework.create().creatingParentsIfNeeded().withMode(createMode).forPath(path);
        }
    }

    /**
     * 更新ZkPath 临时数据
     * @param path 必须以/开始
     * @param data 数据
     * @param createMode 数据节点类型
     * @throws Exception
     */
    public void updateDataForZKPath(String path, String data, CreateMode createMode) throws Exception {
        createIfNotExistsZKPath(path, createMode);
        curatorFramework.setData().forPath(path, data.getBytes());
    }

    /**
     * 删除ZKPath 的数据
     * @param path 必须以/开始
     * @throws Exception
     */
    public void removeDataForZKPath(String path) throws Exception {
        if (!isExistsZKPath(path)) {
            return;
        }
        curatorFramework.delete().forPath(path);
    }

    /**
     * 获取指定ZKPath的子节点 path
     * @param path 必须以/开始
     * @return data
     * @throws Exception
     */
    public List<String> getChildrenPath(String path) throws Exception {

        if (!isExistsZKPath(path)) {
            return Lists.newArrayList();
        }

        List<String> pathList = Lists.newArrayList();
        recursionChildrenPath(path, pathList);
        return pathList;
    }

    /**
     * 递归获取子节点的全系路径
     * @param path 必须以/开始
     * @param pathList 必须以/开始
     * @throws Exception
     */
    private void recursionChildrenPath(String path, List<String> pathList) throws Exception {

        for (String tempPath : curatorFramework.getChildren().forPath(path)) {
            String childPath = FilenameUtils.concat(path, tempPath);
            pathList.add(childPath);
            recursionChildrenPath(childPath, pathList);
        }
    }

    /**
     * 获取指定ZKPath的子节点的path与数据映射
     * @param path 必须以/开始
     * @return
     * @throws Exception
     */
    public Map<String, String> getChildrenDataByPath(String path) throws Exception {

        List<String> childrenPathList = getChildrenPath(path);
        Map<String, String> pathAndDataMap = Maps.newHashMap();

        for (String childrenPath : childrenPathList) {
            String data = getDataFromZKPath(childrenPath);
            if (StringUtils.isNotEmpty(data)) {
                pathAndDataMap.put(childrenPath, data);
            }
        }
        return pathAndDataMap;
    }

    public static void main( String[] args ) throws Exception {
        final UnitedConfiguration unitedConfiguration = new UnitedConfiguration();
        unitedConfiguration.initZookeeperProperties();
        final CuratorFramework curatorFramework = unitedConfiguration.initCuratorFramework();
        final ZkOperationBase zkOperationBase = new ZkOperationBase(curatorFramework);
        zkOperationBase.removeDataForZKPath("/zhanglong");
        System.out.println("zhanglong>>>>>>"+zkOperationBase.isExistsZKPath("/zhanglong"));
        zkOperationBase.setDataForZKPath("/zhanglong", "zhanglong-node", CreateMode.PERSISTENT);
        System.out.println("zhanglong>>>>>>"+zkOperationBase.isExistsZKPath("/zhanglong"));
        System.out.println("node:"+zkOperationBase.getDataFromZKPath("/zhanglong"));
        System.out.println("node:"+zkOperationBase.getDataFromZKPath("/zhanglo"));
        zkOperationBase.removeDataForZKPath("/zhanglong");
    }
}
