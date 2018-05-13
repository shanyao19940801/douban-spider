package com.yao.spider.zhihu.task;

import com.yao.spider.common.config.CommonConfig;
import com.yao.spider.core.entity.Page;
import com.yao.spider.core.factory.ParserFactory;
import com.yao.spider.core.http.util.HttpClientUtil;
import com.yao.spider.core.parser.IPageParser;
import com.yao.spider.core.task.AbstractTask;
import com.yao.spider.core.util.ProxyUtil;
import com.yao.spider.proxytool.ProxyPool;
import com.yao.spider.proxytool.entity.Proxy;
import com.yao.spider.zhihu.ZhiHuHttpClient;
import com.yao.spider.zhihu.config.ZhiHuConfig;
import com.yao.spider.zhihu.dao.IUserDao;
import com.yao.spider.zhihu.dao.IUserTokenDao;
import com.yao.spider.zhihu.dao.Impl.UserDaoImpl;
import com.yao.spider.zhihu.dao.Impl.UserTokenDaoImpl;
import com.yao.spider.zhihu.entity.User;
import com.yao.spider.zhihu.entity.UserToken;
import com.yao.spider.zhihu.parsers.ZhiHuUserParser;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by user on 2018/3/28.
 */
public class ZhiHuUserListTask extends AbstractTask<ZhiHuUserListTask> {
    private static final Logger logger = LoggerFactory.getLogger(ZhiHuUserListTask.class);
    private String userToken;
    public ZhiHuUserListTask(String url, boolean isUseProxy) {
        this(url,isUseProxy,null);
    }
    public ZhiHuUserListTask(String url, boolean isUseProxy, String userToken) {
        this(url, isUseProxy, userToken, 0);
    }

    public ZhiHuUserListTask(String url, boolean enableProxy, String userToken, int retryTimes) {
        this.url = url;
        this.isUseProxy = enableProxy;
        this.userToken = userToken;
        this.retryTimes = retryTimes;
    }


    public void run() {
        getPage(url);
        /*HttpGet request = new HttpGet(url);
        request.setHeader("authorization","oauth " + ZhiHuConfig.authorization);
        Page page = null;
        try {
            if (isUseProxy) {
//                logger.info("当前可用代理:" + ProxyPool.proxyQueue.size());
                this.currentProxy = ProxyPool.proxyQueue.take();
                HttpHost host = new HttpHost(this.currentProxy.getIp(),this.currentProxy.getPort());
                request.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(host).build());
                page = ZhiHuHttpClient.getInstance().getPage(request);
            } else {
                page = ZhiHuHttpClient.getInstance().getPage(this.url);
            }
            if (page != null && page.getStatusCode() == 200) {
                handle(page);
            } else {
//                logger.info("Request failly：" + (System.currentTimeMillis() - requestTime)/1000 + "s");
                this.currentProxy.setFailureTimes(currentProxy.getFailureTimes() + 1);
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
            }
        }*/
    }

    public void handle(Page page) {
        IPageParser pageParser = ParserFactory.getParserClass(ZhiHuUserParser.class);
        if (pageParser != null) {
            List<User> list = pageParser.parser(page.getHtml());
            if (list != null && list.size() > 0) {
                int listSize = list.size();
                User user = null;
                for (int i = 0; i < listSize; i ++) {
                    logger.info(list.get(i).toString());
                }
                IUserTokenDao userTokenDao = new UserTokenDaoImpl();
                for (int i = 0; i < listSize; i++) {
                    user = list.get(i);
                    // 先查询usertoken是否已经爬过 //TODO 改为redis存储
                    if (userTokenDao.judgeAndInsert(new UserToken(user.getUserToken()))) {
                        if (!ZhiHuHttpClient.getInstance().getUserListDownTask().isShutdown()) {
                            for (int j = 0, len = user.getFollowees(); j < len / 20; j++) {
                                String nextUrl = String.format(ZhiHuConfig.FOLLOWEES_API, user.getUserToken(), j * 20);
                                ZhiHuHttpClient.getInstance().getUserListDownTask().execute(new ZhiHuUserListTask(nextUrl, true, user.getUserToken()));
                            }
                        }
                    }
                }
                long dbstart = System.currentTimeMillis();
                if (CommonConfig.dbEnable) {
                    IUserDao dao = new UserDaoImpl();
                    int count = 0;
                    for (int i = 0; i < listSize; i ++) {
                        count += dao.inserSelective(list.get(i));
                    }
                    logger.info("保存到数据库消耗时间："+ (dbstart - System.currentTimeMillis())/ 1000 + " 保存了" + count + "条");
                }

            }
        }
    }

    public void retry() {
//        logger.info("当前活跃线程数：" + ZhiHuHttpClient.getInstance().getUserListDownTask().getActiveCount());
//        logger.info("重试" + this.userToken + "---重试次数：" + retryTimes + "---代理：" + currentProxy.getProxyStr());
        if (retryTimes < 5 || ZhiHuConfig.startUserToken.equals(this.userToken)) {
            ZhiHuHttpClient.getInstance().getUserListDownTask().execute(new ZhiHuUserListTask(this.url, true, this.userToken, this.retryTimes + 1));
        }
    }


}
