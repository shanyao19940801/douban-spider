package com.yao.douban.proxytool.task;

import com.yao.douban.proxytool.ProxyPool;
import com.yao.douban.proxytool.entity.Page;
import com.yao.douban.proxytool.entity.Proxy;
import com.yao.douban.proxytool.http.client.ProxyHttpClient;
import com.yao.douban.proxytool.http.util.HttpClientUtil;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by 单耀 on 2018/1/27.
 * 下载代理页面代理任务类
 */
public class ProxyPageTask implements Runnable{
    private static Logger logger = LoggerFactory.getLogger(ProxyPageTask.class);

    private String  url;
    private boolean isUseProxy;
    private Proxy currentProxy;
    private ProxyHttpClient proxyHttpClient = ProxyHttpClient.getInstance();

    public ProxyPageTask(String url, boolean isUseProxy) {
        this.url = url;
        this.isUseProxy = isUseProxy;
    }

    public void run() {
        HttpGet request = null;
        try {
            Page page = new Page();
            if (isUseProxy) {

                currentProxy = ProxyPool.proxyQueue.take();
                HttpHost proxy = new HttpHost(currentProxy.getIp(), currentProxy.getPort());
                request.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(proxy).build());
                page = proxyHttpClient.getPage(request);
            } else {
                page = proxyHttpClient.getPage(url);
            }
            page.setProxy(currentProxy);
            int status = page.getStatusCode();
            String logStr = Thread.currentThread().getName() + " " + getProxyStr(currentProxy) +
                    " executing request url: " + page.getUrl() + " response statusCode:" + status;

            logger.debug(logStr);

            if (status == 200) {
                handle(page);
            } else {
                Thread.sleep(100);
                retry();
            }

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {

            if (currentProxy != null) {
                ProxyPool.proxyQueue.add(currentProxy)
            }

            if (request != null) {
                request.releaseConnection();
            }
        }
    }

    //TODO
    private void retry() {

    }

    //TODO
    public void handle(Page page) {

    }

    private String getProxyStr(Proxy proxy) {
        if (proxy == null)
            return "";

        return proxy.getIp() + ":" + proxy.getPort();
    }
}
