package com.yao.spider.douban.task;

import com.yao.spider.common.config.CommonConfig;
import com.yao.spider.common.constants.Constants;
import com.yao.spider.douban.DoubanHttpClient;
import com.yao.spider.douban.dao.IMoveDao;
import com.yao.spider.douban.dao.Impl.MoveDaoImpl;
import com.yao.spider.douban.entity.move.Move;
import com.yao.spider.core.parser.IPageParser;
import com.yao.spider.core.factory.ParserFactory;
import com.yao.spider.douban.parsers.move.MoveParser;
import com.yao.spider.proxytool.ProxyPool;
import com.yao.spider.core.entity.Page;
import com.yao.spider.proxytool.entity.Proxy;
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
public class DouBanInfoListPageTask implements Runnable{
    private static Logger logger = LoggerFactory.getLogger(DouBanInfoListPageTask.class);
    private String url;
    private boolean enableProxy;
    private Proxy proxy;
    private DoubanHttpClient doubanHttpClient = DoubanHttpClient.getInstance();
    private int retryTime;
    private int startNumber;

    public DouBanInfoListPageTask(String url, boolean enableProxy) {
        this.url = url;
        this.enableProxy = enableProxy;
    }
    public DouBanInfoListPageTask(String url, boolean enableProxy, int retryTime, int startNumber) {
        this.url = url;
        this.enableProxy = enableProxy;
        this.startNumber = startNumber;
        this.retryTime = retryTime;
    }

    public void run() {

        HttpGet request = new HttpGet(url);
        try {
            Page page = null;
            //下面这段代码是一段重复代码，之所以没有抽离出来是因为我发现多线程运行下代码过分重用会导致很难排查出错点
            if (enableProxy) {
                proxy = ProxyPool.proxyQueue.take();
                HttpHost host =new HttpHost(this.proxy.getIp(), this.proxy.getPort());
                request.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(host).build());
                page = doubanHttpClient.getPage(request);
            } else {
               page = doubanHttpClient.getPage(url);
            }
            if (page != null && page.getStatusCode() == 200) {
                proxy.setSuccessfulTimes(proxy.getSuccessfulTimes() + 1);
                handle(page);
            } else {
                proxy.setFailureTimes(proxy.getFailureTimes() + 1);
                retry();
            }
        } catch (Exception e) {
            proxy.setFailureTimes(proxy.getFailureTimes() + 1);
            retry();
        } finally {
            if (request != null) {
                request.releaseConnection();
            }

            if (proxy != null && !ProxyUtil.isDiscardProxy(proxy)){
                ProxyPool.proxyQueue.add(proxy);
            } else {
                logger.info("丢弃代理：" + proxy.getProxyStr());
            }
        }
    }


    private void retry() {
//        logger.info("电影列表重试次数=" + retryTime + "--开始编号：" + startNumber + "---重试代理：" + proxy.getProxyStr() + "---代理失败/成功次数：" + proxy.getFailureTimes()+ "/" + proxy.getSuccessfulTimes());
        doubanHttpClient.getDownLoadMoveListExector().execute(new DouBanInfoListPageTask(url, true, retryTime + 1, startNumber));
    }

    private void handle(Page page) {
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
