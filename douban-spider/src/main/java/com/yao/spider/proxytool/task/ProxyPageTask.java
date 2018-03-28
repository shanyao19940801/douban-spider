package com.yao.spider.proxytool.task;

import com.yao.spider.proxytool.ProxyHttpClient;
import com.yao.spider.proxytool.ProxyPool;
import com.yao.spider.proxytool.entity.Page;
import com.yao.spider.proxytool.entity.Proxy;
import com.yao.spider.proxytool.http.util.HttpClientUtil;
import com.yao.spider.proxytool.parses.ParserFactory;
import com.yao.spider.proxytool.parses.ProxyListPageParser;
import com.yao.spider.proxytool.proxyutil.ProxyUtil;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by 单耀 on 2018/1/27.
 * 下载代理页面代理任务类
 */
public class ProxyPageTask implements Runnable{
    private static Logger logger = LoggerFactory.getLogger(ProxyPageTask.class);

    //是否继续下载代理
    public static volatile boolean isContinueDownProxy = true;
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

        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
            retry();
        }  finally {

            if (currentProxy != null && !ProxyUtil.isDiscardProxy(currentProxy)){
                ProxyPool.proxyQueue.add(currentProxy);
            }

            if (request != null) {
                request.releaseConnection();
            }
        }
    }

    private void retry() {
        proxyHttpClient.getProxyDoloadThreadExector().execute(new ProxyPageTask(url, true));
    }

    //T处理下载页面
    public void handle(Page page) {
        if (page.getHtml() == null || "".equals(page.getHtml())) {
            return;
        }
        ProxyListPageParser parser = ParserFactory.getProxyListPageParser(ProxyPool.proxyMap.get(url));
        List<Proxy> proxyList =  parser.parse(page.getHtml());
        if (isContinueDownProxy) {
            for (Proxy proxy : proxyList) {
                //测试代理是否可用
                proxyHttpClient.getProxyProxyTestExector().execute(new ProxyTestTask(proxy));
            }
        }


    }

    private String getProxyStr(Proxy proxy) {
        if (proxy == null)
            return "";

        return proxy.getIp() + ":" + proxy.getPort();
    }
}
