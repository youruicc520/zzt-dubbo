package com.cisc.dubbo.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author: Administrator
 * @date: 2021/05/27
 * @description:分布式锁工具类
 */
public class DistributedLock {
    /**
     * zk客户端
     */
    private CuratorFramework client = null;
    final static Logger log = LoggerFactory.getLogger(DistributedLock.class);
    /**
     * 用于挂起当前请求,并且等待上一个分布式锁释放
     */
    private static CountDownLatch zkLockLatch = new CountDownLatch(1);
    /**
     * 分布式锁的总结点名
     */
    private static final String ZK_LOCK_PROJECT = "zzt-locks";
    /**
     * 分布式锁节点
     */
    private static final String DISTRIBUTED_LOCK = "distributed_lock";

    public DistributedLock(CuratorFramework client) {
        this.client = client;
    }

    /**
     * 初始化锁
     */
    public void init(){
        /**
         * 创建zk锁的总结点,相当于eclipse的工作空间下的项目
         *      ZKLocks-Namespace
         *          |
         *          - zzt-locks
         *              |
         *              -distributed_lock
         */
        try {
            if(client.checkExists().forPath("/"+ZK_LOCK_PROJECT) == null){
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath("/"+ZK_LOCK_PROJECT);
            }
            //针对zk的分布式锁节点,创建相应的watch时间监听
            addWatcherLock("/" + ZK_LOCK_PROJECT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLock(){
        //使用死循环,当且仅当上一个锁释放并且当前请求获得锁成功后才会跳出
        while (true) {
            try {
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath("/"+ZK_LOCK_PROJECT+"/"+DISTRIBUTED_LOCK);
                log.info("获得分布式锁成功...");
                //如果锁的节点能被创建成功,则锁没有被占用
                return;
            } catch (Exception e) {
                log.info("获得分布式锁失败...");
                try {
                    //如果没有获取到锁,需要重新设置同步资源值
                    if(zkLockLatch.getCount() <= 0){
                        zkLockLatch = new CountDownLatch(1);
                    }
                    //阻塞线程
                    zkLockLatch.await();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建watcher监听
     * @param path
     */
    public void addWatcherLock(String path) throws Exception{
        final PathChildrenCache cache = new PathChildrenCache(client,path,true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                if(event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)){
                    String path = event.getData().getPath();
                    log.info("上一个会话已释放或该会话已断开,节点路径为: " + path);
                    if(path.contains(DISTRIBUTED_LOCK)){
                        log.info("释放计数器,让当前请求来获得分布式锁...");
                        zkLockLatch.countDown();
                    }
                }
            }
        });
    }

    /**
     * 释放分布式锁
     * @return
     */
    public boolean releaseLock(){
        try {
            if(client.checkExists().forPath("/" + ZK_LOCK_PROJECT + "/" + DISTRIBUTED_LOCK) !=null){
                client.delete().forPath("/" + ZK_LOCK_PROJECT + "/" + DISTRIBUTED_LOCK);
            }
        } catch (Exception e) {

            return false;
        }
        log.info("分布式锁释放完毕...");
        return true;
    }
}
