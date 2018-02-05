package com.yao.douban.douban.task;

import com.yao.douban.douban.DoubanHttpClient;
import com.yao.douban.douban.entity.move.ListMove;
import com.yao.douban.douban.entity.move.Move;
import com.yao.douban.douban.parsers.DoubanPageParser;
import com.yao.douban.douban.parsers.DoubanParserFactory;
import com.yao.douban.douban.parsers.move.MoveListParser;
import com.yao.douban.proxytool.ProxyPool;
import com.yao.douban.proxytool.entity.Page;
import com.yao.douban.proxytool.entity.Proxy;
import com.yao.douban.proxytool.http.util.HttpClientUtil;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by 单耀 on 2018/1/28.
 * 下载电影信息列表页面
 */
public class DouBanInfoListPageTask implements Runnable{
    private String url;
    private boolean isUserProxy;
    private Proxy currentProxy;
    private DoubanHttpClient doubanHttpClient = DoubanHttpClient.getInstance();
    private static Logger logger = LoggerFactory.getLogger(DouBanInfoListPageTask.class);

    public DouBanInfoListPageTask(String url, boolean isUserProxy) {
        this.url = url;
        this.isUserProxy = isUserProxy;
    }

    public void run() {
        System.out.println("kaishi");
        HttpGet request = new HttpGet(url);
        try {
            Page page = null;
            if (isUserProxy) {
                currentProxy = ProxyPool.proxyQueue.take();
                HttpHost proxy =new HttpHost(currentProxy.getIp(), currentProxy.getPort());
                request.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(proxy).build());
                page = doubanHttpClient.getPage(request);
            } else {
               page = doubanHttpClient.getPage(url);
            }
            if (page != null && page.getStatusCode() == 200) {
                handle(page);
            } else {
                retry();
            }
        } catch (Exception e) {
//            e.printStackTrace();
            logger.error(e.getMessage(), e);
            retry();
        } finally {
            if (request != null) {
                request.releaseConnection();
            }
            if (currentProxy != null) {
                ProxyPool.proxyQueue.add(currentProxy);
            }
        }
    }


    private void retry() {
        doubanHttpClient.getDownLoadMoveListExector().execute(new DouBanInfoListPageTask(url, true));
    }

    private void handle(Page page) {
        if (page.getHtml() == null || "".equals(page.getHtml())) {
            return;
        }
        DoubanPageParser parser = DoubanParserFactory.getDoubanParserFactory(MoveListParser.class);
        List<ListMove> moveList = parser.parser(page.getHtml());
        if (moveList != null && moveList.size() > 0) {
            for (ListMove move : moveList) {
                System.out.println(move.toString());
                doubanHttpClient.getDownLoadMoveInfoExector().execute(new DouBanDetailInfoDownLoadTask(move, true));
            }
        }
    }
}
