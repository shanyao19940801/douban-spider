package com.yao.douban.proxytool.task;

import com.yao.douban.proxytool.ProxyHttpClient;
import com.yao.douban.proxytool.ProxyPool;
import com.yao.douban.proxytool.entity.Proxy;
import com.yao.douban.proxytool.proxyutil.MyIOutils;
import com.yao.douban.proxytool.proxyutil.ProxyConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2018/3/27.
 */
public class ProxySerializeTask implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(ProxySerializeTask.class);
    public void run() {
        while (ProxyHttpClient.isContinue) {
            try {
                //每十分钟进行一次序列化
                Thread.sleep(1000 * 10 * 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<Proxy> proxyArray = null;
            ProxyPool.lock.readLock().lock();
            try {
                proxyArray = new ArrayList<Proxy>();
                int i = 0;
                for (Proxy proxy : ProxyPool.proxySet) {
                    proxyArray.add(proxy);
                }
                MyIOutils.serializeObject(proxyArray, ProxyConstants.PROXYSER_FILE_NMAE);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                ProxyPool.lock.readLock().unlock();
            }
        }
    }
}
