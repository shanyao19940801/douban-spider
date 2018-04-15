package com.yao.spider.common.task;

import com.yao.spider.core.constants.ProxyConstants;
import com.yao.spider.core.util.MyIOutils;
import com.yao.spider.proxytool.ProxyPool;
import com.yao.spider.proxytool.entity.Proxy;
import com.yao.spider.zhihu.ZhiHuHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.DelayQueue;

/**
 * Created by shanyao on 2018/4/1.
 */
public class GetProxyTask implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(GetProxyTask.class);
    public void run() {
        int wokerQueueCount = 0;
        long finishedTaskCount = 0;
        while (true) {
            wokerQueueCount = ZhiHuHttpClient.getInstance().getUserListDownTask().getQueue().size();
            finishedTaskCount = ZhiHuHttpClient.getInstance().getUserListDownTask().getCompletedTaskCount();
            logger.info("进入代理管理线程");
            logger.info("当前队列中任务数量：" + wokerQueueCount + "--- 已经完成task数量：" + finishedTaskCount);
            try {
                Thread.currentThread().sleep(30000);

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (ProxyPool.proxyQueue.size() < 50) {
                logger.info("当前代理不够重新反序列化代理");
                try {
                    List<Proxy> proxyList = (List<Proxy>) MyIOutils.deserializeObject(ProxyConstants.PROXYSER_FILE_NMAE);
                    if (proxyList != null) {
                        ProxyPool.proxyQueue.addAll(new DelayQueue<Proxy>(proxyList));
                        logger.info("反序列化后代理数量：" + ProxyPool.proxyQueue.size());
                    }
                } catch (Exception e) {
                }
            }
        }
    }
}
