package com.yao.spider.proxytool.task;

import com.yao.spider.proxytool.ProxyHttpClient;
import com.yao.spider.proxytool.ProxyPool;
import com.yao.spider.core.entity.Page;
import com.yao.spider.proxytool.entity.Proxy;
import com.yao.spider.core.http.util.HttpClientUtil;
import com.yao.spider.core.constants.ProxyConstants;
import com.yao.spider.core.util.ProxyUtil;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by 单耀 on 2018/1/28.
 * 测试代理任务
 * 测试下载的代理是否可用
 */
public class ProxyTestTask implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(ProxyTestTask.class);
    private Proxy proxy;

    public ProxyTestTask(Proxy proxy) {
        this.proxy = proxy;
    }

    public void run() {
        HttpGet request = new HttpGet(ProxyConstants.PROXYTEST_URL);
        try {
            HttpHost proxyHost = new HttpHost(proxy.getIp(), proxy.getPort());
            RequestConfig requestConfig = HttpClientUtil.getRequestConfigBuilder().setProxy(proxyHost).build();
            request.setConfig(requestConfig);
//            CloseableHttpResponse response = HttpClientUtil.getResponse(request);
            Page page = ProxyHttpClient.getInstance().getPage(request);
            String logStr = Thread.currentThread().getName() + " " + proxy.getProxyStr() +
                    "  executing request " + page.getUrl()  + " response statusCode:" + page.getStatusCode();

            if (page == null || page.getStatusCode() != 200) {
//                logger.warn("该代理不可用：" + logStr);
                return;
            }
            if (page.getStatusCode() == 200) {
                ProxyPool.proxyQueue.add(proxy);
                logger.debug(proxy.getProxyStr() + "-----代理可用-----");
//                logger.debug(proxy.toString() + "--------" + page.toString());
                ProxyPool.lock.writeLock().lock();
                ProxyPool.proxySet.add(proxy);
                ProxyPool.lock.writeLock().unlock();
                System.out.println("目前可用代理数量:"+ProxyPool.proxyQueue.size());
            }
        } catch (IOException e) {
//            logger.debug("IOException", e);
        } finally {
            if (request != null) {
                request.releaseConnection();
            }

            if (proxy != null && !ProxyUtil.isDiscardProxy(proxy)){
                ProxyPool.proxyQueue.add(proxy);
            }
        }
    }
}
