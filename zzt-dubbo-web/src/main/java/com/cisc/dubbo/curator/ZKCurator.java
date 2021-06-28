package com.cisc.dubbo.curator;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: Administrator
 * @date: 2021/05/27
 * @description:
 */
public class ZKCurator {
    /**
     *   zk客户端
     */
    private CuratorFramework client = null;
    final static Logger log = LoggerFactory.getLogger(ZKCurator.class);

    public ZKCurator(CuratorFramework client) {
        this.client = client;
    }

    /**
     * 初始化操作
     */
    public void init(){
        //使用命名空间
        client = client.usingNamespace("zk-curator-connector");
    }

    /**
     * 判断zk是否连接
     * @return
     */
    public boolean isZKAlive(){
        return client.isStarted();
    }
}
