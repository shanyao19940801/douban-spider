package com.yao.douban.proxytool.task;

import com.yao.douban.proxytool.ProxyPool;
import com.yao.douban.proxytool.entity.Page;
import com.yao.douban.proxytool.entity.Proxy;
import com.yao.douban.proxytool.http.client.ProxyHttpClient;
import com.yao.douban.proxytool.http.util.HttpClientUtil;
import com.yao.douban.proxytool.proxyutil.ProxyConstants;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
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
        HttpGet request = new HttpGet(ProxyConstants.INDEX_URL);
        try {
            HttpHost proxyHost = new HttpHost(proxy.getIp(), proxy.getPort());
            RequestConfig requestConfig = HttpClientUtil.getRequestConfigBuilder().setProxy(proxyHost).build();
            request.setConfig(requestConfig);
//            CloseableHttpResponse response = HttpClientUtil.getResponse(request);
            Page page = ProxyHttpClient.getInstance().getPage(request);
            String logStr = Thread.currentThread().getName() + " " + proxy.getProxyStr() +
                    "  executing request " + page.getUrl()  + " response statusCode:" + page.getStatusCode();

            if (page == null || page.getStatusCode() != 200) {
                logger.warn("该代理不可用：" + logStr);
                return;
            }
            if (page.getStatusCode() == 200) {
                ProxyPool.proxyQueue.add(proxy);
                logger.debug(proxy.getProxyStr() + "-----代理可用-----");
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (request != null) {
                request.releaseConnection();
            }
        }
    }
}
