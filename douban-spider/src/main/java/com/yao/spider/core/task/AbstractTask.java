package com.yao.spider.core.task;

import com.yao.spider.core.entity.Page;
import com.yao.spider.core.http.client.AbstractHttpClient;
import com.yao.spider.core.http.util.HttpClientUtil;
import com.yao.spider.core.util.ProxyUtil;
import com.yao.spider.proxytool.ProxyPool;
import com.yao.spider.proxytool.entity.Proxy;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTask<T> implements Runnable{
    Logger logger = LoggerFactory.getLogger(AbstractTask.class);

    protected boolean isUseProxy;
    protected String url;
    protected AbstractHttpClient httpClient; //具体实例化放在子类中
    protected Proxy currentProxy;
    public void getPage(String url) {
        this.getPage(url, isUseProxy);
    }

    public void getPage(String url, boolean isUseProxy) {
        this.url = url;
        this.isUseProxy = isUseProxy;

        HttpGet request = new HttpGet(url);
        try {
            Page page = null;
            if (isUseProxy) {
                currentProxy = ProxyPool.proxyQueue.take();
                HttpHost proxy = new HttpHost(currentProxy.getIp(), currentProxy.getPort());
                request.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(proxy).build());
                page = httpClient.getPage(request);
            } else {
                page = httpClient.getPage(url);
            }
            if (page != null && page.getStatusCode() == 200) {
                if (currentProxy != null)
                    currentProxy.setSuccessfulTimes(currentProxy.getSuccessfulTimes() + 1);
                handle(page);
            } else {
                currentProxy.setFailureTimes(currentProxy.getFailureTimes() + 1);
                retry();
            }
        } catch (Exception e) {
            currentProxy.setFailureTimes(currentProxy.getFailureTimes() + 1);
            retry();
        } finally {
            if (request != null) {
                request.releaseConnection();
            }

            if (currentProxy != null && !ProxyUtil.isDiscardProxy(currentProxy)) {
                ProxyPool.proxyQueue.add(currentProxy);
            } else {
                if (currentProxy != null)
                    logger.info("丢弃代理：" + currentProxy.getProxyStr());
            }
        }
    }

    public abstract void retry();

    public abstract void handle(Page page);
}
