package com.yao.spider.zhihu.task;

import com.yao.spider.common.config.CommonConfig;
import com.yao.spider.core.entity.Page;
import com.yao.spider.core.factory.ParserFactory;
import com.yao.spider.core.http.util.HttpClientUtil;
import com.yao.spider.core.parser.IPageParser;
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
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by user on 2018/3/28.
 */
public class ZhiHuUserListTask implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(ZhiHuUserListTask.class);
    private String url;
    private Proxy proxy;
    private boolean ebableProxy;
    private HttpRequestBase request;
    private String userToken;
    //同一个token重试次数记录，如果超过五次就放弃不在重试
    private int retryTimes;


    public ZhiHuUserListTask(String url, boolean enableProxy) {
        this.url = url;
        this.ebableProxy = enableProxy;
    }
    public ZhiHuUserListTask(String url, boolean enableProxy, String userToken) {
        this.url = url;
        this.ebableProxy = enableProxy;
        this.userToken = userToken;
    }

    public ZhiHuUserListTask(String url, boolean enableProxy, String userToken, int retryTimes) {
        this.url = url;
        this.ebableProxy = enableProxy;
        this.userToken = userToken;
        this.retryTimes = retryTimes;
    }


    public void run() {
        HttpGet request = new HttpGet(url);
        request.setHeader("authorization","oauth " + ZhiHuConfig.authorization);
        Page page = null;
        long requestTime = System.currentTimeMillis();
        try {
            if (ebableProxy) {
//                logger.info("当前可用代理:" + ProxyPool.proxyQueue.size());
                proxy = ProxyPool.proxyQueue.take();
                HttpHost host = new HttpHost(this.proxy.getIp(),this.proxy.getPort());
                request.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(host).build());
                page = ZhiHuHttpClient.getInstance().getPage(request);
            } else {
                page = ZhiHuHttpClient.getInstance().getPage(this.url);
            }
            if (page != null && page.getStatusCode() == 200) {
//                logger.info("Request SuccessFully：" + (requestTime - System.currentTimeMillis())/1000 + "s");
                handPage(page);
            } else {
//                logger.info("Request failly：" + (System.currentTimeMillis() - requestTime)/1000 + "s");
                this.proxy.setFailureTimes(proxy.getFailureTimes() + 1);
                retry();
            }
        } catch (Exception e) {
            proxy.setFailureTimes(proxy.getFailureTimes() + 1);
            retry();
        } finally {
            if (request != null) {
                request.releaseConnection();
            }
            if (proxy != null && !ProxyUtil.isDiscardProxy(proxy)) {
                ProxyPool.proxyQueue.add(proxy);
            }
        }
    }

    public void handPage(Page page) {
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
                    // 先查询usertoken是否已经爬过
                    if (userTokenDao.judgeAndInsert(new UserToken(user.getUserToken()))) {
                        if (!ZhiHuHttpClient.getInstance().getUserListDownTask().isShutdown()) {
                            for (int j = 0, len = user.getFollowees(); j < len / 20; j++) {
                                if (ZhiHuHttpClient.getInstance().getUserListDownTask().getQueue().size() > 888) {
                                    continue;
                                }
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
//        logger.info("重试" + this.userToken + "---重试次数：" + retryTimes + "---代理：" + proxy.getProxyStr());
        if (retryTimes < 5 || ZhiHuConfig.startUserToken.equals(this.userToken)) {
            ZhiHuHttpClient.getInstance().getUserListDownTask().execute(new ZhiHuUserListTask(this.url, true, this.userToken, this.retryTimes + 1));
        }
    }
}
