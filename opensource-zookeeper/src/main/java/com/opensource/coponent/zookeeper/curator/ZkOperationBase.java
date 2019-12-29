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
 * created by zhanglong and since  2019/12/29  11:37 ����
 *
 * @description: ���е�pathҪ��·����ʽ����
 */
public class ZkOperationBase {

    private CuratorFramework curatorFramework;

    public ZkOperationBase( CuratorFramework curatorFramework ) {
        this.curatorFramework = curatorFramework;
    }
    /**
     * created by zhanglong and since  2019/12/29 12:45 ����
     * @param path ������/��ʼ
     */
    public boolean isExistsZKPath(String path) throws Exception {
        Stat stat = curatorFramework.checkExists().forPath(path);
        if (stat == null) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * ��ȡ��ZKPath ��data
     * @param path ������/��ʼ
     * @return data
     * @throws Exception
     */
    public String getDataFromZKPath(String path) throws Exception {
        // ��û�и�Path, ֱ�ӷ���
        if (!isExistsZKPath(path)) {
            return null;
        }
        return new String(curatorFramework.getData().forPath(path), StandardCharsets.UTF_8);
    }

    /**
     * ����Zkpath ���������
     * 1.����Path��û������ʱ���򴴽��µ�
     * 2.������ֵ�����
     *
     * @param path ������/��ʼ
     * @param data ����
     * @param createMode ���ݽڵ�����
     * @throws Exception
     */
    public void setDataForZKPath(String path, String data, CreateMode createMode) throws Exception {
        // ��û�и�path��������һ��; �������
        if (!isExistsZKPath(path)) {
            curatorFramework.create().creatingParentsIfNeeded().withMode(createMode).forPath(path, data.getBytes());
        } else {
            updateDataForZKPath(path, data, createMode);
        }
    }

    /**
     *
     * ����Zkpath ���������
     * 1.����Path��û������ʱ���򴴽��µ�
     * 2.������ֵ����ӵ�β��
     *
     * @param path ������/��ʼ
     * @param data ����
     * @param createMode ���ݽڵ�����
     * @throws Exception
     */
    public void appendDataForZKPath(String path, String data, CreateMode createMode) throws Exception {
        // ��û�и�path��������һ��; �������
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
     * ����û�и�·��ʱ���򴴽�
     *
     * @param path ��/��ʼ
     * @param createMode ���ݽڵ�����
     * @throws Exception
     */
    public void createIfNotExistsZKPath(String path, CreateMode createMode) throws Exception {

        Stat stat = curatorFramework.checkExists().forPath(path);
        if (stat == null) {
            curatorFramework.create().creatingParentsIfNeeded().withMode(createMode).forPath(path);
        }
    }

    /**
     * ����ZkPath ��ʱ����
     * @param path ������/��ʼ
     * @param data ����
     * @param createMode ���ݽڵ�����
     * @throws Exception
     */
    public void updateDataForZKPath(String path, String data, CreateMode createMode) throws Exception {
        createIfNotExistsZKPath(path, createMode);
        curatorFramework.setData().forPath(path, data.getBytes());
    }

    /**
     * ɾ��ZKPath ������
     * @param path ������/��ʼ
     * @throws Exception
     */
    public void removeDataForZKPath(String path) throws Exception {
        if (!isExistsZKPath(path)) {
            return;
        }
        curatorFramework.delete().forPath(path);
    }

    /**
     * ��ȡָ��ZKPath���ӽڵ� path
     * @param path ������/��ʼ
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
     * �ݹ��ȡ�ӽڵ��ȫϵ·��
     * @param path ������/��ʼ
     * @param pathList ������/��ʼ
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
     * ��ȡָ��ZKPath���ӽڵ��path������ӳ��
     * @param path ������/��ʼ
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
