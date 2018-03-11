package com.yao.douban.douban.task;

import com.yao.douban.core.util.Constants;
import com.yao.douban.douban.DoubanHttpClient;
import com.yao.douban.douban.dao.IMoveDao;
import com.yao.douban.douban.dao.Impl.MoveDaoImpl;
import com.yao.douban.douban.entity.move.Move;
import com.yao.douban.douban.parsers.DoubanPageParser;
import com.yao.douban.douban.parsers.DoubanParserFactory;
import com.yao.douban.douban.parsers.move.MoveParser;
import com.yao.douban.proxytool.ProxyPool;
import com.yao.douban.proxytool.entity.Page;
import com.yao.douban.proxytool.entity.Proxy;
import com.yao.douban.proxytool.http.util.HttpClientUtil;
import com.yao.douban.proxytool.proxyutil.ProxyUtil;
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
    private String url;
    private boolean isUserProxy;
    private Proxy currentProxy;
    private DoubanHttpClient doubanHttpClient = DoubanHttpClient.getInstance();
    private static Logger logger = LoggerFactory.getLogger(DouBanInfoListPageTask.class);
    private int retryTime;
    private int startNumber;

    public DouBanInfoListPageTask(String url, boolean isUserProxy) {
        this.url = url;
        this.isUserProxy = isUserProxy;
    }
    public DouBanInfoListPageTask(String url, boolean isUserProxy, int retryTime, int startNumber) {
        this.url = url;
        this.isUserProxy = isUserProxy;
        this.startNumber = startNumber;
        this.retryTime = retryTime;
    }

    public void run() {

        HttpGet request = new HttpGet(url);
        try {
            Page page = null;
            //下面这段代码是一段重复代码，之所以没有抽离出来是因为我发现多线程运行下代码过分重用会导致很难排查出错点
            if (isUserProxy) {
                currentProxy = ProxyPool.proxyQueue.take();
                HttpHost proxy =new HttpHost(currentProxy.getIp(), currentProxy.getPort());
                request.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(proxy).build());
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
//            e.printStackTrace();
//            logger.error(e.getMessage(), e);
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
        }
    }


    private void retry() {
        logger.info("电影列表重试次数=" + retryTime + "--开始编号：" + startNumber + "---重试代理：" + currentProxy.getProxyStr() + "---代理失败/成功次数：" + currentProxy.getFailureTimes()+ "/" + currentProxy.getSuccessfulTimes());
        doubanHttpClient.getDownLoadMoveListExector().execute(new DouBanInfoListPageTask(url, true, retryTime + 1, startNumber));
    }

    private void handle(Page page) {
        if (page.getHtml() == null || "".equals(page.getHtml())) {
            return;
        }
        DoubanPageParser parser = DoubanParserFactory.getDoubanParserFactory(MoveParser.class);
        List<Move> moveList = parser.parser(page.getHtml());
        if (moveList != null && moveList.size() > 0) {
            IMoveDao moveDao = new MoveDaoImpl();
            moveDao.insertList(moveList);
            for (Move move : moveList) {
                logger.info(move.toString());

            }
        }
        //深度爬虫获取电影详细信息
        if (Constants.ISDEEP) {
            if (moveList != null && moveList.size() > 0) {
                for (Move move : moveList) {
                    logger.info(move.toString());
//                doubanHttpClient.getDownLoadMoveInfoExector().execute(new DouBanDetailInfoDownLoadTask(move, true));
                }
            }
        }
    }
}
