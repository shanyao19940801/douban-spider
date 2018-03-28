package com.yao.spider.douban.task;

import com.yao.spider.common.constants.Constants;
import com.yao.spider.douban.DoubanHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;

/**
 * Created by 单耀 on 2018/2/5.
 */
public class SpiderDouBanInfo implements Runnable {
    public static volatile boolean isContinue = true;
    private DoubanHttpClient doubanHttpClient = DoubanHttpClient.getInstance();
    //电影开始条数
    public static volatile int MOVE_START = 0;
    private static Logger logger = LoggerFactory.getLogger(SpiderDouBanInfo.class);
    public void run() {
        while (isContinue) {
            //TODO 后期改成可拓展，即可以查询其他信息
            try {
                String url = String.format(Constants.STRTY_URL_MOVE, Constants.TYPE, URLEncoder.encode(Constants.TAG,"UTF-8"), Constants.LIMIT, MOVE_START);
                logger.info("开始条数：" + MOVE_START);
                //TODO 改成future 放回结果
                doubanHttpClient.getDownLoadMoveListExector().execute(new DouBanInfoListPageTask( url, true, 0, MOVE_START));
                MOVE_START += Constants.LIMIT;
                Thread.sleep(10000);

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
