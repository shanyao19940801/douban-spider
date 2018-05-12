package com.yao.spider.douban.task;

import com.yao.spider.common.config.CommonConfig;
import com.yao.spider.common.constants.Constants;
import com.yao.spider.core.task.AbstractTask;
import com.yao.spider.douban.DoubanHttpClient;
import com.yao.spider.douban.dao.IMoveDao;
import com.yao.spider.douban.dao.Impl.MoveDaoImpl;
import com.yao.spider.douban.entity.move.Move;
import com.yao.spider.core.parser.IPageParser;
import com.yao.spider.core.factory.ParserFactory;
import com.yao.spider.douban.parsers.move.MoveParser;
import com.yao.spider.proxytool.ProxyPool;
import com.yao.spider.core.entity.Page;
import com.yao.spider.core.http.util.HttpClientUtil;
import com.yao.spider.core.util.ProxyUtil;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by 单耀 on 2018/1/28.
 * 下载电影信息列表页面
 */
public class DouBanInfoListPageTask extends AbstractTask<DouBanInfoListPageTask> implements Runnable{
    private static Logger logger = LoggerFactory.getLogger(DouBanInfoListPageTask.class);
    private DoubanHttpClient doubanHttpClient = DoubanHttpClient.getInstance();
    private int startNumber;

    public DouBanInfoListPageTask(String url, boolean isUseProxy) {
        this.url = url;
        this.isUseProxy = isUseProxy;
    }
    public DouBanInfoListPageTask(String url, boolean isUseProxy, int retryTimes, int startNumber) {
        this.url = url;
        this.isUseProxy = isUseProxy;
        this.startNumber = startNumber;
        this.retryTimes = retryTimes;
    }

    public void run() {
        getPage(url);
        /*HttpGet request = new HttpGet(url);
        try {
            Page page = null;
            if (isUseProxy) {
                currentProxy = ProxyPool.proxyQueue.take();
                HttpHost host =new HttpHost(this.currentProxy.getIp(), this.currentProxy.getPort());
                request.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(host).build());
                page = doubanHttpClient.getPage(request);
            } else {
               page = doubanHttpClient.getPage(url);
            }
            if (page != null && page.getStatusCode() == 200) {
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

            if (currentProxy != null && !ProxyUtil.isDiscardProxy(currentProxy)){
                ProxyPool.proxyQueue.add(currentProxy);
            } else {
                logger.info("丢弃代理：" + currentProxy.getProxyStr());
            }
        }*/
    }


    public void retry() {
//        logger.info("电影列表重试次数=" + retryTimes + "--开始编号：" + startNumber + "---重试代理：" + currentProxy.getProxyStr() + "---代理失败/成功次数：" + currentProxy.getFailureTimes()+ "/" + currentProxy.getSuccessfulTimes());
        doubanHttpClient.getDownLoadMoveListExector().execute(new DouBanInfoListPageTask(url, true, retryTimes + 1, startNumber));
    }

    public void handle(Page page) {
        if (page.getHtml() == null || "".equals(page.getHtml())) {
            return;
        }
        IPageParser parser = ParserFactory.getParserClass(MoveParser.class);
        List<Move> moveList = parser.parser(page.getHtml());
        if (moveList != null && moveList.size() > 0) {
            for (Move move : moveList) {
                logger.info(move.toString());
            }
            if (CommonConfig.dbEnable) {
                IMoveDao moveDao = new MoveDaoImpl();
                moveDao.insertList(moveList);
            }
        }
        //深度爬虫获取电影详细信息
        if (Constants.ISDEEP) {
            if (moveList != null && moveList.size() > 0) {
                for (Move move : moveList) {
                    logger.info(move.toString());
                    doubanHttpClient.getDownLoadMoveInfoExector().execute(new DouBanDetailInfoDownLoadTask(move, true));
                }
            }
        }
    }
}
